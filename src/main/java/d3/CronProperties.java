package d3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cron")
public class CronProperties {

    private boolean enabled;
    private boolean task;
    private boolean api;
    private boolean account;

    private long fixedDelayMail;
    private long fixedDelayTask;
    private long fixedDelayApi;
    private long fixedDelayAccount;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTask() {
        return task;
    }

    public void setTask(boolean task) {
        this.task = task;
    }

    public boolean isApi() {
        return api;
    }

    public void setApi(boolean api) {
        this.api = api;
    }

    public boolean isAccount() {
        return account;
    }

    public void setAccount(boolean account) {
        this.account = account;
    }

    public long getFixedDelayMail() {
        return fixedDelayMail;
    }

    public void setFixedDelayMail(long fixedDelayMail) {
        this.fixedDelayMail = fixedDelayMail;
    }

    public long getFixedDelayTask() {
        return fixedDelayTask;
    }

    public void setFixedDelayTask(long fixedDelayTask) {
        this.fixedDelayTask = fixedDelayTask;
    }

    public long getFixedDelayApi() {
        return fixedDelayApi;
    }

    public void setFixedDelayApi(long fixedDelayApi) {
        this.fixedDelayApi = fixedDelayApi;
    }

    public long getFixedDelayAccount() {
        return fixedDelayAccount;
    }

    public void setFixedDelayAccount(long fixedDelayAccount) {
        this.fixedDelayAccount = fixedDelayAccount;
    }
}