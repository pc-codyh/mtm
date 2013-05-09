package cody.mtmanager.com;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
	
	// Booleans indicating which optional rules
	// are enabled.
	boolean _pointsEnabled = true;
	boolean _scoreEnabled = true;
	
	// ArrayList holding the current positions
	// of each Spinner.
	ArrayList<Integer> _positions;
	
	// ArrayList holding the Spinners.
	ArrayList<Spinner> _spinners;
	
	// Continue Button.
	Button _continue;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.order_tiebreakers);
		
		initializeGlobalVariables();
		loadExtras();
		setDefaultTiebreakerOrder();
		setOnSpinnerSelectedItemChanged();
		setOnContinueClickListener();
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
		
		_continue = (Button) findViewById(R.id.ot_continue);
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
		
		_pointsEnabled = false;
	}
	
	// Disable (hide) the "Score" tiebreaker.
	private void disableScore()
	{
		// Arbitrarily select the sixth view
		// to be disabled.
		_sixTitle.setVisibility(View.INVISIBLE);
		_six.setVisibility(View.INVISIBLE);
		
		_scoreEnabled = false;
	}
	
	// Function that sets the default tiebreaker order.
	private void setDefaultTiebreakerOrder()
	{
		loadSpinners();
		
		// Set the default tiebreaker order for
		// the default tiebreakers.
		_one.setSelection(0);
		_two.setSelection(1);
		_three.setSelection(2);
		_four.setSelection(3);
		
		//////////////////////////////////////////
		// Set the default tiebreaker order for //
		// the optional tiebreakers.            //
		//////////////////////////////////////////
		if (_pointsEnabled)
		{
			_five.setSelection(4);
		}
		
		if (_scoreEnabled)
		{
			_six.setSelection(5);
		}
		//////////////////////////////////////////
	}
	
	// Function that loads the tiebreaker Spinners
	// with the possible tiebreaker choices.
	private void loadSpinners()
	{
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
		
		// Add the default tiebreaker choices
		// to the adapter.
		adapter.add("Wins");
		adapter.add("Losses");
		adapter.add("Overtime Losses");
		adapter.add("Head-to-Head");
		
		///////////////////////////////////////////////
		// Conditionally add the optional tiebreaker //
		// choices to the adapter.                   //
		///////////////////////////////////////////////
		if (_pointsEnabled)
		{
			adapter.add("Points");
		}
		
		if (_scoreEnabled)
		{
			adapter.add("Score");
		}
		///////////////////////////////////////////////
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Set the adapters for each Spinner.
		_one.setAdapter(adapter);
		_two.setAdapter(adapter);
		_three.setAdapter(adapter);
		_four.setAdapter(adapter);
		
		if (_pointsEnabled)
		{
			_five.setAdapter(adapter);
		}
		
		if (_scoreEnabled)
		{
			_six.setAdapter(adapter);
		}
	}
	
	// Function that handles the re-ordering
	// of the tiebreakers.
	private void setOnSpinnerSelectedItemChanged()
	{
		_spinners = new ArrayList<Spinner>();
		
		// Add all the spinners to an ArrayList for
		// ease of use. Order must be preserved here,
		// i.e. _one, _two, ... as this order is
		// depended on later.
		_spinners.add(_one);
		_spinners.add(_two);
		_spinners.add(_three);
		_spinners.add(_four);
		_spinners.add(_five);
		_spinners.add(_six);
		
		_positions = new ArrayList<Integer>();
		
		// Add the default positions to the array.
		for (int i = 0; i < _spinners.size(); i++)
		{
			_positions.add(i);
		}
		
		// Set up the callback upon selecting an item.
		for (final Spinner spinner : _spinners)
		{
			spinner.setOnItemSelectedListener(new OnItemSelectedListener()
			{
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
				{
					for (Spinner s : _spinners)
					{
						if ((s.getSelectedItemPosition() == position) && (s != spinner))
						{
							// Get the position previously held by the
							// Spinner being modified.
							int prevPos = getCurrentSpinnerPosition(spinner);
							
							// Change the selection of the Spinner that
							// is holding the value just selected.
							s.setSelection(prevPos);
							
							// Modify the positions array to reflect
							// these changes.
							_positions.set(getDefaultSpinnerPosition(spinner), position);
							_positions.set(getDefaultSpinnerPosition(s), prevPos);
						}
					}
				}

				public void onNothingSelected(AdapterView<?> parent)
				{
					// This method is not current used, but is
					// required by default.
				}
			});
		}
	}
	
	// Function to get the default position of a Spinner.
	private int getDefaultSpinnerPosition(Spinner s)
	{
		//////////////////////////////////////////
		// Return the default position based on //
		// the Spinner passed in.               //
		//////////////////////////////////////////
		if (s == _one)
		{
			return 0;
		}
		
		if (s == _two)
		{
			return 1;
		}
		
		if (s == _three)
		{
			return 2;
		}
		
		if (s == _four)
		{
			return 3;
		}
		
		if (s == _five)
		{
			return 4;
		}
		
		if (s == _six)
		{
			return 5;
		}
		//////////////////////////////////////////
		
		return -1;
	}
	
	// Function to get the current position of a Spinner.
	private int getCurrentSpinnerPosition(Spinner s)
	{
		//////////////////////////////////////////
		// Return the current position based on //
		// the Spinner passed in.               //
		//////////////////////////////////////////
		if (s == _one)
		{
			return _positions.get(0);
		}
		
		if (s == _two)
		{
			return _positions.get(1);
		}
		
		if (s == _three)
		{
			return _positions.get(2);
		}
		
		if (s == _four)
		{
			return _positions.get(3);
		}
		
		if (s == _five)
		{
			return _positions.get(4);
		}
		
		if (s == _six)
		{
			return _positions.get(5);
		}
		//////////////////////////////////////////
		
		return -1;
	}
	
	// Function to add the information to be
	// passed to the next Activity.
	private void addExtras(Intent intent)
	{
		int count = 0;
		
		Bundle extras = getIntent().getExtras();
		
		// Load in the unused extras from the
		// previous Activity.
		intent.putExtras(extras);
		
		// Add the tiebreaker order to the list
		// of extras.
		for (Spinner spinner : _spinners)
		{
			if (spinner.getVisibility() == View.VISIBLE)
			{
				intent.putExtra("TB_PRIORITY" + count, spinner.getSelectedItem().toString());				
			}
		}
	}
	
	// Function to handle the clicking of
	// the continue Button.
	private void setOnContinueClickListener()
	{
		_continue.setOnClickListener(new View.OnClickListener()
		{	
			public void onClick(View v)
			{
				Intent intent = new Intent("cody.mtmanager.com.ActiveTournament");
				
				addExtras(intent);
				startActivity(intent);
			}
		});
	}
}
