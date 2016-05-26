package dalecom.com.br.agendamobileprofessional.utils;

import android.content.Context;



import java.util.Calendar;
import javax.inject.Inject;
import javax.inject.Singleton;

import dalecom.com.br.agendamobileprofessional.AgendaMobileApplication;
import dalecom.com.br.agendamobileprofessional.helpers.DateHelper;
import dalecom.com.br.agendamobileprofessional.model.Event;
import dalecom.com.br.agendamobileprofessional.model.Professional;
import dalecom.com.br.agendamobileprofessional.model.Property;
import dalecom.com.br.agendamobileprofessional.model.Service;
import dalecom.com.br.agendamobileprofessional.model.User;
import dalecom.com.br.agendamobileprofessional.wrappers.SharedPreference;


/**
 * Created by daniellessa on 10/19/15.
 */
@Singleton
public class EventManager {
    Context mContext;
    private Event mEvent;
    private User currentUser;
    private User currentUserProf;
    private Professional currentProfessional;
    private Calendar dateSelected;
    private Service currentService;
    private String currentDay;
    private Calendar currentStartAt;
    private Calendar currentEndsAt;
    private Property currentProperty;
    private Event CurrentEvent;

    @Inject public static SharedPreference sharedPreference;

    @Inject
    public EventManager(Context mContext) {
        ( (AgendaMobileApplication) mContext).getAppComponent().inject(this);
        this.mContext = mContext;
    }


    public void startNewEvent(Calendar date) {
        this.mEvent = new Event();
        this.dateSelected = date;
        this.currentDay = DateHelper.toStringSql(date);
        this.mEvent.setStartAt(date);
        setEventCurrentUser();
    }

    public void setEventCurrentUser() {
            User user = sharedPreference.getCurrentUser();
            this.currentUser = user;
            this.mEvent.setUser(user);
    }

    public void setUserProfIntoEvent(User user) {
        this.currentUserProf = user;
        this.currentProfessional = user.getProfessional();
    }

    public User getCurrentUserProfessional(){
        return currentUserProf;
    }

    public Professional getCurrentProfessional() {
        return currentProfessional;
    }

    public void setCurrentProfessional(Professional currentProfessional) {
        this.currentProfessional = currentProfessional;
    }

    public void setServiceIntoEvent(Service service) {
        this.currentService = service;
    }

    public Service getCurrentService(){
        return this.currentService;
    }

    public Calendar getDateSelected() {
        return dateSelected;
    }

    public Calendar getCurrentStartAt() {
        return currentStartAt;
    }

    public void setCurrentStartAt(Calendar currentStartAt) {
        this.currentStartAt = currentStartAt;
    }

    public Calendar getCurrentEndsAt() {
        return currentEndsAt;
    }

    public void setCurrentEndsAt(Calendar currentEndsAt) {
        this.currentEndsAt = currentEndsAt;
    }

    public Event getEvent() {
        return mEvent;
    }

    public void setDateSelected(Calendar dateSelected) {
        this.dateSelected = dateSelected;
    }

    public Property getCurrentProperty() {
        return currentProperty;
    }

    public void setCurrentProperty(Property currentProperty) {
        this.currentProperty = currentProperty;
    }

    public Event getCurrentEvent() {
        return CurrentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        CurrentEvent = currentEvent;
    }

    public void finalizeEvent() {

//
        this.mEvent.setUserId(currentUser.getIdServer());
        this.mEvent.setProfessionalsId(currentUserProf.getProfessional().getIdServer());
        this.mEvent.setServicesId(currentService.getIdServer());
        this.mEvent.setDay(DateHelper.toStringSql(dateSelected));
        this.mEvent.setStartAt(currentStartAt);
        this.mEvent.setEndsAt(currentEndsAt);
        this.mEvent.setStatus(S.STATUS_PENDING);
        this.mEvent.setFinalized(false);

        this.mEvent.setProfessinal(currentProfessional);
        this.mEvent.setService(currentService);
        this.mEvent.setProperty(currentProperty);

        //this.mEvent.save();

    }

    public User getUserProf() {
        if ( this.mEvent != null )
            return this.mEvent.getUserProf();

        return null;
    }

    public void removeService(Service service) {
        if ( this.mEvent != null )
        {
            this.mEvent.setService(null);
        }
    }

    public void clear(){
        this.mEvent.setUser(null);
        this.mEvent.setUserProf(null);
        this.mEvent.setProfessinal(null);
        this.mEvent.setService(null);
        this.mEvent.setStartAt(null);
        this.mEvent.setEndsAt(null);
        this.mEvent.setStatus(null);
        this.mEvent.setFinalized(false);
    }

}
