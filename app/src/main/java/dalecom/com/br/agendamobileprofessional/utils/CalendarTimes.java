package dalecom.com.br.agendamobileprofessional.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import dalecom.com.br.agendamobileprofessional.AgendaMobileApplication;
import dalecom.com.br.agendamobileprofessional.helpers.DateHelper;
import dalecom.com.br.agendamobileprofessional.model.Event;
import dalecom.com.br.agendamobileprofessional.model.Professional;
import dalecom.com.br.agendamobileprofessional.model.Service;
import dalecom.com.br.agendamobileprofessional.model.Times;
import dalecom.com.br.agendamobileprofessional.service.rest.RestClient;
import dalecom.com.br.agendamobileprofessional.wrappers.SharedPreference;

/**
 * Created by daniellessa on 03/04/16.
 */
public class CalendarTimes {

    private Calendar startAt, endsAt, split, interval, startLunch, endsLunch, dateSelected;
    private Professional userProf;
    private List<Event> mEvents;
    List<Times> list;
    private Context mContext;
    private Service service;
    private Calendar today;

    @Inject EventManager eventManager;

    @Inject
    RestClient restClient;

    @Inject
    SharedPreference sharedPreference;



    public CalendarTimes(Context mContext, List<Event> listEvents, Calendar dateSelected) {
        mEvents = listEvents;
        this.mContext = mContext;
        this.dateSelected = dateSelected;
        ((AgendaMobileApplication) mContext.getApplicationContext()).getAppComponent().inject(this);
        populateVariables();
    }

    public CalendarTimes(Context context, List<Times> listTimes){
        this.mContext = context;
        ((AgendaMobileApplication) mContext.getApplicationContext()).getAppComponent().inject(this);
        list = listTimes;
        populateVariables();
    }

    private void populateVariables(){
        userProf = eventManager.getCurrentProfessional();
        startAt = copyDate(userProf.getStartAt());
        endsAt = copyDate(userProf.getEndsAt());
        split = copyDate(userProf.getSplit());
        interval = copyDate(userProf.getInterval());
        startLunch = copyDate(userProf.getStartLaunchAt());
        endsLunch = copyDate(userProf.getEndsLaunchAt());
        service = eventManager.getCurrentService();
        DateHelper.makeIgualsDate(startAt, dateSelected);
        DateHelper.makeIgualsDate(endsAt, dateSelected);
        DateHelper.makeIgualsDate(startLunch, dateSelected);
        DateHelper.makeIgualsDate(endsLunch, dateSelected);
        today = Calendar.getInstance();
    }


    public List<Times> construct(){


        list = new ArrayList<>();
        list.clear();

        Calendar auxStart = copyDate(startAt);
        Calendar auxTarget = copyDate(startAt);

        int next = 1;


        while (startAt.getTimeInMillis() < endsAt.getTimeInMillis()){

            boolean find = false;

            if(DateHelper.hourToString(startAt).equals(DateHelper.hourToString(startLunch))) {

                int timeLunch = 0;

                while (startLunch.getTimeInMillis() < endsLunch.getTimeInMillis()){
                    timeLunch++;
                    startLunch.add(Calendar.MINUTE, next);
                }

                //timeLunch -= 1;

                Calendar auxEnd = copyDate(startAt);
                auxEnd.add(Calendar.MINUTE, timeLunch -1);

                list.add(populateLunch(startAt, auxEnd));

                auxEnd.add(Calendar.MINUTE, 1);

                startAt = copyDate(auxEnd);
                auxTarget = copyDate(startAt);
                auxTarget.add(Calendar.HOUR_OF_DAY, split.get(Calendar.HOUR_OF_DAY));
                auxTarget.add(Calendar.MINUTE, split.get(Calendar.MINUTE));
                auxStart = copyDate(startAt);

            }

            for (Event event: mEvents){

                if(DateHelper.hourToString(startAt).equals(DateHelper.hourToString(event.getStartAt()))){



                    Calendar auxEnd = copyDate(startAt);
                    auxEnd.add(Calendar.HOUR_OF_DAY, event.getService().getHours() + interval.get(Calendar.HOUR_OF_DAY));
                    auxEnd.add(Calendar.MINUTE, event.getService().getMinutes() + interval.get(Calendar.MINUTE));

                    Log.d(LogUtils.TAG, "idServer: " + event.getUser().getIdServer() + " => " + sharedPreference.getCurrentUser().getIdServer());

                    if(event.getUser().getIdServer() == sharedPreference.getCurrentUser().getIdServer())
                        list.add(populateMy(startAt, auxEnd, event.getUser().getName(), event));
                    else
                        list.add(populateTime(startAt, auxEnd, event.getUser().getName()));


                    startAt = copyDate(auxEnd);
                    auxTarget = copyDate(startAt);
                    auxTarget.add(Calendar.HOUR_OF_DAY, split.get(Calendar.HOUR_OF_DAY));
                    auxTarget.add(Calendar.MINUTE, split.get(Calendar.MINUTE));
                    auxStart = copyDate(startAt);

                    Log.d(LogUtils.TAG, "FINDED: " + DateHelper.hourToString(startAt) + " => " + DateHelper.hourToString(auxTarget));
                }

            }


            if(startAt.getTimeInMillis() <  today.getTimeInMillis()) {


                Calendar auxEnd = copyDate(startAt);
                auxEnd.add(Calendar.HOUR_OF_DAY, split.get(Calendar.HOUR_OF_DAY));
                auxEnd.add(Calendar.MINUTE, split.get(Calendar.MINUTE) -1);

                list.add(populateInvalidHour(startAt, auxEnd));

                startAt = copyDate(auxEnd);
                auxTarget = copyDate(startAt);
                auxTarget.add(Calendar.HOUR_OF_DAY, split.get(Calendar.HOUR_OF_DAY));
                auxTarget.add(Calendar.MINUTE, split.get(Calendar.MINUTE));
                auxStart = copyDate(startAt);

            }

            if(!find){
                if(DateHelper.hourToString(startAt).equals(DateHelper.hourToString(auxTarget))){
                    list.add(createFreeTime(auxStart, auxTarget));
                    auxTarget.add(Calendar.HOUR_OF_DAY, split.get(Calendar.HOUR_OF_DAY));
                    auxTarget.add(Calendar.MINUTE, split.get(Calendar.MINUTE));
                    auxStart = copyDate(auxTarget);
                }else{
                    startAt.add(Calendar.MINUTE, next);
                }
            }

        }

        return list;
    }

