package com.example.madgenius;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;


public class ScoreChart extends AppCompatActivity {

    private ScoreViewModel scoreViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_chart);
        Intent intent = getIntent();
        String username = intent.getStringExtra(Scoreboard.EXTRA_MESSAGE);
        TextView title = findViewById(R.id.title_chartScoreboard);

        title.setText(username + "'s score Chart");

        scoreViewModel = ViewModelProviders.of(this).get(ScoreViewModel.class);
        List<Integer> listScores = new ArrayList<>();
        final ToggleButton toggle = (ToggleButton)findViewById(R.id.toggleButton2);

        toggle.setOnCheckedChangeListener((buttonView, isChecked) ->  {
                    scoreViewModel.getAllScores(username, isChecked).observe(ScoreChart.this, scores -> {
                        listScores.clear();
                        listScores.addAll(scores);
                        plot(listScores);
                    });
        });

        scoreViewModel.getAllScores(username, true).observe(ScoreChart.this, scores -> {
            listScores.clear();
            listScores.addAll(scores);
            plot(listScores);

        });


    }

    protected void plot(List<Integer> scoreList){
        LineChart scoreChart = findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<>();

        for(int i=0; i < scoreList.size(); i++){
            Entry entry = new Entry((float)i+1 ,scoreList.get(i).floatValue());
            Log.d("X AXIS", String.valueOf((float)i+1));
            entries.add(entry);
        }

        /* Line data settings */
        LineDataSet lineData = new LineDataSet(entries, "Points");
        lineData.setAxisDependency(YAxis.AxisDependency.LEFT); //default
        lineData.setCircleColor(getResources().getColor(R.color.colorAccent));
        lineData.setColor(getResources().getColor(R.color.colorAccent));
        lineData.setValueTextColor(getResources().getColor(R.color.white));
        lineData.setCircleRadius(10f);
        lineData.setValueTextSize(20f);
        lineData.setLineWidth(2f);





        /* X axis settings */
        XAxis Xaxis = scoreChart.getXAxis();
        Xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        Xaxis.setDrawLabels(true);
        Xaxis.setDrawGridLines(false);
        Xaxis.setAxisMinimum(0f);
        Xaxis.setAxisMaximum(scoreList.size()+1);
        Xaxis.setTextSize(20f);
        Xaxis.setTextColor(getResources().getColor(R.color.white));
        Xaxis.setLabelCount(scoreList.size()+2, true);

        /* Y axis settings (right not used) */
        YAxis rightAxis = scoreChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);

        YAxis leftAxis = scoreChart.getAxisLeft();
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(30f);
        leftAxis.setAxisMinimum(0f);


        Description description = scoreChart.getDescription();
        description.setEnabled(false);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineData);
        LineData data = new LineData(dataSets);

        scoreChart.setData(data);
        scoreChart.invalidate();
    }
}
