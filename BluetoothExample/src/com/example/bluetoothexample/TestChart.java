package com.example.bluetoothexample;

import java.util.Random;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer.GridStyle;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.jjoe64.graphview.LegendRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class TestChart extends Activity{

	Handler mHandler = new Handler();
	Runnable mTimer;
	LineGraphSeries<DataPoint> mSeries1;
	double graph2LastXValue = 10d;
	double mLastRandom = 5;
    Random mRand = new Random();
    GraphView graph;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_graph_layout);
		
		 graph = (GraphView) findViewById(R.id.graph);
        //mSeries1 = new LineGraphSeries<>();
//        mSeries1.setColor(Color.RED);
//        graph.addSeries(mSeries1);
//        graph.getGridLabelRenderer().setGridStyle(GridStyle.NONE);
        //graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        //graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(0);
//        graph.getViewport().setMaxX(20);
       
       /* mSeries1.setOnDataPointTapListener(new OnDataPointTapListener() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void onTap(Series arg0, DataPointInterface datapoint) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), datapoint.getY() + "", Toast.LENGTH_SHORT).show();
			}
		});*/
        
        /*mTimer = new Runnable() {
            @Override
            public void run() {
                graph2LastXValue += 1d;
                mSeries1.appendData(new DataPoint(graph2LastXValue, getRandom()), true, 40);
                mHandler.postDelayed(this, 200);          	
            }
        };
        mHandler.postDelayed(mTimer, 1000);*/
        
		 // first series is a line
		 DataPoint[] points = new DataPoint[50];
	        for (int i = 0; i < 50; i++) {
	            points[i] = new DataPoint(i, Math.sin(i*0.5) * 20*(Math.random()*10+1));
	        }
	        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

	        // set manual X bounds
	        graph.getViewport().setXAxisBoundsManual(true);
	        graph.getViewport().setMinX(0);
	        graph.getViewport().setMaxX(10);

	        // enable scrolling
	        graph.getViewport().setScrollable(true);

	        series.setTitle("Random Curve");

	        graph.addSeries(series);

	        graph.getLegendRenderer().setVisible(true);
	        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
	}

	private double getRandom() {
        return (mLastRandom += mRand.nextDouble()*0.5 - 0.25);
    }

/*	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onStop();
		mHandler.removeCallbacks(mTimer);
	} */   
}
