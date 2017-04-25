package cn.howardliu.aliDdns.provider;

import java.util.Optional;

/**
 * <br>created at 17-4-24
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public interface WanIpProvider {
    Optional<String> wanIpV4Address();
}
