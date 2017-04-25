package cn.howardliu.aliyun.ddns;

import cn.howardliu.aliyun.ddns.provider.ipEcho.IpEchoWanIpProvider;
import cn.howardliu.aliyun.ddns.provider.ipEcho.config.IpEchoConfig;
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
public class IpEchoWanIpProviderFactoryTest {
    @Test
    public void wanIpV4Address() throws Exception {
        IpEchoWanIpProvider provider = new IpEchoWanIpProvider();
        provider.setConfig(new IpEchoConfig().setApiUrl("http://ipecho.net/plain"));
        Optional<String> optional = provider.wanIpV4Address();
        assertNotNull(optional);
        //noinspection ConstantConditions
        assertNotNull(optional.get());
        optional.ifPresent(System.out::println);
    }
}
