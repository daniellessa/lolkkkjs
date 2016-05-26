package dalecom.com.br.agendamobileprofessional.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;

import dalecom.com.br.agendamobileprofessional.AgendaMobileApplication;
import dalecom.com.br.agendamobileprofessional.R;
import dalecom.com.br.agendamobileprofessional.helpers.DateHelper;
import dalecom.com.br.agendamobileprofessional.model.Event;
import dalecom.com.br.agendamobileprofessional.model.Times;
import dalecom.com.br.agendamobileprofessional.service.rest.RestClient;
import dalecom.com.br.agendamobileprofessional.utils.CalendarTimes;
import dalecom.com.br.agendamobileprofessional.utils.EventManager;
import dalecom.com.br.agendamobileprofessional.utils.LogUtils;
import dalecom.com.br.agendamobileprofessional.utils.S;
import dalecom.com.br.agendamobileprofessional.wrappers.SharedPreference;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by daniellessa on 23/05/16.
 */
public class TimesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Times> mList;
    private List<Event> mListEvents;
    private Calendar startAt, endstAt;
    private Context mContext;
    private ImageLoader imageLoader;
    private Calendar dateSelected;

    @Inject
    RestClient restClient;

    @Inject
    EventManager eventManager;

    @Inject
    SharedPreference sharedPreference;


    public TimesAdapter(Context mContext, List list, List<Event> listEvents, Calendar date){
        this.mContext = mContext;
        ((AgendaMobileApplication) mContext.getApplicationContext()).getAppComponent().inject(this);
        this.mList = list;
        this.mListEvents = listEvents;
        this.dateSelected = date;
    }



    public class VHBusy extends RecyclerView.ViewHolder {

        protected TextView startView, endsView, serviceView, userName;

        public VHBusy(View itemView) {
            super(itemView);
            startView = (TextView) itemView.findViewById(R.id.start_hour);
            endsView = (TextView) itemView.findViewById(R.id.end_hour);
            serviceView = (TextView) itemView.findViewById(R.id.service_name);
            userName = (TextView) itemView.findViewById(R.id.user_name);
        }
    }

    class VHBusyMy extends RecyclerView.ViewHolder {

        protected TextView startView, endsView, serviceView, userName;

        public VHBusyMy(View itemView) {
            super(itemView);
            startView = (TextView) itemView.findViewById(R.id.start_hour);
            endsView = (TextView) itemView.findViewById(R.id.end_hour);
            serviceView = (TextView) itemView.findViewById(R.id.service_name);
            userName = (TextView) itemView.findViewById(R.id.user_name);
        }
    }

    public class VHFree extends RecyclerView.ViewHolder {

        protected TextView startView, endsView, serviceView, userName;

        public VHFree(View itemView) {
            super(itemView);

            startView = (TextView) itemView.findViewById(R.id.start_hour);
            endsView = (TextView) itemView.findViewById(R.id.end_hour);
            serviceView = (TextView) itemView.findViewById(R.id.service_name);
            userName = (TextView) itemView.findViewById(R.id.user_name);
        }
    }

    public class VHLunch extends RecyclerView.ViewHolder {

        protected TextView startView, endsView, serviceView, userName;

        public VHLunch(View itemView) {
            super(itemView);
            startView = (TextView) itemView.findViewById(R.id.start_hour);
            endsView = (TextView) itemView.findViewById(R.id.end_hour);
            serviceView = (TextView) itemView.findViewById(R.id.service_name);
            userName = (TextView) itemView.findViewById(R.id.user_name);
        }
    }

    public class VHInvalidHour extends RecyclerView.ViewHolder {

        protected TextView startView, endsView, serviceView, userName;

        public VHInvalidHour(View itemView) {
            super(itemView);
            startView = (TextView) itemView.findViewById(R.id.start_hour);
            endsView = (TextView) itemView.findViewById(R.id.end_hour);
            serviceView = (TextView) itemView.findViewById(R.id.service_name);
            userName = (TextView) itemView.findViewById(R.id.user_name);
        }
    }

    public class VHBloqued extends RecyclerView.ViewHolder {

        protected TextView startView, endsView, serviceView, userName;

        public VHBloqued(View itemView) {
            super(itemView);
            startView = (TextView) itemView.findViewById(R.id.start_hour);
            endsView = (TextView) itemView.findViewById(R.id.end_hour);
            serviceView = (TextView) itemView.findViewById(R.id.service_name);
            userName = (TextView) itemView.findViewById(R.id.user_name);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == S.TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_times, parent, false);
            return new VHBusy(view);
        }
        else if(viewType == S.TYPE_ITEM_MY){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_times, parent, false);
            return new VHBusyMy(view);
        }
        else if(viewType == S.TYPE_ITEM){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_times, parent, false);
            return new VHFree(view);
        }
        else if(viewType == S.TYPE_LUNCH){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_times, parent, false);
            return new VHLunch(view);
        }
        else if(viewType == S.TYPE_INVALID_HOUR){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_times, parent, false);
            return new VHInvalidHour(view);
        }
        else if(viewType == S.TYPE_ITEM_BLOQUED){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_times, parent, false);
            return new VHBloqued(view);
        }


        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder,final int position) {

        if(holder instanceof VHBusyMy){
            ((VHBusyMy) holder).startView.setText(DateHelper.hourToString(mList.get(position).getStartAt()));
            ((VHBusyMy) holder).endsView.setText(DateHelper.hourToString(mList.get(position).getEndsAt()));
            ((VHBusyMy) holder).serviceView.setText(mList.get(position).getEvent().getService().getTitle());
            ((VHBusyMy) holder).userName.setText(mList.get(position).getEvent().getUser().getName());
        }
        else if(holder instanceof VHBusy){
            ((VHBusy) holder).startView.setText(DateHelper.hourToString(mList.get(position).getStartAt()));
            ((VHBusy) holder).endsView.setText(DateHelper.hourToString(mList.get(position).getEndsAt()));

            ((VHBusy) holder).userName.setText("Ocupado");
        }
        else if(holder instanceof VHFree){
            ((VHFree) holder).startView.setText(DateHelper.hourToString(mList.get(position).getStartAt()));
            ((VHFree) holder).endsView.setText(DateHelper.hourToString(mList.get(position).getEndsAt()));
            ((VHFree) holder).userName.setText("Disponível");
        }

        else if(holder instanceof VHLunch){
            ((VHLunch) holder).startView.setText(DateHelper.hourToString(mList.get(position).getStartAt()));
            Calendar ends = DateHelper.copyDate(mList.get(position).getEndsAt());
            ends.add(Calendar.MINUTE, 1);
            ((VHLunch) holder).endsView.setText(DateHelper.hourToString(ends));

            ((VHLunch) holder).userName.setText("Almoço");
        }

        else if(holder instanceof VHInvalidHour){
            ((VHInvalidHour) holder).startView.setText(DateHelper.hourToString(mList.get(position).getStartAt()));
            Calendar ends = DateHelper.copyDate(mList.get(position).getEndsAt());
            ends.add(Calendar.MINUTE, 1);
            ((VHInvalidHour) holder).endsView.setText(DateHelper.hourToString(ends));

            ((VHInvalidHour) holder).userName.setText("Vencido");
        }

        else if(holder instanceof VHBloqued){
            ((VHBloqued) holder).startView.setText(DateHelper.hourToString(mList.get(position).getStartAt()));
            ((VHBloqued) holder).endsView.setText(DateHelper.hourToString(mList.get(position).getEndsAt()));

            ((VHBloqued) holder).userName.setText("Bloqueado");
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getViewType();
    }


    public void swap(List<Times> times){
        mList.clear();
        mList.addAll(times);
        notifyDataSetChanged();
    }

    private void goToEventActivity(Event event){

        event.setUserProf(eventManager.getCurrentUserProfessional());
        eventManager.setCurrentEvent(event);
        //Intent it = new Intent(mContext, EventActivity.class);
        //mContext.startActivity(it);

    }

    private void createEvent(Times time){

        CalendarTimes calendarTimes = new CalendarTimes(mContext, mList);

        Log.d(LogUtils.TAG, "Date selected: " + time.getStartAt() + " " + time.getEndsAt());
        if (calendarTimes.checkDisponible(mContext, time)) {

            startAt = DateHelper.copyDate(dateSelected);
            startAt.set(Calendar.HOUR_OF_DAY, time.getStartAt().get(Calendar.HOUR_OF_DAY));
            startAt.set(Calendar.MINUTE, time.getStartAt().get(Calendar.MINUTE));

            endstAt = DateHelper.copyDate(startAt);
            endstAt.add(Calendar.HOUR_OF_DAY, eventManager.getCurrentService().getHours());
            endstAt.add(Calendar.MINUTE, eventManager.getCurrentService().getMinutes());

            eventManager.setCurrentStartAt(DateHelper.copyDate(startAt));
            eventManager.setCurrentEndsAt(DateHelper.copyDate(endstAt));
            eventManager.finalizeEvent();

            Log.d(LogUtils.TAG, "Start date: " + DateHelper.convertDateToStringSql(startAt));
            Log.d(LogUtils.TAG, "Ends date: " + DateHelper.convertDateToStringSql(endstAt));

            //initConfirmDialog(startAt, endstAt);
        }
    }


    private Callback callbackPostEvents = new Callback<JsonObject>(){

        @Override
        public void success(JsonObject jsonObject, Response response) {
            Log.d(LogUtils.TAG,"Success postEvent: "+ response.getStatus());
//            updateRecyclerView(dateSelected);
//            initDialog(startAt, endstAt);

            restClient.notifyNewEvent(eventManager.getCurrentUserProfessional().getIdServer(), new Callback<JsonObject>() {

                @Override
                public void success(JsonObject jsonObject, Response response) {
                    Log.d(LogUtils.TAG, "notificationEvent: SUCESS");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(LogUtils.TAG, "notificationEvent: FAIL: "+ error);
                }
            });
        }

        @Override
        public void failure(RetrofitError error) {
            Log.d(LogUtils.TAG,"Erro postEvent: "+ error);
        }
    };

}