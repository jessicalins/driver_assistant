package it.polito.inginformatica.driverassistant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class AddDataActivity extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";
	public static EditText editText_mileage;
	public static EditText editText_amount;
	public static TextView measure;
	public static TextView amount;
	public static Refill oldRefill;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_data);
		
		editText_mileage = (EditText) findViewById(R.id.editText1);
		editText_amount = (EditText) findViewById(R.id.editText2);
		measure = (TextView) findViewById(R.id.tv_measure);
		amount = (TextView) findViewById(R.id.tv_amount);
		
		final ArrayList<Refill> array = Refill.getArray(getApplicationContext());
		if (array != null) {
			if (!array.isEmpty()) {
				measure.setText("(" + array.get(0).getMeasure() + ")");
				amount.setText("(" + array.get(0).getCurrency() + ")");
			} else {
				SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
				if (settings.contains("measure") && settings.contains("currency")) {
					measure.setText("(" + settings.getString("measure", "") + ")");
					amount.setText("(" + settings.getString("currency", "") + ")");
				} else {
					measure.setText("(km)");
					amount.setText("(euro)");
				}
			}
		} 
		
		final boolean isEditMode = checkEditMode();
		Button button = (Button) findViewById(R.id.button5);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// validation of input
				boolean vMileage = validate(editText_mileage, true, isEditMode, array);
				boolean vAmount = validate(editText_amount, false, isEditMode, array);
				if (vMileage && vAmount) { // if its valid
					sendPreferences(isEditMode);
					finish();
				} 
			}
		});
	}

	public boolean checkEditMode() { // check if there is need of set text in the editText
		SharedPreferences settings = getApplicationContext()
				.getSharedPreferences(PREFS_NAME, 0);
		if (settings.contains("miles") && settings.contains("amount")) { // edit mode
			String date = settings.getString("date", "");
			String mileage = settings.getString("miles", "");
			String amount = settings.getString("amount", "");
			String measure = settings.getString("measure", "");
			String currency = settings.getString("currency", "");
			oldRefill = new Refill(date, mileage, amount, measure, currency);
			editText_mileage.setText(mileage);
			editText_amount.setText(amount);
			SharedPreferences.Editor editor = settings.edit();
			editor.remove("date");
			editor.remove("miles");
			editor.remove("amount");
			editor.remove("measure");
			editor.remove("currency");
			editor.commit();
			return true;
		}
		return false;
	}

	public void sendPreferences(boolean isEditMode) {
		String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		String mileage = editText_mileage.getText().toString();
		String amount = editText_amount.getText().toString();
		String measure = "km"; // standard measure
		String currency = "euro"; // standard currency
		Refill refill = new Refill(date, mileage, amount, measure, currency);
		ArrayList<Refill> array = Refill.getArray(getApplicationContext());
		if (isEditMode) {
			for (Refill arrRefill : array) {
				if (oldRefill.isEqual(arrRefill,oldRefill)) { 
					arrRefill.setMileage(mileage);
					arrRefill.setAmount(amount);
				}
			}
		} else {
			array.add(refill);
		}
		Refill.setArray(getApplicationContext(), array);
	}
	
	public boolean validate(EditText editText, boolean isMeasure, boolean isEditMode, ArrayList<Refill> arrayRefill) {
		if (editText.getText().toString().matches("")) {
			Toast.makeText(this, getString(R.string.toast_field), Toast.LENGTH_SHORT).show();
			return false;
		} else if (isMeasure) {
			Float lastValue = (float) 0.0;
			if (arrayRefill != null) {
				if (arrayRefill.size() == 1) {
					lastValue = Float.parseFloat(arrayRefill.get(0).getMileage());
				} else if (arrayRefill.size() > 0) {
					lastValue = Float.parseFloat(arrayRefill.get(arrayRefill.size() - 1).getMileage());
				}
			}
			Float newValue = Float.parseFloat(editText.getText().toString());
			if (isEditMode) {
				if (lastValue != 0) {
					if (newValue < lastValue) {
						Toast.makeText(this, getString(R.string.toast_mileage), Toast.LENGTH_LONG).show();
						return false;
					}
				}
			} else {
				if (lastValue != 0) {
					if (newValue <= lastValue) {
						Toast.makeText(this, getString(R.string.toast_mileage), Toast.LENGTH_LONG).show();
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_data, menu);
		return true;
	}

}
