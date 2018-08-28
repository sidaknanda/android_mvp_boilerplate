package co.sn.di.component;

import co.sn.di.PerActivity;
import co.sn.di.module.ServiceModule;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {
}
