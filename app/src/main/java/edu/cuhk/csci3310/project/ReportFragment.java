package edu.cuhk.csci3310.project;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    final Calendar startDateCalendar = Calendar.getInstance();
    final Calendar endDateCalendar = Calendar.getInstance();

    public String mStartDate = null;
    public String mEndDate = null;

    Button searchButton;

    ArrayList<Event> periodEventList;
    ArrayList<String> periodDateList;

    private FirebaseFirestore db;
    CollectionReference collectionReference;
    int total;
    int count;
    int days;

    LinkedHashMap<String, Float> reverseDateCalMap = new LinkedHashMap<>();

    public ReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportFragment newInstance(String param1, String param2) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        EditText startDateEditText= (EditText) getActivity().findViewById(R.id.start_date);
        showCalender(startDateEditText,startDateCalendar,0);
        EditText endDateEditText= (EditText) getActivity().findViewById(R.id.end_date);
        showCalender(endDateEditText,endDateCalendar,1);

        RadioGroup radioGroup = getActivity().findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkId){

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");

                LocalDate startDate = LocalDate.parse(mStartDate,formatter);
                LocalDate endDate = LocalDate.parse(mEndDate,formatter);

                PieChart pieChart = getActivity().findViewById(R.id.pieChart);
                TextView totalCaltextView = getActivity().findViewById(R.id.total_cal_burn);
                TextView textView = getActivity().findViewById(R.id.textView);

                TextView periodPerDateView = getActivity().findViewById(R.id.periodPerDate);
                LineChart  lineChart = (LineChart) getActivity().findViewById(R.id.lineChart);

                GridLayout gridLayout = getActivity().findViewById(R.id.gridLayout);

                if(!endDate.isAfter(startDate)){
                    pieChart.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    totalCaltextView.setVisibility(View.INVISIBLE);
                    lineChart.setVisibility(View.INVISIBLE);
                    periodPerDateView.setVisibility(View.INVISIBLE);

                    for(int i=0;i<3;i++){
                        LinearLayout linearLayout = (LinearLayout) gridLayout.getChildAt(i);
                        linearLayout.setVisibility(View.INVISIBLE);
                    }
                }else{
                    switch (checkId){
                        case R.id.show_piechart:
                            pieChart.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);
                            totalCaltextView.setVisibility(View.VISIBLE);
                            lineChart.setVisibility(View.INVISIBLE);
                            final int[] index = {0};

                            reverseDateCalMap.entrySet().forEach( entry -> {
                                LinearLayout linearLayout = (LinearLayout) gridLayout.getChildAt(index[0]);
                                linearLayout.setVisibility(View.VISIBLE);
                                TextView starText = (TextView) linearLayout.getChildAt(1);
                                starText.setText(entry.getKey() + " : " + entry.getValue());
                                index[0]++;
                            });
                            periodPerDateView.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.show_linechart:
                            pieChart.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            totalCaltextView.setVisibility(View.INVISIBLE);
                            for(int i=0;i<3;i++){
                                LinearLayout linearLayout = (LinearLayout) gridLayout.getChildAt(i);
                                linearLayout.setVisibility(View.INVISIBLE);
                            }
                            lineChart.setVisibility(View.VISIBLE);
                            periodPerDateView.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        });


        searchButton =  (Button) getActivity().findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                PieChart pieChart = getActivity().findViewById(R.id.pieChart);
                TextView totalCaltextView = getActivity().findViewById(R.id.total_cal_burn);
                TextView textView = getActivity().findViewById(R.id.textView);

                TextView periodPerDateView = getActivity().findViewById(R.id.periodPerDate);
                LineChart  lineChart = (LineChart) getActivity().findViewById(R.id.lineChart);

                GridLayout gridLayout = getActivity().findViewById(R.id.gridLayout);

                pieChart.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                totalCaltextView.setVisibility(View.INVISIBLE);

                periodPerDateView.setVisibility(View.INVISIBLE);
                lineChart.setVisibility(View.INVISIBLE);
                RadioButton showPiechart = getActivity().findViewById(R.id.show_piechart);
                showPiechart.setChecked(true);

                for(int i=0;i<3;i++){
                    LinearLayout linearLayout = (LinearLayout) gridLayout.getChildAt(i);
                    linearLayout.setVisibility(View.INVISIBLE);
                }



                if(mStartDate != null && mEndDate != null){
                    //Toast.makeText(getActivity(), "Start Date = "+mStartDate+" " + "End Date = "+mEndDate, Toast.LENGTH_SHORT).show();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");

                    LocalDate startDate = LocalDate.parse(mStartDate,formatter);
                    LocalDate endDate = LocalDate.parse(mEndDate,formatter);

                    System.out.println("Local Start Date " + startDate);
                    System.out.println("Local End Date " + endDate);

                    if(endDate.isAfter(startDate)){

                        System.out.println("Is true ");
                        periodDateList = new ArrayList<>();

                        ProgressBar pb = getActivity().findViewById(R.id.progressBar);
                        pb.setVisibility(View.VISIBLE);
                        //testButton.setVisibility(View.VISIBLE);
                        LocalDate date = startDate;
                        days = (int) startDate.until(endDate, ChronoUnit.DAYS);
                        for(int i=0;i<days+1;i++){
                            String periodDate = date.plus(i, ChronoUnit.DAYS).format(formatter);
                            periodDateList.add(periodDate);
                        }
                        count = 0;
                        HashMap<String, Float> dateCalMap = new HashMap<String,Float>();
                        HashMap<String, Integer> datePeriodMap = new HashMap<String,Integer>();

                        for(String tmpDate : periodDateList) {
                            System.out.println(tmpDate);
                            db = FirebaseFirestore.getInstance();
                            collectionReference = db.collection(tmpDate);
                            periodEventList = new ArrayList<>();

                            readData(new FirestoreCallback() {
                                @Override
                                public void onCallback(ArrayList<Event> arrayList) {
                                    count++;

                                    System.out.println(tmpDate);
                                    pb.setProgress(count);
                                    System.out.println("Size of Event List " + periodEventList.size());
                                    if(periodEventList.size() != 0){
                                        Float date_total_cal = 0.0f;
                                        int date_total_period = 0;
                                        for(Event e:periodEventList){

                                            date_total_cal += Float.parseFloat(e.getCal_burn());
                                            date_total_period += Integer.parseInt(e.getPeriod());
                                            System.out.println(e.getDescription());
                                        }
                                        dateCalMap.put(tmpDate,date_total_cal);
                                        datePeriodMap.put(tmpDate,date_total_period);
                                        //periodEventList.clear();
                                    }
                                    //System.out.println(count);
                                    if(count == days+1){

                                        pb.setVisibility(View.GONE);
                                        //System.out.println("Map = " + dateEventMap);
                                        //System.out.println("Size of Map = " + dateEventMap.size());

                                        System.out.println(dateCalMap);
                                        reverseDateCalMap.clear();
                                        dateCalMap.entrySet()
                                                .stream()
                                                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                                .limit(3)
                                                .forEachOrdered(x -> reverseDateCalMap.put(x.getKey(), x.getValue()));
                                        System.out.println(reverseDateCalMap);

                                        if(dateCalMap.size() > 0 && datePeriodMap.size() > 0){

                                            radioGroup.setVisibility(View.VISIBLE);
                                            //For PieChart
                                            PieChart pieChart = getActivity().findViewById(R.id.pieChart);
                                            ArrayList<PieEntry> chartData = new ArrayList<>();

                                            AtomicReference<Float> total_cal = new AtomicReference<>(0.0f);
                                            dateCalMap.forEach((key,value) -> {
                                                chartData.add(new PieEntry(value,key));
                                                total_cal.updateAndGet(v1 -> v1 + value);
                                            });

                                            PieDataSet pieDataSet = new PieDataSet(chartData,null);
                                            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                            pieDataSet.setValueTextColor(Color.BLACK);
                                            pieDataSet.setValueTextSize(16f);

                                            PieData pieData = new PieData(pieDataSet);
                                            pieChart.setData(pieData);
                                            pieChart.notifyDataSetChanged();
                                            pieChart.invalidate();
                                            pieChart.getDescription().setEnabled(false);
                                            pieChart.setCenterText("Calories Burn");
                                            pieChart.setVisibility(View.VISIBLE);
                                            pieChart.animate();

                                            TextView totalCaltextView = getActivity().findViewById(R.id.total_cal_burn);
                                            TextView textView = getActivity().findViewById(R.id.textView);
                                            textView.setVisibility(View.VISIBLE);
                                            totalCaltextView.setVisibility(View.VISIBLE);
                                            totalCaltextView.setText(total_cal.toString());

                                            //For linechart
                                            LineChart  lineChart = (LineChart) getActivity().findViewById(R.id.lineChart);
                                            ArrayList<Entry> lineData = new ArrayList<>();

                                            final Float[] tmpflaot = {0.0f};
                                            datePeriodMap.forEach((key,value) -> {
                                                lineData.add(new Entry(tmpflaot[0],value));
                                                tmpflaot[0] += 1.0f;
                                            });

                                            LineDataSet lineDataSet = new LineDataSet(lineData,"Period/Date");
                                            lineDataSet.setDrawValues(true);

                                            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                            dataSets.add(lineDataSet);

                                            LineData data = new LineData(dataSets);
                                            YAxis yAxis = lineChart.getAxisLeft();
                                            YAxis rightAxis = lineChart.getAxisRight();
                                            rightAxis.setDrawGridLines(false);
                                            yAxis.setAxisMinimum(0);
                                            yAxis.setDrawGridLines(true);
                                            yAxis.enableGridDashedLine(10f, 10f, 0f);
                                            XAxis xAxis = lineChart.getXAxis();
                                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                            xAxis.setLabelCount(6,true);
                                            xAxis.setDrawGridLines(false);
                                            xAxis.setDrawLabels(false);

                                            lineChart.setData(data);
                                            lineChart.getDescription().setEnabled(false);
                                            lineChart.setTouchEnabled(true);
                                            lineChart.setDragEnabled(true);
                                            lineChart.invalidate();

                                            GridLayout gridLayout = getActivity().findViewById(R.id.gridLayout);
                                            final int[] index = {0};

                                            reverseDateCalMap.entrySet().forEach( entry -> {
                                                LinearLayout linearLayout = (LinearLayout) gridLayout.getChildAt(index[0]);
                                                linearLayout.setVisibility(View.VISIBLE);
                                                TextView starText = (TextView) linearLayout.getChildAt(1);
                                                starText.setText(entry.getKey() + " : " + entry.getValue());
                                                index[0]++;
                                            });

                                        }else{
                                            Toast.makeText(getActivity(), "No Record", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                        }
                    }else{
                        RadioGroup rg = getActivity().findViewById(R.id.radio_group);
                        rg.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(), "Please Choose Valid Date", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Please Input the Period", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void readData(FirestoreCallback firestoreCallback){
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    periodEventList.clear();
                    for (DocumentSnapshot document : task.getResult()) {
                        Event ev = document.toObject(Event.class);
                        periodEventList.add(ev);
                    }
                    firestoreCallback.onCallback(periodEventList);
                }
            }
        });
    }

    private interface FirestoreCallback{
        void onCallback( ArrayList<Event> arrayList );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    public void showCalender(EditText editText,Calendar calendar,int op){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
                editText.setText(sdf.format(calendar.getTime()));
                switch(op){
                    case 0:
                        mStartDate = editText.getText().toString();
                        break;
                    default:
                        mEndDate = editText.getText().toString();
                        break;
                }
            }
        };
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}