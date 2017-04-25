package cn.howardliu.aliDdns.provider;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <br>created at 17-4-25
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class ProviderFactoryTest {
    @Test
    public void getWanIpProvider() throws Exception {
        assertNotNull(ProviderFactory.getWanIpProvider("ipEcho"));
    }
}
