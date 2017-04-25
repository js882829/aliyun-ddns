package cn.howardliu.aliyun.ddns.runner;

import cn.howardliu.aliyun.ddns.DdnsConfigInvalidException;
import cn.howardliu.aliyun.ddns.client.DdnsClient;
import cn.howardliu.aliyun.ddns.config.DdnsConfig;
import cn.howardliu.aliyun.ddns.provider.ProviderFactory;
import cn.howardliu.aliyun.ddns.provider.WanIpProvider;
import com.alibaba.fastjson.JSONReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * <br>created at 17-4-24
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class Runner {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r);
        t.setName("ddns-check-scheduler");
        t.setDaemon(true);
        return t;
    });

    public static void main(String[] args) {
        String providerName = System.getProperty("ddns.provider.name");
        if (providerName == null || providerName.isEmpty()) {
            System.err.println(
                    "parameter ddns.provider.name can't be null or empty. use -Dddns.provider.name=\"the provider name you want to use\"");
            System.exit(1);
        }

        String home = System.getProperty("ddns.home");
        File homeDir = new File(home);
        File logDir = new File(homeDir, "logs");
        if (!logDir.exists()) {
            if (logDir.mkdirs()) {
                logger.warn("CAN'T CREATE DIRECTORY USE `mkdir`");
            }
        }

        String configJsonFileName = null;
        if (args != null && args.length > 0) {
            configJsonFileName = args[0];
        }
        if (configJsonFileName == null) {
            configJsonFileName = System.getProperty("ddns.config");
        }
        if (configJsonFileName == null) {
            configJsonFileName = home + File.separatorChar + "conf" + File.separatorChar + "config.json";
        }
        try {
            JSONReader jsonReader = new JSONReader(new InputStreamReader(new FileInputStream(configJsonFileName)));
            WanIpProvider wanIpProvider = ProviderFactory.getWanIpProvider(providerName);
            //noinspection ConstantConditions
            DdnsConfig config = jsonReader.readObject(wanIpProvider.getConfigClass());
            if (!config.isValid()) {
                throw new DdnsConfigInvalidException();
            }
            wanIpProvider.setConfig(config);
            check(wanIpProvider, config);
        } catch (FileNotFoundException e) {
            logger.error("can't find \"" + configJsonFileName + "\", please check again!");
            System.exit(1);
        } catch (DdnsConfigInvalidException e) {
            logger.error("config file \"" + configJsonFileName + "\" exist, but value is invalid!");
            System.exit(1);
        }
    }

    private static void check(WanIpProvider provider, DdnsConfig config) {
        DdnsClient ddnsClient = DdnsClient.instance(config, provider);
        try {
            ddnsClient.checkAndUpdate();
        } catch (Exception e) {
            logger.error("can't check/update domain record", e);
            ddnsClient.clean();
        }
        check(provider, config, config.getCheckInterval());
    }

    private static void check(WanIpProvider provider, DdnsConfig config, Long interval) {
        try {
            ScheduledFuture<?> future = executor.schedule(
                    () -> {
                        DdnsClient ddnsClient = DdnsClient.instance(config, provider);
                        try {
                            ddnsClient.checkAndUpdate();
                        } catch (Exception e) {
                            ddnsClient.clean();
                        }
                    },
                    interval,
                    TimeUnit.MILLISECONDS
            );
            future.get();
        } catch (Exception e) {
            logger.error("can't check/update domain record", e);
        } finally {
            check(provider, config, interval);
        }
    }
}
