package cn.howardliu.aliDdns.provider;

import cn.howardliu.aliDdns.config.DdnsConfig;
import cn.howardliu.aliDdns.config.IpEchoConfig;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Optional;

/**
 * <br>created at 17-4-24
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public final class IpEchoWanIpProvider implements WanIpProvider {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private IpEchoConfig config;

    @Override
    public Class<IpEchoConfig> getConfigClass() {
        return IpEchoConfig.class;
    }

    @Override
    public IpEchoWanIpProvider setConfig(DdnsConfig config) {
        if (config instanceof IpEchoConfig) {
            this.config = Validate.notNull((IpEchoConfig) config);
        } else {
            throw new IllegalArgumentException("parameter must be extended cn.howardliu.aliDdns.config.IpEchoConfig");
        }
        return this;
    }

    @Override
    public Optional<String> wanIpV4Address() {
        String apiUrl = this.config.getApiUrl();
        try {
            URL url = new URL(apiUrl);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            char[] buf = new char[1024];
            StringWriter writer = new StringWriter();
            int len;
            while ((len = reader.read(buf)) > 0) {
                writer.write(buf, 0, len);
            }
            return Optional.ofNullable(writer.toString());
        } catch (Exception e) {
            logger.error("can't get WAN IP(IpV4) use \"" + apiUrl + "\"", e);
        }
        return Optional.empty();
    }
}
