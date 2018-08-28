package co.sn.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchYoutubeItem extends YoutubeItem {

    @Expose
    @SerializedName("id")
    private SearchId searchId;

    public SearchId getSearchId() {
        return searchId;
    }

    public void setSearchId(SearchId searchId) {
        this.searchId = searchId;
    }

    @Override
    public String getVideoId() {
        return searchId.getVideoId();
    }

    public class SearchId {

        @Expose
        @SerializedName("kind")
        String kind;

        @Expose
        @SerializedName("videoId")
        String videoId;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }
    }
}
