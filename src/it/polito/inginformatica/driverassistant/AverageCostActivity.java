package it.polito.inginformatica.driverassistant;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;

public class AverageCostActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_average_cost);
	    openChart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.average_cost, menu);
		return true;
	}
	
	private int[] getX (ArrayList<Refill> arrayRefill) { // axis x
		int[] x = new int[300];
		for (int i = 0; i < arrayRefill.size(); i++) {
			x[i] = i;
		}
		return x;
	}
	
	private float[] getY (ArrayList<Refill> arrayRefill) { // axis y, with the average cost per mile(or km)
		float[] y = new float[300];
		y[0] = 0;
		for (int i = 1; i < arrayRefill.size(); i++) {
			Refill refill = arrayRefill.get(i);
			Refill oldRefill = arrayRefill.get(i - 1);
			y[i] = Float.parseFloat(refill.getAmount())
					/ (Float.parseFloat(refill.getMileage()) - Float
							.parseFloat(oldRefill.getMileage()));
		}
		return y;
	}
	
	public double getAverage (ArrayList<Refill> arrayRefill) {
		double sumAmount = 0;
		double mileage = Float.parseFloat(arrayRefill.get(arrayRefill.size() - 1).getMileage());
		for (Refill refill : arrayRefill) {
			sumAmount = sumAmount + Float.parseFloat(refill.getAmount());
		}
		double average = sumAmount/mileage;
		return average;
	}
	
	private void openChart(){
		ArrayList<Refill> arrayRefill = Refill.getArray(getApplicationContext());
		int[] x = getX(arrayRefill);
		float[] y = getY(arrayRefill);
		double averageNotTruncate = getAverage(arrayRefill);
		int aux = (int)(averageNotTruncate*100);
		double average = aux/100d;
        XYSeries averageCost = new XYSeries(getString(R.string.average_cost_graphic));
        XYSeries averageNumber = new XYSeries(getString(R.string.average));
        // Adding data
        for(int i = 0; i < arrayRefill.size(); i++){
            averageCost.add(x[i], y[i]);
        }
        
        averageNumber.add(0, average);
        averageNumber.add(arrayRefill.size() - 1, average);
 
        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        
        // Adding Average Cost to the dataset
        dataset.addSeries(averageCost);
        dataset.addSeries(averageNumber);
        
        // Creating XYSeriesRenderer to customize averageCost
        XYSeriesRenderer averageCostRenderer = new XYSeriesRenderer();
        averageCostRenderer.setColor(Color.CYAN);
        averageCostRenderer.setPointStyle(PointStyle.CIRCLE);
        averageCostRenderer.setFillPoints(true);
        averageCostRenderer.setLineWidth(2);
        averageCostRenderer.setDisplayChartValues(true);
        
        // Creating XYSeriesRenderer to customize the average number
        XYSeriesRenderer averageNumberRenderer = new XYSeriesRenderer();
        averageNumberRenderer.setColor(Color.YELLOW);
        averageNumberRenderer.setPointStyle(PointStyle.CIRCLE);
        averageNumberRenderer.setFillPoints(true);
        averageNumberRenderer.setLineWidth(2);
        averageNumberRenderer.setDisplayChartValues(true);
        
        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setApplyBackgroundColor(true);
        multiRenderer.setBackgroundColor(Color.BLACK);
        multiRenderer.setXLabels(0);
        multiRenderer.clearXTextLabels();
        multiRenderer.setChartTitle(getString(R.string.average_cost_graphic));
        multiRenderer.setXTitle(getString(R.string.mileage_));
        multiRenderer.setYTitle(getString(R.string.amount));
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setLegendTextSize(20);
        multiRenderer.setLabelsTextSize(20);
        multiRenderer.setChartTitleTextSize(25);
 
        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(averageCostRenderer);
        multiRenderer.addSeriesRenderer(averageNumberRenderer);
 
        // Creating an intent to plot line chart using dataset and multipleRenderer
        Intent intent = ChartFactory.getLineChartIntent(getBaseContext(), dataset, multiRenderer);
 
        // Start Activity
        startActivity(intent);
	}

}
