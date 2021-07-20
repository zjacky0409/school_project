package edu.cuhk.csci3310.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;

public class DateDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EventListAdapter mAdapter;
    private TextView mTextView;
    private Button mButton;
    Context mContext = this;
    private String mDate;

    private LinkedList<Event> mEventList = new LinkedList<>();

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_detail);

        mButton = findViewById(R.id.addEvent);
        mTextView = findViewById(R.id.myDate);

        Bundle bundle = getIntent().getExtras();
        mDate = bundle.getString("Date");

        mTextView.setText(mDate);

        db = FirebaseFirestore.getInstance();
        CollectionReference events = db.collection(mDate);
        Query query = events.limit(30);
        query.get().addOnCompleteListener(task ->{
            QuerySnapshot querySnapshot = task.isSuccessful() ? task.getResult() : null;
            for(DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                Event ev = documentSnapshot.toObject(Event.class);
                mEventList.addLast(ev);
                Log.d("DateDetailActivity",ev.getDescription());
                Log.d("DateDetailActivity",""+ mEventList.size());
            }

            // Get a handle to the RecyclerView.
            mRecyclerView = findViewById(R.id.recyclerview);
            // Create an adapter and supply the data to be displayed.
            mAdapter = new EventListAdapter(this, mEventList,mDate);
            // Connect the adapter with the RecyclerView.
            mRecyclerView.setAdapter(mAdapter);
            // Give the RecyclerView a default layout manager.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, AddEventActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Date",mDate);
                it.putExtras(bundle);
                DateDetailActivity.this.startActivityForResult(it, 1);
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        recreate();
    }
}