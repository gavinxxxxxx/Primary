package me.gavin.primary.app.collection;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 收藏
 *
 * @author gavin.xiong 2017/6/27
 */
@Entity
public class Collection {

    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long id;
    private String image;
    private long time;
    private String tag;
    private String remark;

    @Generated(hash = 672185574)
    public Collection(Long id, String image, long time, String tag, String remark) {
        this.id = id;
        this.image = image;
        this.time = time;
        this.tag = tag;
        this.remark = remark;
    }

    @Generated(hash = 1149123052)
    public Collection() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
