package dalecom.com.br.agendamobileprofessional.di.modules;

import android.content.Context;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dalecom.com.br.agendamobileprofessional.service.rest.RestClient;

/**
 * Created by viniciuslima on 11/24/15.
 */
@Module
public class RestClientModule {
    Context mContext;
    // Which objects do you want dagger to provide?

    public RestClientModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    @Singleton
    public RestClient provideRestClient() {
        return new RestClient(mContext);
    }
}
