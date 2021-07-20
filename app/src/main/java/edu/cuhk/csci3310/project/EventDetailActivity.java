package edu.cuhk.csci3310.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

public class EventDetailActivity extends AppCompatActivity {



    private Fragment MapsFragment;
    private Fragment ShowDetailFragment;
    private Fragment mapWebView;
    Button alarmButton;
    String alarmtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        alarmButton = findViewById(R.id.setAlarm);
        ShowDetailFragment = new ShowDetailFragment();

        mapWebView = new MapWebView();


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Bundle sendBundle = new Bundle();
        sendBundle.putString("location", extras.getString("location"));
        sendBundle.putString("remark",extras.getString("remark") );
        sendBundle.putString("cal_burn",extras.getString("cal_burn") );
        sendBundle.putString("description", extras.getString("description"));
        sendBundle.putString("period", extras.getString("period"));
        sendBundle.putString("web_link", extras.getString("web_link")); // here
        sendBundle.putString("start_time", extras.getString("start_time"));
        sendBundle.putString("end_time", extras.getString("end_time"));
        alarmtime = extras.getString("date") + " " + extras.getString("start_time");
        Log.d("EventDetail","Alarm Time " + alarmtime);
        //Log.d("web_link", extras.getString("web_link"));

        alarmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Calendar calendar = Calendar.getInstance();
                Date alarmDate;
                Date currentDate;

                try
                {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("d-M-yyyy hh:mm aa");
                    alarmDate = dateFormat.parse(alarmtime);
                    currentDate = new Date(System.currentTimeMillis());
                    calendar.setTime(alarmDate);

                    System.out.println("Click");
                    //if(alarmDate.after(currentDate)) {
                        startAlarm(calendar,extras.getString("description"),extras.getString("remark"));
                        Toast.makeText(EventDetailActivity.this,"Alarm Set!",Toast.LENGTH_SHORT).show();
                    //}else {
                       // Toast.makeText(EventDetailActivity.this,"You Must Set Alarm Time After Current Time!",Toast.LENGTH_LONG).show();
                    //}
                }catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        });

        ShowDetailFragment.setArguments(sendBundle);
        mapWebView.setArguments(sendBundle);

        transaction.replace(R.id.show_detail_fragment, ShowDetailFragment,"Fragment");
        transaction.replace(R.id.map_fragment, mapWebView,"mapsFragment");
        transaction.commit();
    }

    private void startAlarm(Calendar calendar,String description,String remark) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Project";
            String desc = "Project";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("project",name,importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        AlarmManager manger =  (AlarmManager) this.getSystemService(ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        myIntent = new Intent(this,AlarmReceiver.class);
        myIntent.putExtra("desc",description);
        myIntent.putExtra("remark",remark);
        myIntent.setAction("Project");

        pendingIntent = PendingIntent.getBroadcast(this,1,myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        manger.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
    }


}