package me.gavin.app.daily;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 日报列表
 *
 * @author gavin.xiong 2017/4/26
 */
public class Daily {
    //日期,唯一,重复的直接覆盖
    @SerializedName("date")
    private String date;

    //当日新闻
    @SerializedName("stories")
    private List<Story> stories;

    //顶部ViewPager滚动显示的新闻
    @SerializedName("top_stories")
    private List<Story> topStories;

    public String getDate() {
        return date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public List<Story> getTopStories() {
        return topStories;
    }

    @Override
    public String toString() {
        return "Daily{" +
                "mDate='" + date + '\'' +
                ", mStories=" + stories +
                ", mTopStories=" + topStories +
                '}';
    }

    public static class Story {
        //id
        @SerializedName("id")
        private long id;

        //标题
        @SerializedName("title")
        private String title;

        //图像地址（官方 API 使用数组形式。目前暂未有使用多张图片的情形出现，曾见无 images 属性的情况，请在使用中注意 ）
        @SerializedName("images")
        private List<String> images;

        //图像地址 只有topStories才有
        @SerializedName("image")
        private String image;

        //类型,作用未知
        @SerializedName("type")
        private int type;

        //供 Google Analytics 使用
        @SerializedName("ga_prefix")
        private String gaPrefix;

        //消息是否包含多张图片（仅出现在包含多图的新闻中)
        @SerializedName("multipic")
        private boolean multiPic;

        private String date;

        public long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public List<String> getImageUrls() {
            return images;
        }

        public String getImageUrl() {
            return image;
        }

        public int getType() {
            return type;
        }

        public String getGaPrefix() {
            return gaPrefix;
        }

        public boolean isMultiPic() {
            return multiPic;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "Story{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", images=" + images +
                    ", image='" + image + '\'' +
                    ", type=" + type +
                    ", gaPrefix='" + gaPrefix + '\'' +
                    ", multiPic=" + multiPic +
                    '}';
        }
    }

}
