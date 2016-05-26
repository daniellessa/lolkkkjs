package dalecom.com.br.agendamobileprofessional.di.components;

import javax.inject.Singleton;
import dagger.Component;
import dalecom.com.br.agendamobileprofessional.adapters.TimesAdapter;
import dalecom.com.br.agendamobileprofessional.di.modules.AwsModule;
import dalecom.com.br.agendamobileprofessional.di.modules.FileModule;
import dalecom.com.br.agendamobileprofessional.di.modules.ManagerModule;
import dalecom.com.br.agendamobileprofessional.di.modules.RestClientModule;
import dalecom.com.br.agendamobileprofessional.di.modules.SharedPreferenceModule;
import dalecom.com.br.agendamobileprofessional.service.gcm.RegistrationIntentService;
import dalecom.com.br.agendamobileprofessional.service.rest.RestClient;
import dalecom.com.br.agendamobileprofessional.service.sync.SyncAdapter;
import dalecom.com.br.agendamobileprofessional.ui.HomeActivity;
import dalecom.com.br.agendamobileprofessional.ui.LoginActivity;
import dalecom.com.br.agendamobileprofessional.utils.CalendarTimes;
import dalecom.com.br.agendamobileprofessional.utils.EventManager;
import dalecom.com.br.agendamobileprofessional.utils.FileUtils;
import dalecom.com.br.agendamobileprofessional.utils.OneProfessionalParser;
import dalecom.com.br.agendamobileprofessional.utils.ProfessionalParser;
import dalecom.com.br.agendamobileprofessional.wrappers.MixPanel;
import dalecom.com.br.agendamobileprofessional.wrappers.S3;


@Singleton
@Component(modules = {AwsModule.class, ManagerModule.class, SharedPreferenceModule.class, RestClientModule.class, FileModule.class})
public interface AppComponent {
    //Where do you want dagger to provide this object?
    void inject(SyncAdapter syncAdapter);
    void inject(EventManager eventManager);
    void inject(FileUtils fileUtils);
    void inject(RestClient restClient);
    void inject(S3 s3);
    void inject(MixPanel mixPanel);
    void inject(RegistrationIntentService registrationIntentService);
    void inject(CalendarTimes calendarTimes);
    void inject(ProfessionalParser professionalParser);
    void inject(OneProfessionalParser oneProfessionalParser);
    void inject(TimesAdapter timesAdapter);
    void inject(HomeActivity homeActivity);
    void inject(LoginActivity loginActivity);

}
