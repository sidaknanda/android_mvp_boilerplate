package co.sn.di.module;

import android.app.Activity;
import android.content.Context;

import co.sn.di.ActivityContext;
import co.sn.di.PerActivity;
import co.sn.ui.base.BasePresenter;
import co.sn.ui.base.MvpPresenter;
import co.sn.ui.base.MvpView;
import co.sn.ui.youtube.VideosPresenter;
import co.sn.ui.youtube.VideosPresenterImpl;
import co.sn.ui.youtube.VideosView;
import co.sn.utils.rx.SchedulerProvider;
import co.sn.utils.rx.SchedulerProviderImpl;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new SchedulerProviderImpl();
    }

    @Provides
    @PerActivity
    MvpPresenter<MvpView> provideMvpPresenter(
            BasePresenter<MvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    VideosPresenter<VideosView> provideVideosPresenter(
            VideosPresenterImpl<VideosView> presenter) {
        return presenter;
    }
}