package d3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tenant")
public class TenantProperties {

    private String defaultId;
    private String headerName;
    private boolean required;
    private int cacheMaxEntries;

    public String getDefaultId() {
        return defaultId;
    }

    public void setDefaultId(String defaultId) {
        this.defaultId = defaultId;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getCacheMaxEntries() {
        return cacheMaxEntries;
    }

    public void setCacheMaxEntries(int cacheMaxEntries) {
        this.cacheMaxEntries = cacheMaxEntries;
    }
}