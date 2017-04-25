package cn.howardliu.aliDdns.provider.descriptor;

import cn.howardliu.aliDdns.provider.WanIpProvider;

/**
 * <br>created at 17-4-24
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ProviderDescriptor {
    String getName();

    WanIpProvider getWanIpProvider();
}
