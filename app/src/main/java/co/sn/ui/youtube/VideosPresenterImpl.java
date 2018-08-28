package co.sn.ui.youtube;

import android.text.TextUtils;

import javax.inject.Inject;

import co.sn.data.DataManager;
import co.sn.data.network.retrofit.RetrofitException;
import co.sn.ui.base.BasePresenter;
import co.sn.utils.AppConstants;
import co.sn.utils.StringUtils;
import co.sn.utils.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;

public class VideosPresenterImpl<V extends VideosView> extends BasePresenter<V> implements VideosPresenter<V> {

    private String nextPageToken;
    private boolean isMoreData = false;
    private boolean isDataLoading = false;
    private String search;

    @Inject
    public VideosPresenterImpl(DataManager dataManager,
                               SchedulerProvider schedulerProvider,
                               CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public boolean isMoreData() {
        return isMoreData;
    }

    @Override
    public void setMoreData(boolean moreData) {
        isMoreData = moreData;
    }

    @Override
    public boolean isDataLoading() {
        return isDataLoading;
    }

    @Override
    public void setDataLoading(boolean dataLoading) {
        isDataLoading = dataLoading;
    }

    @Override
    public String getSearch() {
        return search;
    }

    @Override
    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public void resetValues() {
        nextPageToken = null;
        isMoreData = false;
        isDataLoading = false;
    }

    @Override
    public void getSearchVideos() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .getSearchVideos(search, nextPageToken, AppConstants.getGapiKey())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(searchYoutubeItemModel -> {

                    if (!isViewAttached()) {
                        return;
                    }

                    setDataLoading(false);
                    if (TextUtils.isEmpty(searchYoutubeItemModel.getNextPageToken())) {
                        isMoreData = false;
                    } else {
                        isMoreData = true;
                        nextPageToken = searchYoutubeItemModel.getNextPageToken();
                    }
                    getMvpView().hideLoading();
                    getMvpView().onVideosFetched(searchYoutubeItemModel.getYoutubeItems());
                }, throwable -> {

                    if (!isViewAttached()) {
                        return;
                    }

                    setDataLoading(false);
                    getMvpView().hideLoading();
                    handleError((RetrofitException) throwable);
                }));
    }

    @Override
    public void getMostPopularVideos() {
        getMvpView().showLoading();
        getCompositeDisposable().add(getDataManager()
                .getMostPopularVideos(nextPageToken, AppConstants.getGapiKey())
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(listingYoutubeItemModel -> {

                    if (!isViewAttached()) {
                        return;
                    }

                    setDataLoading(false);
                    if (TextUtils.isEmpty(listingYoutubeItemModel.getNextPageToken())) {
                        isMoreData = false;
                    } else {
                        isMoreData = true;
                        nextPageToken = listingYoutubeItemModel.getNextPageToken();
                    }
                    getMvpView().hideLoading();
                    getMvpView().onVideosFetched(listingYoutubeItemModel.getYoutubeItems());
                }, throwable -> {

                    if (!isViewAttached()) {
                        return;
                    }

                    setDataLoading(false);
                    getMvpView().hideLoading();
                    handleError((RetrofitException) throwable);
                }));
    }

    @Override
    public String parseVideoDurationForApi(String duration) {
        duration = duration.replace("PT", "");

        boolean hasHour;
        int indexOfH = duration.indexOf("H");
        String hour = null;

        boolean hasMinute;
        int indexOfM = duration.indexOf("M");
        String minute = null;

        boolean hasSecond;
        int indexOfS = duration.indexOf("S");
        String second = null;

        if (duration.contains("H")) {
            hasHour = true;
            String hourStr = duration.substring(0, indexOfH);
            hour = StringUtils.getTwoDigitString(hourStr);
        } else {
            hasHour = false;
        }
        if (duration.contains("M")) {
            hasMinute = true;
            String minStr;
            if (hasHour) {
                minStr = duration.substring(indexOfH + 1, indexOfM);
            } else {
                minStr = duration.substring(0, indexOfM);
            }
            minute = StringUtils.getTwoDigitString(minStr);
        } else {
            hasMinute = false;
        }
        if (duration.contains("S")) {
            hasSecond = true;
            String secStr;
            if (hasHour || hasMinute) {
                if (hasMinute) {
                    secStr = duration.substring(indexOfM + 1, indexOfS);
                } else {
                    secStr = duration.substring(indexOfH + 1, indexOfS);
                }
            } else {
                secStr = duration.substring(0, indexOfS);
            }
            second = StringUtils.getTwoDigitString(secStr);
        } else {
            hasSecond = false;
        }
        return (hour == null ? "00" : hour) + ":" + (minute == null ? "00" : minute) + ":" + (second == null ? "00" : second);
    }
}
