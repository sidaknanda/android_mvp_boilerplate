package co.sn.data.network;

import co.sn.data.model.ListingYoutubeItemModel;
import co.sn.data.model.SearchYoutubeItemModel;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiHelper {

    @GET("https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails&chart=mostPopular&regionCode=IN&maxResults=10")
    Observable<ListingYoutubeItemModel> getMostPopularVideos(@Query("pageToken") String pageToken, @Query("key") String apiKey);

    @GET("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=10&type=&video")
    Observable<SearchYoutubeItemModel> getSearchVideos(@Query("q") String searchQuery,
                                                       @Query("pageToken") String pageToken, @Query("key") String apiKey);
}