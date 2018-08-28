package co.sn.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchYoutubeItemModel {

    @Expose
    @SerializedName("items")
    private ArrayList<SearchYoutubeItem> youtubeItems;

    @Expose
    @SerializedName("nextPageToken")
    private String nextPageToken;

    @Expose
    @SerializedName("prevPageToken")
    private String prevPageToken;

    public ArrayList<SearchYoutubeItem> getYoutubeItems() {
        return youtubeItems;
    }

    public void setYoutubeItems(ArrayList<SearchYoutubeItem> youtubeItems) {
        this.youtubeItems = youtubeItems;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getPrevPageToken() {
        return prevPageToken;
    }

    public void setPrevPageToken(String prevPageToken) {
        this.prevPageToken = prevPageToken;
    }
}
