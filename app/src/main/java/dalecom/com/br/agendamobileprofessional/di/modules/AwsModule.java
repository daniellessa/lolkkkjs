package dalecom.com.br.agendamobileprofessional.di.modules;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import dalecom.com.br.agendamobileprofessional.wrappers.S3;

/**
 * Created by daniellessa on 10/15/15.
 */
@Module
public class AwsModule {

    Context mContext;

    public AwsModule(Context mContextParam) {
        mContext = mContextParam;
    }

    // Which objects do you want dagger to provide?

    @Provides
    S3 provideS3() {
        return new S3(mContext);
    }
}
