package dalecom.com.br.agendamobileprofessional.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

import dalecom.com.br.agendamobileprofessional.AgendaMobileApplication;
import dalecom.com.br.agendamobileprofessional.R;
import dalecom.com.br.agendamobileprofessional.adapters.TimesAdapter;
import dalecom.com.br.agendamobileprofessional.helpers.DateHelper;
import dalecom.com.br.agendamobileprofessional.model.Event;
import dalecom.com.br.agendamobileprofessional.model.Professional;
import dalecom.com.br.agendamobileprofessional.model.Times;
import dalecom.com.br.agendamobileprofessional.model.User;
import dalecom.com.br.agendamobileprofessional.service.rest.RestClient;
import dalecom.com.br.agendamobileprofessional.utils.CalendarTimes;
import dalecom.com.br.agendamobileprofessional.utils.EventManager;
import dalecom.com.br.agendamobileprofessional.utils.EventParser;
import dalecom.com.br.agendamobileprofessional.utils.LogUtils;
import dalecom.com.br.agendamobileprofessional.utils.OneProfessionalParser;
import dalecom.com.br.agendamobileprofessional.wrappers.SharedPreference;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarLayout appBarLayout;
    private MaterialCalendarView calendarView;
    private TextView weekDay, numberDay, monthDay;
    private Toolbar toolbar;
    private Calendar dateSelected = Calendar.getInstance();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TimesAdapter adapter;
    private List<Times> mList;
    private  CalendarTimes calendarTimes;
    private List<Event> mEvents = new ArrayList<>();

    @Inject
    public EventManager eventManager;

    @Inject
    public RestClient restClient;

    @Inject
    SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AgendaMobileApplication) getApplicationContext()).getAppComponent().inject(this);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setFindByIds();
        populateProfessionalFromServer();


    }

    private void populateProfessionalFromServer(){
        restClient.getProfessionalForId(sharedPreference.getCurrentUser().getIdServer(), new Callback<JsonObject>() {

            @Override
            public void success(JsonObject jsonObject, Response response) {

                OneProfessionalParser parser = new OneProfessionalParser(HomeActivity.this, jsonObject);
                User professional = parser.parseFullProfessional();
                eventManager.setCurrentProfessional(professional.getProfessional());

                populateEventsList();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LogUtils.TAG, "RetrofitError populateProfessionalFromServer: " + error);
            }
        });
    }

    private void setOnclickListeners(){

        calendarView.setDateSelected(dateSelected, true);
        setCurrentDayView();

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                date.copyTo(dateSelected);
                populateEventsList();
                setCurrentDayView();
                appBarLayout.setExpanded(false);

            }
        });
    }

    private void setRecyclerView(){

        calendarTimes = new CalendarTimes(this, mEvents, dateSelected);
        mList = calendarTimes.construct();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_calendar);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new TimesAdapter(this,mList,mEvents,dateSelected);
        mRecyclerView.invalidate();
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);

    }

    private void populateEventsList(){
        if(eventManager.getCurrentProfessional() != null) {
            mEvents.clear();
            restClient.getEvents(eventManager.getCurrentProfessional().getIdServer(), DateHelper.toStringSql(dateSelected), callbackEvents);
        }
    }


    private Callback callbackEvents = new Callback<JsonArray>(){

        @Override
        public void success(JsonArray jsonArray, Response response) {
            EventParser eventParser = new EventParser(jsonArray);
            mEvents = eventParser.parseFullEvents();
            setRecyclerView();
            //mRecyclerView.setVisibility(View.VISIBLE);
        }

        @Override
        public void failure(RetrofitError error) {
            mEvents = new ArrayList<>();
            setRecyclerView();
            //mRecyclerView.setVisibility(View.VISIBLE);
        }
    };

    private void setCurrentDayView(){
        weekDay.setText(DateHelper.getWeekDay(dateSelected));
        numberDay.setText(DateHelper.getDay(dateSelected));
        monthDay.setText(DateHelper.getMonth(dateSelected));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setFindByIds(){

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendar_view);
        weekDay = (TextView) findViewById(R.id.week_day);
        numberDay = (TextView) findViewById(R.id.number_day);
        monthDay = (TextView) findViewById(R.id.month_day);


        setOnclickListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.calendar_icon:
                if(appBarLayout.getTop() < 0){
                    appBarLayout.setExpanded(true);
                }else{
                    appBarLayout.setExpanded(false);
                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

            sharedPreference.clearUserToken();
            Intent it = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(it);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
