package edu.cuhk.csci3310.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;

public class EventListAdapter extends RecyclerView.Adapter<edu.cuhk.csci3310.project.EventListAdapter.EventViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private FirebaseFirestore db;

    private final LinkedList<Event> mEventList;
    private String mDate;

    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView textItemView;
        Button deleteBtn, changeBtn;

        final edu.cuhk.csci3310.project.EventListAdapter mAdapter;

        public EventViewHolder(View itemView, edu.cuhk.csci3310.project.EventListAdapter adapter) {
            super(itemView);
            this.mAdapter = adapter;
            textItemView = itemView.findViewById(R.id.event);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            changeBtn = itemView.findViewById(R.id.changeDetail);

            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //Toast.makeText(context, "This is "+mEventList.get(getAdapterPosition()).getDescription(), Toast.LENGTH_SHORT).show();
                    showAlertDialog(getAdapterPosition(),mAdapter);
                }
            });
            textItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(context, "After I click", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, EventDetailActivity.class);
                    int position = getPosition();

                    Bundle sendBundle = new Bundle();
                    sendBundle.putString("location", mEventList.get(position).getLocation());
                    sendBundle.putString("remark",mEventList.get(position).getRemark() );
                    sendBundle.putString("cal_burn",mEventList.get(position).getCal_burn() );
                    sendBundle.putString("description", mEventList.get(position).getDescription());
                    sendBundle.putString("period", mEventList.get(position).getPeriod());
                    sendBundle.putString("start_time", mEventList.get(position).getStartTime());
                    sendBundle.putString("end_time", mEventList.get(position).getEndTime());
                    sendBundle.putString("web_link", mEventList.get(position).getWebLink());
                   // sendBundle.putString("web_link", "https://www.cuhk.edu.hk");
                    sendBundle.putString("date", mDate);


                    intent.putExtras(sendBundle);

                    ((Activity) context).startActivity(intent);
                }
            });

            changeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddEventActivity.class);
                    int position = getPosition();

                    Bundle sendBundle = new Bundle();
                    sendBundle.putString("location", mEventList.get(position).getLocation());
                    sendBundle.putString("remark",mEventList.get(position).getRemark() );
                    sendBundle.putString("cal_burn",mEventList.get(position).getCal_burn() );
                    sendBundle.putString("description", mEventList.get(position).getDescription());
                    sendBundle.putString("period", mEventList.get(position).getPeriod());
                    sendBundle.putString("start_time", mEventList.get(position).getStartTime());
                    sendBundle.putString("end_time", mEventList.get(position).getEndTime());
                    sendBundle.putString("web_link", mEventList.get(position).getWebLink());
                    // sendBundle.putString("web_link", "https://www.cuhk.edu.hk");
                    sendBundle.putString("Date", mDate);


                    intent.putExtras(sendBundle);

                    ((Activity) context).startActivityForResult(intent,1);
                    // here
                }
            });
        }
    }

    public EventListAdapter(Context context,
                            LinkedList<Event> eventList,String date) {
        mInflater = LayoutInflater.from(context);
        this.mEventList = eventList;
        this.mDate = date;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.eventlist_item, parent, false);
        return new EventViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull edu.cuhk.csci3310.project.EventListAdapter.EventViewHolder holder, int position) {
        String mEventDescription = mEventList.get(position).getDescription();
        holder.textItemView.setText(mEventDescription);
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public void showAlertDialog(int position,EventListAdapter adapter){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Warning");
        alert.setMessage("Are you sure to remove this event?");
        alert.setPositiveButton("Yes",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int i){
                //Toast.makeText(context, "Clicked Yes", Toast.LENGTH_SHORT).show();
                db = FirebaseFirestore.getInstance();
                db.collection(mDate).document(mEventList.get(position).getDescription()).delete();
                mEventList.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        alert.setNegativeButton("No",null);
        alert.show();
    }


}
