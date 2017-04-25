package cn.howardliu.aliDdns.client;

import cn.howardliu.aliDdns.DdnsConfig;
import cn.howardliu.aliDdns.provider.WanIpProvider;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.profile.DefaultProfile;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <br>created at 17-4-24
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class DdnsClient {
    private static final Logger logger = LoggerFactory.getLogger(DdnsClient.class);
    private static final String regionId = "cn-beijing";
    private static IAcsClient client = null;
    private static DdnsClient ddnsClient = null;
    private static String cachedRecordId;
    private DdnsConfig config;
    private WanIpProvider provider;

    private DdnsClient(DdnsConfig config, WanIpProvider provider) {
        this.config = config;
        this.provider = provider;
    }

    public static DdnsClient instance(DdnsConfig config, WanIpProvider provider) {
        ddnsClient = new DdnsClient(config, provider);
        DefaultProfile profile = DefaultProfile
                .getProfile(regionId, config.getAccessKeyId(), config.getAccessKeySecret());
        client = new DefaultAcsClient(profile);
        return ddnsClient;
    }

    public void clean() {
        cachedRecordId = null;
    }

    private static void updateCacheRecordId(String cacheRecordId) {
        DdnsClient.cachedRecordId = Validate.notNull(cacheRecordId);
    }

    public void checkAndUpdate() throws ClientException {
        if (!this.provider.wanIpV4Address().isPresent()) {
            return;
        }
        String ip = this.provider.wanIpV4Address().get();
        logger.debug("the local wan ip is " + ip);
        if (cachedRecordId == null) {
            this.checkAndUpdateAll(ip);
        } else {
            this.checkAndUpdate(cachedRecordId, ip);
        }
    }

    private void checkAndUpdate(String recordId, String ip) throws ClientException {
        DescribeDomainRecordInfoResponse response = this.getDomainRecord(recordId);
        if (response == null || !recordId.equals(response.getRecordId())) {
            this.checkAndUpdateAll(ip);
        } else if (!ip.equals(response.getValue())) {
            cachedRecordId = this.updateDomainRecord(ip).getRecordId();
        }
    }

    private void checkAndUpdateAll(String ip) throws ClientException {
        List<DescribeDomainRecordsResponse.Record> records = this.listDomainRecord();
        if (records.size() <= 0) {
            // add domain record
            AddDomainRecordResponse response = this.addDomainRecord(ip);
            cachedRecordId = response.getRecordId();
        } else if (records.size() == 1) {
            // check and update domain record
            DescribeDomainRecordsResponse.Record record = records.get(0);
            cachedRecordId = record.getRecordId();
            if (!ip.equals(record.getValue())) {
                UpdateDomainRecordResponse response = this.updateDomainRecord(ip);
                cachedRecordId = response.getRecordId();
            }
        } else {
            // other action
            logger.warn("There are multiple record named \"" + config.getRr()
                    + "\", I don't know which one needed to update");
        }
    }

    private List<DescribeDomainRecordsResponse.Record> listDomainRecord() throws ClientException {
        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
        request.setDomainName(config.getDomain());
        request.setPageNumber(1L);
        request.setPageSize(500L);
        request.setRRKeyWord(config.getRr());
        request.setAcceptFormat(FormatType.JSON);
        // TODO 分页查询所有记录
        return client.getAcsResponse(request).getDomainRecords();
    }

    private DescribeDomainRecordInfoResponse getDomainRecord(String recordId) throws ClientException {
        logger.debug("get domain record: [recordId=" + recordId + "]");
        DescribeDomainRecordInfoRequest request = new DescribeDomainRecordInfoRequest();
        request.setRecordId(recordId);
        return client.getAcsResponse(request);
    }

    private AddDomainRecordResponse addDomainRecord(String ip) throws ClientException {
        logger.debug("add domain record: " + ip);
        AddDomainRecordRequest request = new AddDomainRecordRequest();
        request.setDomainName(config.getDomain());
        request.setRR(config.getRr());
        request.setType("A");
        request.setValue(ip);
        return client.getAcsResponse(request);
    }

    private UpdateDomainRecordResponse updateDomainRecord(String ip) throws ClientException {
        return updateDomainRecord(cachedRecordId, ip);
    }

    private UpdateDomainRecordResponse updateDomainRecord(String recordId, String ip) throws ClientException {
        logger.debug("update domain record: [ip=" + ip + ", recordId=" + recordId + "]");
        UpdateDomainRecordRequest request = new UpdateDomainRecordRequest();
        request.setRecordId(recordId);
        request.setRR(config.getRr());
        request.setType("A");
        request.setValue(ip);
        UpdateDomainRecordResponse response = client.getAcsResponse(request);
        updateCacheRecordId(response.getRecordId());
        return response;
    }
}
