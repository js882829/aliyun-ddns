package cn.howardliu.aliDdns.provider.descriptor;

import cn.howardliu.aliDdns.provider.IpEchoWanIpProvider;
import cn.howardliu.aliDdns.provider.WanIpProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Provider;

/**
 * <br>created at 17-4-25
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class IpEchoWanIpProviderDescriptor implements ProviderDescriptor {
    private static final WanIpProvider PROVIDER = new IpEchoWanIpProvider();

    @Override
    public String getName() {
        return "ipEcho";
    }

    @Override
    public WanIpProvider getWanIpProvider() {
        return PROVIDER;
    }
}
