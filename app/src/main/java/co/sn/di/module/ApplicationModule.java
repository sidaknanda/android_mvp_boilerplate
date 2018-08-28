package co.sn.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import co.sn.data.DataManager;
import co.sn.data.DataManagerImpl;
import co.sn.data.network.ApiHelper;
import co.sn.data.network.ApiHelperImpl;
import co.sn.data.prefs.PreferencesHelper;
import co.sn.data.prefs.PreferencesHelperImpl;
import co.sn.di.ApplicationContext;
import co.sn.di.PreferenceInfo;
import dagger.Module;
import dagger.Provides;

import static co.sn.data.prefs.PreferencesHelperImpl.PREF_NAME;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

//    @Provides
//    @DatabaseInfo
//    String provideDatabaseName() {
//        return AppConstants.DB_NAME;
//    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return PREF_NAME;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(DataManagerImpl appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(PreferencesHelperImpl appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(ApiHelperImpl appApiHelper) {
        return appApiHelper;
    }

//    @Provides
//    @Singleton
//    DbHelper provideDbHelper(DbHelperImpl appDbHelper) {
//        return appDbHelper;
//    }
}
