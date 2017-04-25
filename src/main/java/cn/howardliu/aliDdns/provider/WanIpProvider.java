package cn.howardliu.aliDdns.provider;

import cn.howardliu.aliDdns.config.DdnsConfig;

import java.util.Optional;

/**
 * <br>created at 17-4-24
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public interface WanIpProvider {
    default Class<? extends DdnsConfig> getConfigClass() {
        return DdnsConfig.class;
    }

    default WanIpProvider setConfig(DdnsConfig config) {
        return this;
    }

    Optional<String> wanIpV4Address();
}
