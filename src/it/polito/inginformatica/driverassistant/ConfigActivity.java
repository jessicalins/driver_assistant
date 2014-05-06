package it.polito.inginformatica.driverassistant;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ConfigActivity extends Activity {
	public static final String PREFS_NAME = "MyPrefsFile";
	private RadioGroup radioMeasure;
	private RadioGroup radioCurrency;
	private RadioButton radioButtonMeasure;
	private RadioButton radioButtonCurrency;
	private RadioButton rbMileage;
	private RadioButton rbKm;
	private RadioButton rbEuro;
	private RadioButton rbDolar;
	private Button btSubmit;
	private String selectedM;
	private String selectedC;
	private ArrayList<Refill> arrayRefill;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		addListenerOnButton();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		rbMileage.setChecked(settings.getBoolean("radioMileage", false));
		rbKm.setChecked(settings.getBoolean("radioKm", false));
		rbEuro.setChecked(settings.getBoolean("radioEuro", false));
		rbDolar.setChecked(settings.getBoolean("radioDolar", false));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}
	
	public void addListenerOnButton() {
		radioMeasure = (RadioGroup) findViewById(R.id.radio_measure);
		radioCurrency = (RadioGroup) findViewById(R.id.radio_currency);
		rbMileage = (RadioButton) findViewById(R.id.radioMileage);
		rbKm = (RadioButton) findViewById(R.id.radioKm);
		rbDolar = (RadioButton) findViewById(R.id.radioDolar);
		rbEuro = (RadioButton) findViewById(R.id.radioEuro);
		
		btSubmit = (Button) findViewById(R.id.bt_c_submit);
		
		btSubmit.setOnClickListener(new OnClickListener() {
	 
			@Override
			public void onClick(View v) {
			    // get selected radio button from radioGroup
				int selectedMeasure = radioMeasure.getCheckedRadioButtonId();
				int selectedCurrency = radioCurrency.getCheckedRadioButtonId();
				
				// find the radiobutton by returned id
			    radioButtonMeasure = (RadioButton) findViewById(selectedMeasure);
			    radioButtonCurrency = (RadioButton) findViewById(selectedCurrency);
	 
			    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
			    SharedPreferences.Editor editor = settings.edit();
			    
				selectedM = (String) radioButtonMeasure.getText();
				selectedC = (String) radioButtonCurrency.getText();
				
				addListenerOnButton();
				editor.putString("measure", selectedM);
				editor.putString("currency", selectedC);
				arrayRefill = Refill.getArray(getApplicationContext());
//				if (arrayRefill != null) {
//					setPreferencesOnArray(arrayRefill, selectedM, selectedC);
//					Refill.setArray(getApplicationContext(), arrayRefill);
//				}
				editor.putBoolean("radioMileage", rbMileage.isChecked());
				editor.putBoolean("radioKm", rbKm.isChecked());
				editor.putBoolean("radioDolar", rbDolar.isChecked());
				editor.putBoolean("radioEuro", rbEuro.isChecked());
				editor.commit();
				finish();
			}
		});
	  }
	
	public void setPreferencesOnArray (ArrayList<Refill> arrayRefill, String measure, String currency) {
		for (Refill refill : arrayRefill) {
			refill.setMeasure(measure);
			refill.setCurrency(currency);
		}
	}
}
