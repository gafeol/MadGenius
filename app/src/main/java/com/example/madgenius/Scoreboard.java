package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scoreboard extends AppCompatActivity {

    private ScoreViewModel scoreViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ScoreListAdapter adapter = new ScoreListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        scoreViewModel.getAllScores().observe(this, scores -> {
            adapter.setScores(scores);
        });

        List<Double> listScores = new ArrayList<Double>();
        scoreViewModel.getAllScores("cobra", true).observe(this, scores -> {
            listScores.addAll(scores);
            for(int i=0;i<listScores.size();i++){
                Log.d("SCORES", "Score "+listScores.get(i));
            }
        });
        List<Double> testlist = Arrays.asList(new Double[]{10.0, 15.0, 12.0, 19.0, 16.0, 21.0});

        plot(testlist);



    }

    protected void plot(List<Double> scoreList){
        LineChart scoreChart = findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();

        for(int i=0; i < scoreList.size(); i++){
            Entry entry = new Entry((float)i+1 ,scoreList.get(i).floatValue());
            Log.d("X AXIS", String.valueOf((float)i+1));
            entries.add(entry);
        }

        LineDataSet lineData = new LineDataSet(entries, "Dataset 1");
        lineData.setAxisDependency(YAxis.AxisDependency.LEFT);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineData);
        LineData data = new LineData(dataSets);

        scoreChart.setData(data);
        scoreChart.invalidate();

    }
}
