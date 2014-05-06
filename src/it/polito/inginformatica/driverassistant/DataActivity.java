package it.polito.inginformatica.driverassistant;

import java.util.ArrayList;
import java.util.Iterator;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class DataActivity extends Activity {
	public static ListView listView;
	public static ArrayList<Refill> arrayRefill = new ArrayList<Refill>();
	public static final String PREFS_NAME = "MyPrefsFile";
	public static Runnable run;
	public static TextView data_measure;
	public static TextView data_amount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_data);
		checkPreferences();
		data_measure = (TextView) findViewById(R.id.tv_data_measure);
		data_amount = (TextView) findViewById(R.id.tv_data_amount);
		
		ArrayList<Refill> array = Refill.getArray(getApplicationContext());
		SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
		if (array != null) {
			if (!array.isEmpty()) {
				data_measure.setText("(" + array.get(0).getMeasure() + ")");
				data_amount.setText("(" + array.get(0).getCurrency() + ")");
			} else {
				if (settings.contains("measure") && settings.contains("currency")) {
					data_measure.setText("(" + settings.getString("measure", "") + ")");
					data_amount.setText("(" + settings.getString("currency", "") + ")");
				} else {
					data_measure.setText("(km)");
					data_amount.setText("(euro)");
				}
			}
		} 
		
		listView = (ListView) findViewById(R.id.lv_data);
		listView.setAdapter(new Adapter(this));
		run = new Runnable(){
            public void run(){
                   arrayRefill.clear();
                   arrayRefill = Refill.getArray(getApplicationContext());
                   ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                   listView.invalidateViews();
                   listView.refreshDrawableState();
            }
       };
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		runOnUiThread(run);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.add, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add:
			addNewData();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void checkPreferences() {
		ArrayList<Refill> arrayRefill = Refill.getArray(getApplicationContext());
		SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
		if (settings.contains("measure") && settings.contains("currency")) {
			String measure = settings.getString("measure", "");
			String currency = settings.getString("currency", "");
			if (arrayRefill != null && !arrayRefill.isEmpty()) {
				if (measure.equals("miles")) {
					toMileage(arrayRefill);
				} else if (measure.equals("km")) {
					toKm(arrayRefill);
				}
				
				if (currency.equals("dollar")) {
					toDolar(arrayRefill);
				} else if (currency.equals("euro")) {
					toEuro(arrayRefill);
				}
			}
		}
	}
	
	private void toMileage(ArrayList<Refill> arrayRefill) {
		if (arrayRefill.get(0).getMeasure().equals("km")) { // can be converted to mileages
			for (Refill refill : arrayRefill) {
				Float mileage = (float) (Float.parseFloat(refill.getMileage()) * 0.621371192);
				refill.setMeasure("miles");
				refill.setMileage(String.valueOf(mileage));
			}
		}
		Refill.setArray(getApplicationContext(), arrayRefill);
	}
	
	private void toKm (ArrayList<Refill> arrayRefill) {
		if (arrayRefill.get(0).getMeasure().equals("miles")) { // can be converted to km
			for (Refill refill : arrayRefill) {
				Float km = (float) (Float.parseFloat(refill.getMileage()) * 1.609344);
				refill.setMeasure("km");
				refill.setMileage(String.valueOf(km));
			}
		}
		Refill.setArray(getApplicationContext(), arrayRefill);
	}
	
	private void toDolar (ArrayList<Refill> arrayRefill) {
		if (arrayRefill.get(0).getCurrency().equals("euro")) { // can be converted to dolar
			for (Refill refill : arrayRefill) {
				Float dolar = (float) (Float.parseFloat(refill.getAmount()) * 1.3792);
				refill.setCurrency("dollar");
				refill.setAmount(String.valueOf(dolar));
			}
		}
		Refill.setArray(getApplicationContext(), arrayRefill);
	}
	
	private void toEuro (ArrayList<Refill> arrayRefill) {
		if (arrayRefill.get(0).getCurrency().equals("dollar")) { // can be converted to euro
			for (Refill refill : arrayRefill) {
				Float euro = (float) (Float.parseFloat(refill.getAmount()) * 0.725058005);
				refill.setCurrency("euro");
				refill.setAmount(String.valueOf(euro));
			}
		}
		Refill.setArray(getApplicationContext(), arrayRefill);
	}

	private void addNewData() {
		Intent intent = new Intent(getApplicationContext(), AddDataActivity.class);
		startActivity(intent);
	}
	
	private static class Adapter extends BaseAdapter {
        private LayoutInflater mInflater;
 
        public Adapter (Context context) {
            mInflater = LayoutInflater.from(context);
        }
 
        public int getCount() {
        	return arrayRefill.size();
        }
 
        public Object getItem(int position) {
            return position;
        }
 
        public long getItemId(int position) {
            return position;
        }
 
        public View getView (int position, View convertView, final ViewGroup parent) {
            ViewHolder holder;
            
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.row, null);
                holder = new ViewHolder();
                holder.text1 = (TextView) convertView.findViewById(R.id.TextView01);
                holder.text2 = (TextView) convertView.findViewById(R.id.TextView02);
                holder.text3 = (TextView) convertView.findViewById(R.id.TextView03);
                holder.edit_button = (ImageButton) convertView.findViewById(R.id.edit_button);
                holder.remove_button = (ImageButton) convertView.findViewById(R.id.remove_button);
                
                convertView.setTag(holder); 
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            final Refill refill = DataActivity.arrayRefill.get(position);
            
            holder.text1.setText(refill.getDate());
            holder.text2.setText(refill.getMileage());
            holder.text3.setText(refill.getAmount());
            
       
            holder.edit_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SharedPreferences settings = parent.getContext().getSharedPreferences(PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("date", refill.getDate());
					editor.putString("miles", refill.getMileage());
					editor.putString("amount", refill.getAmount());
					editor.commit();
					Intent intent = new Intent(parent.getContext(), AddDataActivity.class);
					parent.getContext().startActivity(intent);
				}
			});
            
            holder.remove_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder1 = new AlertDialog.Builder(parent.getContext());
		            builder1.setMessage(parent.getContext().getString(R.string.are_you_sure));
		            builder1.setCancelable(true);
		            builder1.setPositiveButton(parent.getContext().getString(R.string.yes),
		                    new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                	ArrayList<Refill> array = Refill.getArray(parent.getContext());
		                	Refill currentRefill = new Refill(refill.getDate(), refill.getMileage(), refill.getAmount(), refill.getMeasure(), refill.getCurrency());
		                	for (Iterator<Refill> iterator = array.iterator(); iterator.hasNext();) {
		                		Refill arrayRefill = iterator.next();
		                		if (currentRefill.isEqual(arrayRefill,currentRefill)) { 
									iterator.remove();
									Refill.setArray(parent.getContext(), array);
									((Activity) parent.getContext()).runOnUiThread(run);
								}
		                	}
		                }
		            });
		            builder1.setNegativeButton(parent.getContext().getString(R.string.no),
		                    new DialogInterface.OnClickListener() {
		                public void onClick(DialogInterface dialog, int id) {
		                    dialog.cancel();
		                }
		            });

		            AlertDialog alert11 = builder1.create();
		            alert11.show();
				}
			});
            return convertView;
        }
 
        static class ViewHolder {
			TextView text1;
            TextView text2;
            TextView text3;
            ImageButton edit_button;
            ImageButton remove_button;
        }
    }

}
