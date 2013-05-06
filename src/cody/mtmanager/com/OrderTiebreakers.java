package cody.mtmanager.com;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;


public class OrderTiebreakers extends Activity
{
	// Tiebreaker section titles.
	TextView _oneTitle;
	TextView _twoTitle;
	TextView _threeTitle;
	TextView _fourTitle;
	TextView _fiveTitle;
	TextView _sixTitle;
	
	// Tiebreaker Spinners.
	Spinner _one;
	Spinner _two;
	Spinner _three;
	Spinner _four;
	Spinner _five;
	Spinner _six;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.order_tiebreakers);
		
		initializeGlobalVariables();
		loadExtras();
	}
	
	// Function to initialize all global variables.
	// This prevents cluttering in the onCreate method.
	private void initializeGlobalVariables()
	{
		_oneTitle = (TextView) findViewById(R.id.ot_one_title);
		_twoTitle = (TextView) findViewById(R.id.ot_two_title);
		_threeTitle = (TextView) findViewById(R.id.ot_three_title);
		_fourTitle = (TextView) findViewById(R.id.ot_four_title);
		_fiveTitle = (TextView) findViewById(R.id.ot_five_title);
		_sixTitle = (TextView) findViewById(R.id.ot_six_title);
		
		_one = (Spinner) findViewById(R.id.ot_one);
		_two = (Spinner) findViewById(R.id.ot_two);
		_three = (Spinner) findViewById(R.id.ot_three);
		_four = (Spinner) findViewById(R.id.ot_four);
		_five = (Spinner) findViewById(R.id.ot_five);
		_six = (Spinner) findViewById(R.id.ot_six);
	}
	
	// Function to load the extras passed in
	// from the caller.
	private void loadExtras()
	{
		Bundle extras = getIntent().getExtras();
		
		//////////////////////////////////////////////////
		// Conditionally disable the extra tiebreakers. //
		//////////////////////////////////////////////////
		if (!extras.getBoolean("POINTS_ACTIVE"))
		{
			disablePoints();
		}
		
		if (!extras.getBoolean("SCORE_ACTIVE"))
		{
			disableScore();
		}
		///////////////////////////////////////////////////
	}
	
	// Disable (hide) the "Points" tiebreaker.
	private void disablePoints()
	{
		// Arbitrarily select the fifth view
		// to be disabled.
		_fiveTitle.setVisibility(View.INVISIBLE);
		_five.setVisibility(View.INVISIBLE);
	}
	
	// Disable (hide) the "Score" tiebreaker.
	private void disableScore()
	{
		// Arbitrarily select the sixth view
		// to be disabled.
		_sixTitle.setVisibility(View.INVISIBLE);
		_six.setVisibility(View.INVISIBLE);
	}
}
