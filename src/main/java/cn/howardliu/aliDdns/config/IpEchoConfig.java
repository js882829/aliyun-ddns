package cn.howardliu.aliDdns.config;

/**
 * <br>created at 17-4-25
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class IpEchoConfig extends DdnsConfig {
    private String apiUrl;

    @Override
    public boolean isValid() {
        return this.apiUrl != null && super.isValid();
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public IpEchoConfig setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        return this;
    }
}
