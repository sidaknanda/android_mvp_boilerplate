package co.sn.di.component;

import co.sn.di.PerActivity;
import co.sn.di.module.ActivityModule;
import co.sn.ui.youtube.VideosActivity;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(VideosActivity videosActivity);
}