    private Times populateTime(Calendar startAt, Calendar ends, String name){
        Times time = new Times();
        time.setViewType(S.TYPE_HEADER);
        time.setStartAt(DateHelper.copyDate(startAt));
        time.setEndsAt(DateHelper.copyDate(ends));
        time.setUserName(name);
        time.setFree(false);

        return time;
    }

    private Times createFreeTime(Calendar startAt, Calendar endsAt){

        Times time = new Times();
        time.setViewType(S.TYPE_ITEM);
        time.setStartAt(DateHelper.copyDate(startAt));
        time.setEndsAt(DateHelper.copyDate(endsAt));
        //time.setUserName(mContext.getResources().getString(R.string.available));
        time.setFree(true);


        return time;
    }

    private Times populateLunch(Calendar startAt, Calendar ends){
        Times time = new Times();
        time.setViewType(S.TYPE_LUNCH);
        time.setStartAt(DateHelper.copyDate(startAt));
        time.setEndsAt(DateHelper.copyDate(ends));
        //time.setUserName(mContext.getResources().getString(R.string.lunch));
        time.setFree(false);

        return time;
    }

    private Times populateInvalidHour(Calendar startAt, Calendar ends){
        Times time = new Times();
        time.setViewType(S.TYPE_INVALID_HOUR);
        time.setStartAt(DateHelper.copyDate(startAt));
        time.setEndsAt(DateHelper.copyDate(ends));
        //time.setUserName(mContext.getResources().getString(R.string.invalid_hour));
        time.setFree(false);

        return time;
    }

    private Times populateBloqued(Calendar startAt, Calendar ends){
        Times time = new Times();
        time.setViewType(S.TYPE_ITEM_BLOQUED);
        time.setStartAt(DateHelper.copyDate(startAt));
        time.setEndsAt(DateHelper.copyDate(ends));
        //time.setUserName(mContext.getResources().getString(R.string.bloqued));
        time.setFree(false);

        return time;
    }

    private Times populateMy(Calendar startAt, Calendar ends, String name, Event event){
        Times time = new Times();
        time.setViewType(S.TYPE_ITEM_MY);
        time.setStartAt(DateHelper.copyDate(startAt));
        time.setEndsAt(DateHelper.copyDate(ends));
        time.setUserName(name);
        time.setFree(false);
        time.setEvent(event);

        return time;
    }



    public boolean checkDisponible(Context context, Times selectedTime){

        if(selectedTime.isFree()){
            for (int i=0; i < list.size(); i++){
                if(list.get(i).getStartAt() == selectedTime.getStartAt()){
                    int duration = (service.getHours()*60) + service.getMinutes();
                    int splitMinutes = (split.get(Calendar.HOUR_OF_DAY)*60) + split.get(Calendar.MINUTE);
                    int checkNextIndex = (int) Math.round(((double)duration / splitMinutes)+0.5d);//duration/splitMinutes;

                    for (int x=0; x < checkNextIndex; x++){
                        try {
                            if(!list.get(i+x).isFree()){
                                Toast.makeText(context,"Tempo insuficente para este serviço",Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }catch (IndexOutOfBoundsException e){
                            Toast.makeText(context,"Tempo insuficente para este serviço",Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }else{
            Toast.makeText(context,"Este horário não está disponível",Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private Calendar copyDate(Calendar date){
        Calendar newDate = Calendar.getInstance();
        newDate.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH),date.get(Calendar.HOUR_OF_DAY),date.get(Calendar.MINUTE),0);
        return newDate;
    }



}
