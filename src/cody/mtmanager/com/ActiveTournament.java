package cody.mtmanager.com;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ActiveTournament extends Activity
{
	// Number of players in the tournament.
	int _numPlyrs;
	
	// Standings Table.
	TableLayout _standings;
	
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
	}
	
	// Function to load the extras passed in
	// from the caller.
	private void loadExtras()
	{
		Bundle extras = getIntent().getExtras();
		
		_numPlyrs = extras.getInt("NUM_PLAYERS");
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
				wins.setText("Wins");
				losses.setText("Losses");
				otlosses.setText("OT Losses");
				
				name.setTextColor(Color.RED);
				wins.setTextColor(Color.RED);
				losses.setTextColor(Color.RED);
				otlosses.setTextColor(Color.RED);
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
}
