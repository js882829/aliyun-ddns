package cn.howardliu.aliDdns.config;

/**
 * <br>created at 17-4-24
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class DdnsConfig {
    protected String accessKeyId;
    protected String accessKeySecret;
    protected Long checkInterval = 60000L;
    protected String domain;
    protected String rr;

    public boolean isValid() {
        return this.accessKeyId != null
                && this.accessKeySecret != null
                && this.checkInterval != null
                && this.checkInterval > 0
                && this.domain != null
                && this.rr != null;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public Long getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(Long checkInterval) {
        this.checkInterval = checkInterval;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRr() {
        return rr;
    }

    public void setRr(String rr) {
        this.rr = rr;
    }
}
