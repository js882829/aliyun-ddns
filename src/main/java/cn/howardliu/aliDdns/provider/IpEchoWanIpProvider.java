package cn.howardliu.aliDdns.provider;

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
public class IpEchoWanIpProvider implements WanIpProvider {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String apiUri;

    public IpEchoWanIpProvider(String apiUri) {
        this.apiUri = Validate.notNull(apiUri);
    }

    @Override
    public Optional<String> wanIpV4Address() {
        try {
            URL url = new URL(this.apiUri);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            char[] buf = new char[1024];
            StringWriter writer = new StringWriter();
            int len;
            while ((len = reader.read(buf)) > 0) {
                writer.write(buf, 0, len);
            }
            return Optional.ofNullable(writer.toString());
        } catch (Exception e) {
            logger.error("can't get WAN IP(IpV4) use \"" + this.apiUri + "\"", e);
        }
        return Optional.empty();
    }
}
