package it.polito.inginformatica.driverassistant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StatisticsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		Button averageCost = (Button) findViewById(R.id.button_average_cost);
		final ArrayList<Refill> arrayRefill = Refill.getArray(getApplicationContext());
		averageCost.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!arrayRefill.isEmpty()) {
					Intent intent = new Intent(getApplicationContext(), AverageCostActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), getString(R.string.data_graphic),
							   Toast.LENGTH_LONG).show();
				}
			}
		});
		

		TextView amountSpent = (TextView) findViewById(R.id.textView_stat);
		int currentYear = getCurrentYear();
		float amountS = getAmountSpent(arrayRefill, currentYear);
		amountSpent.setText(getString(R.string.amount_spent_cYear) +String.valueOf(currentYear)+ "): " +String.valueOf(amountS));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.statistics, menu);
		return true;
	}
	
	public float getAmountSpent(ArrayList<Refill> arrayRefill, int year) {
		float total = 0;
		for (Refill refill : arrayRefill) {
			String refillDate = refill.getDate();
			String[] parts = refillDate.split("-");
			int refillYear = Integer.parseInt(parts[2]);
			if (year == refillYear) {
				total = total + Float.parseFloat(refill.getAmount());
			}
		}
		return total;
	}
	
	public int getCurrentYear() {
		String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String[] parts = currentDate.split("-");
		int currentYear = Integer.parseInt(parts[2]);
		return currentYear;
	}
	

}
