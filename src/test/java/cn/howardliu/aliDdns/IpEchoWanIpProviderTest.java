package cn.howardliu.aliDdns;

import cn.howardliu.aliDdns.provider.IpEchoWanIpProvider;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;

/**
 * <br>created at 17-4-24
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class IpEchoWanIpProviderTest {
    @Test
    public void wanIpV4Address() throws Exception {
        IpEchoWanIpProvider provider = new IpEchoWanIpProvider("http://ipecho.net/plain");
        Optional<String> optional = provider.wanIpV4Address();
        assertNotNull(optional);
        //noinspection ConstantConditions
        assertNotNull(optional.get());
        optional.ifPresent(System.out::println);
    }
}
