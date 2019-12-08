package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Scoreboard extends AppCompatActivity {
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newscoreboard);
        final TextView text = (TextView)findViewById(R.id.bestof);
        final ToggleButton toggle = (ToggleButton)findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    text.setText(R.string.best_memory);
                } else {
                    text.setText(R.string.best_agility);
                }
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ScoreListAdapter adapter = new ScoreListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScoreViewModel scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        scoreViewModel.getAllScores().observe(this, scores -> {
            adapter.setScores(scores);
        });
    }
}

//    private ScoreViewModel scoreViewModel;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scoreboard);
//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//        final ScoreListAdapter adapter = new ScoreListAdapter(this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
//        scoreViewModel.getAllScores().observe(this, scores -> {
//            adapter.setScores(scores);
//        });
//
//        List<Double> listScores = new ArrayList<>();
//        String username;
//        scoreViewModel.getAllScores("cobra", true).observe(this, scores -> {
//            listScores.clear();
//            listScores.addAll(scores);
//
//
//            plot(listScores);
//
//        });
//
//
//
//    }
//
//    protected void plot(List<Double> scoreList){
//        LineChart scoreChart = findViewById(R.id.chart);
//        List<Entry> entries = new ArrayList<>();
//
//        for(int i=0; i < scoreList.size(); i++){
//            Entry entry = new Entry((float)i+1 ,scoreList.get(i).floatValue());
//            Log.d("X AXIS", String.valueOf((float)i+1));
//            entries.add(entry);
//        }
//
//
//        /* Line data settings */
//        LineDataSet lineData = new LineDataSet(entries, "Variable");
//        lineData.setAxisDependency(YAxis.AxisDependency.LEFT); //default
//        lineData.setCircleRadius(10f);
//        lineData.setValueTextSize(20f);
//        lineData.setLineWidth(2f);
//
//
//
//        /* X axis settings */
//        XAxis Xaxis = scoreChart.getXAxis();
//        Xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        Xaxis.setDrawLabels(true);
//        //Xaxis.setDrawGridLines(false);
//        Xaxis.setAxisMinimum(0f);
//        Xaxis.setAxisMaximum(scoreList.size()+1);
//        Xaxis.setTextSize(20f);
//        Xaxis.setLabelCount(scoreList.size()+2, true);
//
//
//
//        /* Y axis settings (right not used) */
//        YAxis rightAxis = scoreChart.getAxisRight();
//        rightAxis.setDrawLabels(false);
//        rightAxis.setDrawGridLines(false);
//
//        YAxis leftAxis = scoreChart.getAxisLeft();
//        leftAxis.setDrawLabels(false);
//        leftAxis.setDrawGridLines(false);
//        leftAxis.setAxisMaximum(30f);
//        leftAxis.setAxisMinimum(0f);
//
//        Description description = scoreChart.getDescription();
//        description.setEnabled(false);
//
//
//
//
//        List<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(lineData);
//        LineData data = new LineData(dataSets);
//
//        scoreChart.setData(data);
//        scoreChart.invalidate();
//
//    }
//}
