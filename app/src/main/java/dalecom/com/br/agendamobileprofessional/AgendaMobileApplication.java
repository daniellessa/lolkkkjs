package dalecom.com.br.agendamobileprofessional;

import android.content.Context;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import dalecom.com.br.agendamobileprofessional.di.components.AppComponent;
import dalecom.com.br.agendamobileprofessional.di.components.DaggerAppComponent;
import dalecom.com.br.agendamobileprofessional.di.modules.AwsModule;
import dalecom.com.br.agendamobileprofessional.di.modules.FileModule;
import dalecom.com.br.agendamobileprofessional.di.modules.ManagerModule;
import dalecom.com.br.agendamobileprofessional.di.modules.RestClientModule;
import dalecom.com.br.agendamobileprofessional.di.modules.SharedPreferenceModule;

/**
 * Created by daniellessa on 19/05/16.
 */
public class AgendaMobileApplication extends com.activeandroid.app.Application{

    AppComponent appComponent;
    public static Context mGlobalContext;


    public void onCreate() {
        super.onCreate();
        mGlobalContext = getApplicationContext();

        appComponent =  DaggerAppComponent.builder().
                awsModule(new AwsModule(this)).
                restClientModule(new RestClientModule(this)).
                managerModule(new ManagerModule(this)).
                fileModule(new FileModule(this)).
                sharedPreferenceModule(new SharedPreferenceModule(this))
                .build();

        initImageLoader(getApplicationContext());
    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);

        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
