package dalecom.com.br.agendamobileprofessional.wrappers;

import android.content.Context;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

import javax.inject.Inject;

import dalecom.com.br.agendamobileprofessional.utils.S;


/**
 * Created by daniellessa on 12/11/15.
 */
public class MixPanel {

    private static MixpanelAPI mixpanel;
    @Inject
    public static SharedPreference sharedPreference;


    static public MixpanelAPI getMixPanelInstance(Context context) {

        if ( mixpanel == null )
        {
            mixpanel = MixpanelAPI.getInstance(context, S.MixPanelProjectToken);
        }

        return mixpanel;
    }

    static public void trackEvent(Context context,String event,JSONObject props) {
        mixpanel = getMixPanelInstance(context);
        mixpanel.track(event, props);
    }

    static public void setUser(Context context,JSONObject usersProps) {
        mixpanel = getMixPanelInstance(context);
        mixpanel.getPeople().identify( mixpanel.getDistinctId() );
        mixpanel.getPeople().set(usersProps);
    }

    static public void trackEvent(Context context,String event) {
        mixpanel = getMixPanelInstance(context);
        mixpanel.track(event);
    }
}
