package it.polito.inginformatica.driverassistant;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button viewData = (Button) findViewById(R.id.bt_view_data);
        viewData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), DataActivity.class);
		        startActivity(intent);
			}
		});
        
        Button stat = (Button) findViewById(R.id.bt_statistics);
        stat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), StatisticsActivity.class);
		        startActivity(intent);
			}
		});
        
        Button config = (Button) findViewById(R.id.bt_config);
        config.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), ConfigActivity.class);
		        startActivity(intent);
			}
		});
        
        Button about = (Button) findViewById(R.id.bt_about);
        about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(view.getContext(), AboutActivity.class);
		        startActivity(intent);
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
