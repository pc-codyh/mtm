package cody.mtmanager.com;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class PlayerSetup extends Activity
{
	// Holds all the rows to enter the teams
	// and/or players in the tournament.
	TableLayout _participants;
	
	// Number of players in the tournament.
	int _numPlyrs;
	
	// Array to hold all the rows in the table.
	ArrayList<EditText> _names;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.player_setup);
		
		initializeGlobalVariables();
		loadExtras();
		createParticipants();
	}
	
	// Function to initialize all global variables.
	// This prevents cluttering in the onCreate method.
	private void initializeGlobalVariables()
	{
		_participants = (TableLayout) findViewById(R.id.ps_table);
	}
	
	// Function to load the extras passed in
	// from the caller.
	private void loadExtras()
	{
		Bundle extras = getIntent().getExtras();
		
		_numPlyrs = extras.getInt("NUM_PLAYERS");
	}
	
	// Function to create a table allowing the user
	// to enter the names of the players/teams
	// participating in the tournament.
	private void createParticipants()
	{
		_names = new ArrayList<EditText>(_numPlyrs);
		
		// Create a row for each participant.
		for (int i = 0; i < _numPlyrs; i++)
		{
			TableRow row = new TableRow(getApplicationContext());
			EditText name = new EditText(getApplicationContext());
			
			// Modify the height and width of
			// the row and text field.
			row.setLayoutParams(Utility.DEFAULT_ROW_PARAMS);
			name.setLayoutParams(Utility.DEFAULT_ROW_PARAMS);
			
			// Set a default placeholder name.
			name.setText("Participant " + i);
			
			// Add the views to the table.
			row.addView(name);
			_participants.addView(row);
			
			_names.add(name);
		}
		
		// Create a button to continue to the next screen.
		Button advance = new Button(getApplicationContext());
		
		// Modify the appearance of the continue button
		// add handle its click event.
		advance.setLayoutParams(Utility.DEFAULT_ROW_PARAMS);
		advance.setText("Continue");
		
		setOnAdvanceClickListener(advance);
		
		_participants.addView(advance);
	}
	
	// Function to add the information to be
	// passed to the next Activity.
	private void addExtras(Intent intent)
	{
		int count = 0;
		
		// Add each participant to the list of
		// parameters being passed to the next
		// Activity.
		for (EditText name : _names)
		{
			intent.putExtra("PARTICIPANT" + count, name.getText().toString());
			
			count ++;
		}
		
		// Pass the number of players to the
		// next Activity.
		intent.putExtra("NUM_PLAYERS", _numPlyrs);
	}
	
	// Function to handle the clicking of the
	// continue button.
	private void setOnAdvanceClickListener(Button btn)
	{
		btn.setOnClickListener(new View.OnClickListener()
		{	
			public void onClick(View arg0)
			{
				Intent intent = new Intent("cody.mtmanager.com.ExtraStats");
				
				addExtras(intent);
				startActivity(intent);
			}
		});
	}
}
