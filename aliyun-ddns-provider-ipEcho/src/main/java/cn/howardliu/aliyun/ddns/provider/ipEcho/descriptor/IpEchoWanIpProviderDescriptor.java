package cn.howardliu.aliyun.ddns.provider.ipEcho.descriptor;

import cn.howardliu.aliyun.ddns.provider.WanIpProvider;
import cn.howardliu.aliyun.ddns.provider.descriptor.ProviderDescriptor;
import cn.howardliu.aliyun.ddns.provider.ipEcho.IpEchoWanIpProvider;

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
