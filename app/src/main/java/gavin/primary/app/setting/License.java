package gavin.primary.app.setting;

import java.io.Serializable;

/**
 * 开源项目
 *
 * @author gavin.xiong 2017/4/24
 */
public class License implements Serializable {

    private String title;
    private String license;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
