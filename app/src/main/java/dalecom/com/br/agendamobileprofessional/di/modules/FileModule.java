package dalecom.com.br.agendamobileprofessional.di.modules;

import android.content.Context;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dalecom.com.br.agendamobileprofessional.utils.FileUtils;

/**
 * Created by daniellessa on 12/15/15.
 */
@Module
public class FileModule {
    Context mContext;

    public FileModule(Context mContext) {
        this.mContext = mContext;
    }

    // Which objects do you want dagger to provide?

    @Provides
    @Singleton
    public FileUtils provideFileUtils() {
        return new FileUtils(mContext);
    }
}
