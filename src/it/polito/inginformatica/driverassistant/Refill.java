package it.polito.inginformatica.driverassistant;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;

public class Refill {
	private String date;
	private String mileage;
	private String amount;
	private String measure;
	private String currency;
	public static final String PREFS_NAME = "MyPrefsFile";
	
	public Refill(String date, String mileage, String amount, String measure, String currency) {
		super();
		this.date = date;
		this.mileage = mileage;
		this.amount = amount;
		this.measure = measure;
		this.currency = currency;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public static ArrayList<Refill> getArray(Context context) {
		ArrayList<Refill> arrayRefill = new ArrayList<Refill>();
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		if (settings.contains("refill")) {
			String json = settings.getString("refill", "");
			Type type = new TypeToken<ArrayList<Refill>>(){}.getType();
			arrayRefill = new Gson().fromJson(json, type);
		}
		return arrayRefill;
	}
	
	public static void setArray(Context context, ArrayList<Refill> arrayRefill) {
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		String refill = new Gson().toJson(arrayRefill);
		editor.putString("refill", refill);
		editor.commit();
	}

	public boolean isEqual(Refill r1, Refill r2) {
		if (r1.getDate().equals(r2.getDate()) && r1.getMileage().equals(r2.getMileage()) 
				&& r1.getAmount().equals(r2.getAmount())) {
			return true;
		}
		return false;
	}
}
