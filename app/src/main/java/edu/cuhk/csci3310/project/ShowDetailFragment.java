package edu.cuhk.csci3310.project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;


public class ShowDetailFragment extends Fragment {


    TextView mDescription,mPeriod,mCal_burn,mLocation,mRemark,testing,end_time,start_time;
    Button goToWeb;
    String description, period,cal_burn,remark,webLink,end_time_string,start_time_string;

    public ShowDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_show_detail, container, false);
        mDescription = rootView.findViewById(R.id.description_sd);
        mPeriod = rootView.findViewById(R.id.period_sd);
        mCal_burn = rootView.findViewById(R.id.cal_burn_sd);
        mRemark = rootView.findViewById((R.id.remark_sd));
        goToWeb = rootView.findViewById(R.id.goToWeb);
        end_time = rootView.findViewById(R.id.end_time_two);
        start_time = rootView.findViewById(R.id.start_time_two);

        try {
            description = getArguments().getString("description");
            period = getArguments().getString("period");
            cal_burn = getArguments().getString("cal_burn");
            remark = getArguments().getString("remark");
            webLink =  getArguments().getString("web_link");
            end_time_string = getArguments().getString("end_time");
           //  Log.d("HJBKHBKJB KJ = ",end_time_string);
            start_time_string = getArguments().getString("start_time");


        } catch (Exception e) {
            Log.d("ERROR == ", String.valueOf(e));
            description = "Error";
            period = "Error";
            cal_burn = "Error";
            remark = "Error";
        }

        goToWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WebResourceActivity.class);
                // String message = editText.getText().toString();
                intent.putExtra("web_link", webLink);
                startActivity(intent);
            }
        });

        mDescription.setText(description);

        mCal_burn.setText(cal_burn);
        mRemark.setText(remark);
        mPeriod.setText(period);
        end_time.setText(end_time_string);
        start_time.setText(start_time_string);
        mCal_burn.setEnabled(false);
        mCal_burn.setBackgroundColor(Color.TRANSPARENT);

        mDescription.setEnabled(false);
        mDescription.setBackgroundColor(Color.TRANSPARENT);



        start_time.setEnabled(false);
        start_time.setBackgroundColor(Color.TRANSPARENT);

        end_time.setEnabled(false);
        end_time.setBackgroundColor(Color.TRANSPARENT);


        mPeriod.setEnabled(false);
        mPeriod.setBackgroundColor(Color.TRANSPARENT);

        mRemark.setEnabled(false);
        mRemark.setBackgroundColor(Color.TRANSPARENT);
        return rootView;
    }

    public void goToWebResource(View view) {
        Intent intent = new Intent(getContext(), WebResourceActivity.class);
        // String message = editText.getText().toString();
        // intent.putExtra("link", link);
        startActivity(intent);
    }
}