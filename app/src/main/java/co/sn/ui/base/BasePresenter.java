package co.sn.ui.base;

import android.os.Bundle;

import javax.inject.Inject;

import co.sn.app.R;
import co.sn.data.DataManager;
import co.sn.data.network.retrofit.RetrofitException;
import co.sn.utils.CommonUtils;
import co.sn.utils.rx.SchedulerProvider;
import io.reactivex.disposables.CompositeDisposable;
import rebus.permissionutils.PermissionEnum;

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private final DataManager mDataManager;
    private final SchedulerProvider mSchedulerProvider;
    private final CompositeDisposable mCompositeDisposable;

    private V mMvpView;

    @Inject
    public BasePresenter(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        this.mDataManager = dataManager;
        this.mSchedulerProvider = schedulerProvider;
        this.mCompositeDisposable = compositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mMvpView = null;
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    @Override
    public PermissionEnum[] getPermissionEnums(String... permissions) {
        return CommonUtils.getPermissionEnums(permissions);
    }

    public V getMvpView() {
        return mMvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    @Override
    public void handleError(RetrofitException error) {
        if (error == null) {
            getMvpView().onError(R.string.api_default_error);
            return;
        }

        if (error.getKind() == RetrofitException.Kind.HTTP) {
            getMvpView().onError("Non-200 Error !!");
        } else if (error.getKind() == RetrofitException.Kind.NETWORK) {
            getMvpView().onError("Internet Connectivity Error !!");
        } else {
            getMvpView().onError("Unknown Error Occurred !!");
        }
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        MvpViewNotAttachedException() {
            super("Please call Presenter.onAttach(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}
