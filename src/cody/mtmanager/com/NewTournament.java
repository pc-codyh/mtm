package cody.mtmanager.com;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

public class NewTournament extends Activity
{
	// NumberPicker to choose the amount of players
	// in the tournament.
	NumberPicker _numPlyrs;
	
	// NumberPicker to choose the amount of players
	// in the playoffs.
	NumberPicker _numPlyrsPlayoffs;
	
	// ListViews that display the possible tournament
	// and playoff types.
	ExpandableListView _tournamentTypesList;
	ExpandableListView _playoffTypesList;
	
	// CheckBox to choose if there are playoffs
	// or not for this tournament.
	CheckBox _isPlayoffs;
	
	// Section titles.
	TextView _playoffsTitle;
	TextView _numPlyrsPlayoffsTitle;
	
	// Button to advance to the next screen.
	Button _continue;
	
	Utility _util;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.new_tournament);
		
		initializeGlobalVariables();
		
		setMinAndMaxPlayerValues();
		setPossibleTournamentTypes();
		setPlayoffContentVisibility(false);
		setOnCheckChangeListener(_isPlayoffs);
	}
	
	// Function to initialize all global variables.
	// This prevents cluttering in the onCreate method.
	private void initializeGlobalVariables()
	{
		_util = new Utility();
		
		_numPlyrs = (NumberPicker) findViewById(R.id.nd_num_plyrs_sel);
		_numPlyrsPlayoffs = (NumberPicker) findViewById(R.id.nd_playoffs_num_plyrs_sel);
		
		_tournamentTypesList = (ExpandableListView) findViewById(R.id.nd_draft_type_sel);
		_playoffTypesList = (ExpandableListView) findViewById(R.id.nd_playoffs_type_sel);
		
		_isPlayoffs = (CheckBox) findViewById(R.id.nd_playoffs_sel);
		
		_playoffsTitle = (TextView) findViewById(R.id.nd_playoffs_type);
		_numPlyrsPlayoffsTitle = (TextView) findViewById(R.id.nd_playoffs_num_plyrs);
	}
	
	// Function to set the minimum and maximum players
	// allowed for the prelims and the playoffs.
	private void setMinAndMaxPlayerValues()
	{
		_numPlyrs.setMinValue(_util.MIN_PLAYERS);
		_numPlyrs.setMaxValue(_util.MAX_PLAYERS);
		
		_numPlyrsPlayoffs.setMinValue(_util.MIN_PLAYERS_PLAYOFFS);
		
		// This value needs to be dynamic depending
		// on the value chosen for the number of players
		// in the prelims.
		_numPlyrsPlayoffs.setMaxValue(_numPlyrs.getValue());
		
		setOnValueChangeListener(_numPlyrs);
	}
	
	// Function that is called when the number of
	// players in the prelims changes. This function
	// updates the new max players for the playoffs.
	private void setOnValueChangeListener(NumberPicker np)
	{
		np.setOnValueChangedListener(new OnValueChangeListener()
		{
			public void onValueChange(NumberPicker picker, int oldVal, int newVal)
			{
				_numPlyrsPlayoffs.setMaxValue(newVal);
			}
		});
	}
	
	// Function to load the ListViews with the possible
	// tournament and playoff types.
	private void setPossibleTournamentTypes()
	{
		_tournamentTypesList.setAdapter(new CustomExpandableListAdapter
		(this, _util.TOURNAMENT_TYPES, _util.TOURNAMENT_SUBTYPES_ELIM, _util.TOURNAMENT_SUBTYPES_RR));
		
		_playoffTypesList.setAdapter(new CustomExpandableListAdapter
		(this, _util.TOURNAMENT_TYPES, _util.TOURNAMENT_SUBTYPES_ELIM, _util.TOURNAMENT_SUBTYPES_RR));
		
		// Adjust the heights of the ListViews to counter
		// the effects of the ScrollView.
		_util.setListViewHeightBasedOnChildren(_tournamentTypesList);
		_util.setListViewHeightBasedOnChildren(_playoffTypesList);
	}
	
	// Function that is called when the value of the
	// playoffs CheckBox is changed. Checking this
	// CheckBox will enable the visibility of the
	// remaining playoff content and vice versa.
	private void setOnCheckChangeListener(CheckBox cb)
	{
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton checkBox, boolean isChecked)
			{
				setPlayoffContentVisibility(isChecked);
			}	
		});
	}
	
	// Function to toggle the visibility of the
	// playoff content.
	private void setPlayoffContentVisibility(boolean isVisible)
	{
		if (isVisible)
		{
			_playoffsTitle.setVisibility(View.VISIBLE);
			_numPlyrsPlayoffsTitle.setVisibility(View.VISIBLE);
			_playoffTypesList.setVisibility(View.VISIBLE);
			_numPlyrsPlayoffs.setVisibility(View.VISIBLE);
		}
		else
		{
			_playoffsTitle.setVisibility(View.INVISIBLE);
			_numPlyrsPlayoffsTitle.setVisibility(View.INVISIBLE);
			_playoffTypesList.setVisibility(View.INVISIBLE);
			_numPlyrsPlayoffs.setVisibility(View.INVISIBLE);
		}
	}
}
