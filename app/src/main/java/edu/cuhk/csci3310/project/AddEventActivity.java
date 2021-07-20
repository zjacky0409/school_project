package edu.cuhk.csci3310.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {

    private Context context = this;
    private FirebaseFirestore db;

    TextView mDescription,mPeriod,mCal_burn,mLocation,mRemark,start_timer,end_timer,Weblink;
    String mDate;
    Button save_btn,reset_btn;
    int myHour,myMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Bundle bundle = getIntent().getExtras();
        mDate = bundle.getString("Date");

        Log.d("AddEventActivity",mDate);

        mDescription = findViewById(R.id.description);
        mPeriod = findViewById(R.id.period);
        mCal_burn = findViewById((R.id.cal_burn));
        mLocation = findViewById(R.id.location);
        mRemark = findViewById(R.id.remark);

        start_timer = findViewById(R.id.start_timer);
        end_timer = findViewById(R.id.end_timer);
        Weblink = findViewById(R.id.WebLink);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras.getString("description") != null){

           //  myWebView.loadUrl(extras.getString("web_link"));
            mDescription.setText(extras.getString("description"));
            mPeriod.setText(extras.getString("period"));
            mCal_burn.setText(extras.getString("cal_burn"));
            mLocation.setText(extras.getString("location"));
            mRemark.setText(extras.getString("remark"));
            start_timer.setText(extras.getString("start_time"));
            end_timer.setText(extras.getString("end_time"));
            Weblink.setText(extras.getString("web_link"));

        }







        save_btn = findViewById(R.id.save);

        reset_btn = findViewById(R.id.reset);

        db = FirebaseFirestore.getInstance();
        //Add data
        save_btn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
                //input data
               String description = mDescription.getText().toString();
               String period = mPeriod.getText().toString();
               String cal_burn = mCal_burn.getText().toString();
               String location = mLocation.getText().toString();
               String remark = mRemark.getText().toString();
               String start_time = start_timer.getText().toString();
               String end_time = end_timer.getText().toString();
               String web_link = Weblink.getText().toString(); // here

               if(isValidInput(description) == false || isValidInput(period) == false || isValidInput(cal_burn) == false
               || isValidInput(location) == false || isValidInput(start_time) == false
               || isValidInput(end_time) == false){
                   Toast.makeText(context, "Please enter all info.", Toast.LENGTH_SHORT).show();
               }else if(isNumeric(cal_burn) == false){
                   Toast.makeText(context, "Please enter a valid value for Calories Burn.", Toast.LENGTH_SHORT).show();
               }else if(isInteger(period) == false){
                   Toast.makeText(context, "Please enter a valid value for Period.", Toast.LENGTH_SHORT).show();
               }else{
                   //create event object

                   if(extras.getString("description") != null){
                       //
                       updateData(extras.getString("description"));
                       Event ev = new Event(description,period,cal_burn,location,remark,start_time,end_time,web_link);
                       uploadData(ev);
                   }else{
                       Event ev = new Event(description,period,cal_burn,location,remark,start_time,end_time,web_link);
                       //func to upload data
                       uploadData(ev);
                   }

               }
           }

            private void uploadData(Event newEvent) {

                db.collection(mDate).document(newEvent.getDescription()).set(newEvent);
                finish();
           }

           private void updateData(String description){
               db = FirebaseFirestore.getInstance();
               Log.d("mDate", mDate);
               db.collection(mDate).document(description).delete();
           }

            private boolean isValidInput(String input) {
                if (input.trim().length() > 0)
                    return true;
                return false;
            }

            private boolean isNumeric(String input){
                return input.matches("[0-9.]+");
            }

            private boolean isInteger(String input){
                return input.matches("[0-9]+");
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(extras.getString("description") != null){
                    mDescription.setText(extras.getString("description"));
                    mPeriod.setText(extras.getString("period"));
                    mCal_burn.setText(extras.getString("cal_burn"));
                    mLocation.setText(extras.getString("location"));
                    mRemark.setText(extras.getString("remark"));
                    start_timer.setText(extras.getString("start_time"));
                    end_timer.setText(extras.getString("end_time"));
                    Weblink.setText(extras.getString("web_link"));
                }else{
                    mDescription.setText("");
                    mPeriod.setText("");
                    mCal_burn.setText("");
                    mLocation.setText("");
                    mRemark.setText("");
                    start_timer.setText("");
                    end_timer.setText("");
                    Weblink.setText("");
                }

            }
        });

        start_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddEventActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute){
                                myHour = hour;
                                myMinute = minute;
                                String time = myHour + ":" + myMinute;
                                SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24Hours.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    start_timer.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12 ,0,false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(myHour ,myMinute);
                timePickerDialog.show();
            }
        });

        end_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddEventActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute){
                                myHour = hour;
                                myMinute = minute;
                                String time = myHour + ":" + myMinute;
                                SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    Date date = f24Hours.parse(time);
                                    SimpleDateFormat f12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    //Set selected time on text view
                                    end_timer.setText(f12Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 12 ,0,false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(myHour ,myMinute);
                timePickerDialog.show();
            }
        });
    }

    public void goToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d("TESTING = ",data.getStringExtra("location"));
            mLocation.setText(data.getStringExtra("location"));
        }
    }
}
