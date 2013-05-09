package cody.mtmanager.com;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ActiveTournament extends Activity
{
	// Number of players in the tournament.
	int _numPlyrs;
	
	// Standings Table.
	TableLayout _standings;
	
	// Schedule Table.
	TableLayout _schedule;
	
	// Help text.
	TextView _help;
	
	// ArrayList holding the Views selectable
	// in the Options Menu.
	ArrayList<View> _views;
	
	// Tournament participants.
	ArrayList<String> _participants;
	
	// Default Animation.
	Animation _defaultAnimation;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.active_tournament);
		
		initializeGlobalVariables();
		
		loadExtras();
		createStandings();
	}
	
	// Function to initialize all global variables.
	// This prevents cluttering in the onCreate method.
	private void initializeGlobalVariables()
	{
		_standings = (TableLayout) findViewById(R.id.at_standings);
		_schedule = (TableLayout) findViewById(R.id.at_schedule);
		_help = (TextView) findViewById(R.id.at_help);
		
		_views = new ArrayList<View>();
		
		_views.add(_standings);
		_views.add(_schedule);
		_views.add(_help);
		
		// Create the default View Animation.
		_defaultAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
												   Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		_defaultAnimation.setDuration(Utility.ANIMATION_DURATION);
		
		// Set the standings as the default View.
		bringViewToFront(_standings, false);
	}
	
	// Function to load the extras passed in
	// from the caller.
	private void loadExtras()
	{
		Bundle extras = getIntent().getExtras();
		
		_numPlyrs = extras.getInt("NUM_PLAYERS");
		
		_participants = new ArrayList<String>(_numPlyrs);
		
		// Add each participant from the Bundle
		// to the tournament participants array.
		for (int i = 0; i < _numPlyrs; i++)
		{
			_participants.add(extras.getString("PARTICIPANT" + i));
		}
	}
	
	// Function to create a TableLayout to display
	// the standings of the tournament.
	private void createStandings()
	{
		// Create a new row for each player
		// in the tournament plus one extra
		// for the column titles.
		for (int i = 0; i <= _numPlyrs; i++)
		{
			TableRow row = new TableRow(getApplicationContext());
			TextView name = new TextView(getApplicationContext());
			TextView wins = new TextView(getApplicationContext());
			TextView losses = new TextView(getApplicationContext());
			TextView otlosses = new TextView(getApplicationContext());
			
			// Modify the column titles and colors
			// for the first row. 
			if (i == 0)
			{
				name.setText("Player / Team");
				wins.setText("W");
				losses.setText("L");
				otlosses.setText("OTL");
				
				name.setTextColor(Color.RED);
				wins.setTextColor(Color.RED);
				losses.setTextColor(Color.RED);
				otlosses.setTextColor(Color.RED);
			}
			else
			{
				// Set the name equal to the (i - 1)th
				// participant since we added an extra
				// row for column titles.
				name.setText(_participants.get(i - 1));
				
				// Default the stats columns to "0".
				wins.setText("0");
				losses.setText("0");
				otlosses.setText("0");
			}
			
			// Modify the width, height, and weight
			// of the content in the row.
			name.setLayoutParams(Utility.NAME_ROW_PARAMS);
			wins.setLayoutParams(Utility.STAT_ROW_PARAMS);
			losses.setLayoutParams(Utility.STAT_ROW_PARAMS);
			otlosses.setLayoutParams(Utility.STAT_ROW_PARAMS);
			
			// Modify the width and height of the row
			// and add the TextViews to it.
			row.setLayoutParams(Utility.DEFAULT_ROW_PARAMS);
			row.addView(name);
			row.addView(wins);
			row.addView(losses);
			row.addView(otlosses);
			
			// Finally, add the row to the TableLayout.
			_standings.addView(row);
		}
	}
	
	//////////////////
	// OPTIONS MENU //
	//////////////////
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.options_menu, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_standings:
			{
				bringViewToFront(_standings, true);
			
				return true;
			}
			
			case R.id.menu_playoffs:
			{
				return true;
			}
			
			case R.id.menu_schedule:
			{
				bringViewToFront(_schedule, true);
				
				return true;
			}
			
			case R.id.menu_save:
			{
				return true;
			}
			
			case R.id.menu_help:
			{
				bringViewToFront(_help, true);
				
				return true;
			}
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	// Function to bring the chosen View
	// to the front.
	private void bringViewToFront(View view, boolean isAnimated)
	{
		if (isAnimated)
		{
			AnimationSet set = new AnimationSet(true);
			
			set.addAnimation(_defaultAnimation);
			
			view.startAnimation(_defaultAnimation);
		}
		
		// Enable the View passed in.
		view.setVisibility(View.VISIBLE);
		
		// Disable the remaining Views.
		for (View v : _views)
		{
			if (v != view)
			{
				v.setVisibility(View.INVISIBLE);
			}
		}
	}
}
