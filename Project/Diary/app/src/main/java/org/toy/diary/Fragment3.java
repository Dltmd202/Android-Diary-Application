package org.toy.diary;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Fragment3 extends Fragment {
    PieChart chart;
    BarChart chart2;
    LineChart chart3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment3, container, false);
        initUI(rootView);
        return rootView;
    }
    public void initUI(ViewGroup rootView){
        chart = rootView.findViewById(R.id.chart1);
        chart2 = rootView.findViewById(R.id.chart2);
        chart3 = rootView.findViewById(R.id.chart3);

        chart.setCenterText("기분별 비율");
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);

        Legend legent1 = chart.getLegend();
        legent1.setEnabled(false);

        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);

        setData1();
        
        chart2.setDrawValueAboveBar(true);
        chart2.setDrawGridBackground(false);
        chart2.getDescription().setEnabled(false);

        XAxis xAxis = chart2.getXAxis();
        xAxis.setEnabled(false);

        YAxis leftAxis = chart2.getAxisLeft();
        leftAxis.setLabelCount(6,false);
        leftAxis.setAxisMaximum(0.0f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setGranularity(1f);

        YAxis rightAxis = chart2.getAxisRight();
        rightAxis.setEnabled(false);

        Legend legend2= chart2.getLegend();
        legend2.setEnabled(false);

        chart2.animateXY(1500,1500);
        setData2();

        chart3.getDescription().setEnabled(false);
        chart3.setDrawGridBackground(false);
        chart3.setBackgroundColor(Color.WHITE);
        chart3.setViewPortOffsets(0,0,0,0);

        Legend legend3 = chart3.getLegend();
        legend3.setEnabled(false);

        XAxis xAxis3 = chart3.getXAxis();
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis3.setTextSize(10f);
        xAxis3.setTextColor(Color.WHITE);
        xAxis3.setDrawAxisLine(false);
        xAxis3.setDrawGridLines(true);
        xAxis3.setCenterAxisLabels(true);
        xAxis3.setGranularity(1f);
        xAxis3.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                long millis = TimeUnit.HOURS.toMillis((long)value);
                return super.getFormattedValue(value);
            }
        });

        YAxis leftAxis3 =chart3.getAxisLeft();
        leftAxis3.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis3.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis3.setDrawGridLines(true);
        leftAxis3.setGranularityEnabled(true);
        leftAxis3.setAxisMaximum(0f);
        leftAxis3.setAxisMaximum(170f);
        leftAxis3.setYOffset(-9f);
        leftAxis3.setTextColor(Color.rgb(255,192,56));

        YAxis rightAxis3 = chart3.getAxisRight();
        rightAxis3.setEnabled(false);

        setData3();
    }
    private void setData1(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(20.0f,"",getResources().getDrawable(R.drawable.smile1_24)));
        entries.add(new PieEntry(20.0f,"",getResources().getDrawable(R.drawable.smile2_24)));
        entries.add(new PieEntry(20.0f,"",getResources().getDrawable(R.drawable.smile3_24)));
        entries.add(new PieEntry(20.0f,"",getResources().getDrawable(R.drawable.smile4_24)));
        entries.add(new PieEntry(20.0f,"",getResources().getDrawable(R.drawable.smile5_24)));

        PieDataSet dataSet = new PieDataSet(entries,"기분별 비율 ");
        dataSet.setDrawIcons(true);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0,40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.JOYFUL_COLORS){
            colors.add(c);
        }
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(22.0f);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);
        chart.invalidate();
    }

    private void setData2(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        BarEntry bar = new BarEntry(1,20);
        entries.add(new BarEntry(2,30));
        entries.add(new BarEntry(3,40));
        entries.add(new BarEntry(4,50));
        entries.add(new BarEntry(5,70));

        BarDataSet barDataSet = new BarDataSet(entries,"요일별 기분");
        barDataSet.setBarBorderColor(Color.WHITE);
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.JOYFUL_COLORS){
            colors.add(c);
        }
        barDataSet.setColors(colors);

        BarData data = new BarData(barDataSet);
        chart2.setData(data);
        chart2.invalidate();
    }

    private  void setData3(){
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1,20f));
        entries.add(new Entry(2,30f));
        entries.add(new Entry(3,40f));
        entries.add(new Entry(4,50f));
        entries.add(new Entry(5,70f));

        LineDataSet dataSet = new LineDataSet(entries,"기분별 차트");
        dataSet.setDrawIcons(true);
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.JOYFUL_COLORS){
            colors.add(c);
        }
        dataSet.setColors(colors);
        LineData data = new LineData(dataSet);
        chart3.setData(data);
        chart3.invalidate();

    }


}