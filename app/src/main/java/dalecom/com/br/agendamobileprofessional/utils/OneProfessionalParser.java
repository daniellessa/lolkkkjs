package dalecom.com.br.agendamobileprofessional.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dalecom.com.br.agendamobileprofessional.AgendaMobileApplication;
import dalecom.com.br.agendamobileprofessional.model.Professional;
import dalecom.com.br.agendamobileprofessional.model.User;
import dalecom.com.br.agendamobileprofessional.wrappers.S3;


public class OneProfessionalParser {

    private JsonObject jsonObject;
    private Context mContext;
    private User user;
    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    @Inject public S3 s3;

    public OneProfessionalParser(Context context, JsonObject jsonObject) {
        mContext = context;
        ((AgendaMobileApplication) mContext.getApplicationContext()).getAppComponent().inject(this);
        this.jsonObject = jsonObject;
    }

    public User parseFullProfessional() {

            user = new User();
            JsonObject data = jsonObject;


            try {
                user.setIdServer(data.getAsJsonObject("users").get("id").getAsInt());
                user.setEmail(data.getAsJsonObject("users").get("email").getAsString());
                user.setName(data.getAsJsonObject("users").get("name").getAsString());
                user.setSex(data.getAsJsonObject("users").get("sex").getAsString());

                if(!data.getAsJsonObject("users").get("registration_id").isJsonNull())
                    user.setRegistrationId(data.getAsJsonObject("users").get("registration_id").getAsString());

                if ( !data.getAsJsonObject("users").get("bucket_name").isJsonNull() )
                    user.setBucketPath(data.getAsJsonObject("users").get("bucket_name").getAsString());

                if ( !data.getAsJsonObject("users").get("photo_path").isJsonNull() )
                    user.setPhotoPath(data.getAsJsonObject("users").get("photo_path").getAsString());

                Professional professional = new Professional();

                professional.setIdServer(data.get("id").getAsInt());
                professional.setProperties(data.getAsJsonObject("properties").get("id").getAsInt());
                professional.setCategory(data.getAsJsonObject("professions").get("id").getAsInt());
                professional.setProfessionName(data.getAsJsonObject("professions").get("name").getAsString());

                Calendar startAt = Calendar.getInstance();
                Date dateStart = format.parse(data.get("startAt").getAsString());
                startAt.set(Calendar.HOUR_OF_DAY, dateStart.getHours());
                startAt.set(Calendar.MINUTE, dateStart.getMinutes());
                startAt.set(Calendar.SECOND, 0);
                professional.setStartAt(startAt);


                Calendar endsAt = Calendar.getInstance();
                Date dateEnds = format.parse(data.get("endsAt").getAsString());
                endsAt.set(Calendar.HOUR_OF_DAY, dateEnds.getHours());
                endsAt.set(Calendar.MINUTE, dateEnds.getMinutes());
                endsAt.set(Calendar.SECOND, 0);
                professional.setEndsAt(endsAt);

                Calendar split = Calendar.getInstance();
                Date dateSplit = format.parse(data.get("split").getAsString());
                split.set(Calendar.HOUR_OF_DAY, dateSplit.getHours());
                split.set(Calendar.MINUTE, dateSplit.getMinutes());
                split.set(Calendar.SECOND, 0);
                professional.setSplit(split);

                Calendar interval = Calendar.getInstance();
                Date dateInterval = format.parse(data.get("interval").getAsString());
                interval.set(Calendar.HOUR_OF_DAY, dateInterval.getHours());
                interval.set(Calendar.MINUTE, dateInterval.getMinutes());
                interval.set(Calendar.SECOND, 0);
                professional.setInterval(interval);

                Calendar startLunchAt = Calendar.getInstance();
                Date dateStartLunch = format.parse(data.get("startLunchAt").getAsString());
                startLunchAt.set(Calendar.HOUR_OF_DAY, dateStartLunch.getHours());
                startLunchAt.set(Calendar.MINUTE, dateStartLunch.getMinutes());
                startLunchAt.set(Calendar.SECOND, 0);
                professional.setStartLaunchAt(startLunchAt);

                Calendar endsLunchAt = Calendar.getInstance();
                Date dateEndsLunch = format.parse(data.get("endsLunchAt").getAsString());
                endsLunchAt.set(Calendar.HOUR_OF_DAY, dateEndsLunch.getHours());
                endsLunchAt.set(Calendar.MINUTE, dateEndsLunch.getMinutes());
                endsLunchAt.set(Calendar.SECOND, 0);
                professional.setEndsLaunchAt(endsLunchAt);

                professional.setWorkSunday(data.get("workSunday").getAsBoolean());
                professional.setWorkMonday(data.get("workMonday").getAsBoolean());
                professional.setWorkTuesday(data.get("workTuesday").getAsBoolean());
                professional.setWorkWednesday(data.get("workWednesday").getAsBoolean());
                professional.setWorkThursday(data.get("workThursday").getAsBoolean());
                professional.setWorkFriday(data.get("workFriday").getAsBoolean());
                professional.setWorkSaturday(data.get("workSaturday").getAsBoolean());
                professional.setViewType(1);
                user.setProfessional(professional);


            }
            catch (UnsupportedOperationException e)
            {
                Log.d(LogUtils.TAG, "Catch 1: " + e);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(LogUtils.TAG, "Catch 2: " + e);
            }

        return user;
    }



}
