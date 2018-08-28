package co.sn.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import co.sn.App;
import co.sn.data.DataManager;
import co.sn.di.ApplicationContext;
import co.sn.di.module.ApplicationModule;
import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(App app);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();
}
