package dalecom.com.br.agendamobileprofessional.di.modules;

import android.content.Context;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dalecom.com.br.agendamobileprofessional.utils.EventManager;


@Module
public class ManagerModule {
    Context mContext;

    public ManagerModule(Context mContext) {
        this.mContext = mContext;
    }

    // Which objects do you want dagger to provide?

    @Provides @Singleton
    public EventManager provideEventManager() {
        return new EventManager(mContext);
    }
}
