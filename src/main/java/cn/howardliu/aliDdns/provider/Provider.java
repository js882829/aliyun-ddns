package cn.howardliu.aliDdns.provider;

import cn.howardliu.aliDdns.provider.descriptor.ProviderDescriptor;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * <br>created at 17-4-24
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class Provider {
    public static WanIpProvider getWanIpProvider(String name) {
        ServiceLoader<ProviderDescriptor> loader = ServiceLoader.load(ProviderDescriptor.class,
                Provider.class.getClassLoader());
        Iterator i$ = loader.iterator();
        ProviderDescriptor descriptor;
        do {
            if(!i$.hasNext()) {
                return null;
            }
            descriptor = (ProviderDescriptor)i$.next();
        } while(!name.equals(descriptor.getName()));
        return descriptor.getWanIpProvider();
    }
}
