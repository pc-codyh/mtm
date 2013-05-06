package cody.mtmanager.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class ExtraStats extends Activity
{
	// Button to advance to the next Activity.
	Button _continue;
	
	// Optional standings categories.
	CheckBox _points;
	CheckBox _score;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.extra_stats);
		
		initializeGlobalVariables();
		setOnContinueClickListener();
	}
	
	// Function to initialize all global variables.
	// This prevents cluttering in the onCreate method.
	private void initializeGlobalVariables()
	{
		_continue = (Button) findViewById(R.id.es_continue);
		
		_points = (CheckBox) findViewById(R.id.es_points);
		_score = (CheckBox) findViewById(R.id.es_score);
	}
	
	// Function to add the information to be
	// passed to the next Activity.
	private void addExtras(Intent intent)
	{
		Bundle extras = getIntent().getExtras();
		
		// Load in the unused extras from the
		// previous Activity.
		intent.putExtras(extras);
		
		////////////////////////////////////////
		// Add optional standings categories. //
		////////////////////////////////////////
		if (_points.isChecked())
		{
			intent.putExtra("POINTS_ACTIVE", true);
		}
		else
		{
			intent.putExtra("POINTS_ACTIVE", false);
		}
		
		if (_score.isChecked())
		{
			intent.putExtra("SCORE_ACTIVE", true);
		}
		else
		{
			intent.putExtra("SCORE_ACTIVE", false);
		}
		/////////////////////////////////////////
	}
	
	// Function to handle the clicking of
	// the continue Button.
	private void setOnContinueClickListener()
	{
		_continue.setOnClickListener(new View.OnClickListener()
		{	
			public void onClick(View v)
			{
				Intent intent = new Intent("cody.mtmanager.com.OrderTiebreakers");
				
				addExtras(intent);
				startActivity(intent);
			}
		});
	}
}
