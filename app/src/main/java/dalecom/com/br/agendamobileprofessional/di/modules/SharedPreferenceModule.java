package dalecom.com.br.agendamobileprofessional.di.modules;

import android.content.Context;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dalecom.com.br.agendamobileprofessional.wrappers.SharedPreference;

/**
 * Created by daniellessa on 11/23/15.
 */
@Module
public class SharedPreferenceModule {
    Context mContext;
    // Which objects do you want dagger to provide?


    public SharedPreferenceModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    @Singleton
    public SharedPreference provideExamManager() {
        return new SharedPreference(mContext);
    }
}
