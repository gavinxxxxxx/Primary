package gavin.primary.widget.banner;

import java.io.Serializable;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/5/10
 */
public class BannerModel implements Serializable {

    private Long id;
    private String url;
    private String title;

    public BannerModel(Long id, String url, String title) {
        this.id = id;
        this.url = url;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
}
