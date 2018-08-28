package co.sn.ui.youtube;

import co.sn.ui.base.MvpPresenter;

public interface VideosPresenter<V extends VideosView> extends MvpPresenter<V> {

    boolean isMoreData();

    void setMoreData(boolean moreData);

    boolean isDataLoading();

    void setDataLoading(boolean dataLoading);

    String getSearch();

    void setSearch(String search);

    void resetValues();

    String parseVideoDurationForApi(String duration);

    void getSearchVideos();

    void getMostPopularVideos();
}
