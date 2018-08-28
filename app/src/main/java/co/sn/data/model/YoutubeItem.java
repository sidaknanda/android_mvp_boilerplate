package co.sn.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class YoutubeItem {

    @Expose
    @SerializedName("snippet")
    private Snippet snippet;

    @Expose
    @SerializedName("contentDetails")
    private ContentDetails contentDetails;

    public abstract String getVideoId();

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public ContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(ContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }

    public class Snippet {

        @Expose
        @SerializedName("title")
        private String title;

        @Expose
        @SerializedName("description")
        private String description;

        @Expose
        @SerializedName("thumbnails")
        private Thumbnails thumbnails;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Thumbnails getThumbnails() {
            return thumbnails;
        }

        public void setThumbnails(Thumbnails thumbnails) {
            this.thumbnails = thumbnails;
        }
    }

    public class ContentDetails {

        @Expose
        @SerializedName("duration")
        private String duration;

        @Expose
        @SerializedName("licensedContent")
        private boolean licensedContent;

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public boolean isLicensedContent() {
            return licensedContent;
        }

        public void setLicensedContent(boolean licensedContent) {
            this.licensedContent = licensedContent;
        }
    }

    public class Thumbnails {

        @Expose
        @SerializedName("default")
        private Thumbnail defaultVal;

        @Expose
        @SerializedName("medium")
        private Thumbnail mediumVal;

        @Expose
        @SerializedName("high")
        private Thumbnail highVal;

        public Thumbnail getDefaultVal() {
            return defaultVal;
        }

        public void setDefaultVal(Thumbnail defaultVal) {
            this.defaultVal = defaultVal;
        }

        public Thumbnail getMediumVal() {
            return mediumVal;
        }

        public void setMediumVal(Thumbnail mediumVal) {
            this.mediumVal = mediumVal;
        }

        public Thumbnail getHighVal() {
            return highVal;
        }

        public void setHighVal(Thumbnail highVal) {
            this.highVal = highVal;
        }
    }

    public class Thumbnail {

        @Expose
        @SerializedName("url")
        String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
