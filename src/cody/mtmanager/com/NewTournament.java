package cody.mtmanager.com;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
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
	
	// NumberPicker to choose the amount of groups
	// in a "Pool Play" tournament.
	NumberPicker _numGroups;
	
	// NumberPicker to choose the amount of prelim
	// games per player. Restricted based on how
	// many players are in the tournament.
	NumberPicker _numGroupGames;
	
	// ListViews that display the possible tournament
	// and playoff types.
	ExpandableListView _tournamentTypesList;
	ExpandableListView _playoffTypesList;
	
	// CheckBox to choose if there are playoffs
	// or not for this tournament.
	CheckBox _isPlayoffs;
	
	// Section titles.
	TextView _tournamentTypeTitle;
	TextView _playoffTypeTitle;
	TextView _numPlyrsPlayoffsTitle;
	TextView _numGroupsTitle;
	TextView _numPrelimGamesTitle;
	
	// Button to advance to the next screen.
	Button _continue;
	
	// The tournament type selected.
	int _tournamentTypeSelected = -1;
	
	// The playoff type selected.
	int _playoffTypeSelected = -1;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.new_tournament);
		
		initializeGlobalVariables();
		
		// Set the minimum number of groups and
		// prelim games in a pool play situation.
		_numGroupGames.setMinValue(1);
		_numGroups.setMinValue(1);
		
		setMinAndMaxPlayerValues();
		setPossibleTournamentTypes();
		setPlayoffContentVisibility(false);
		setGroupContentVisibility(false);
		setPrelimGamesVisibility(false);
		setOnCheckChangeListener(_isPlayoffs);
		setTournamentTypesOnClickListener(_tournamentTypesList, false);
		setTournamentTypesOnClickListener(_playoffTypesList, true);
		setOnContinueClickListener();
	}

	// Function to initialize all global variables.
	// This prevents cluttering in the onCreate method.
	private void initializeGlobalVariables()
	{
		_numPlyrs = (NumberPicker) findViewById(R.id.nd_num_plyrs_sel);
		_numPlyrsPlayoffs = (NumberPicker) findViewById(R.id.nd_playoffs_num_plyrs_sel);
		_numGroups = (NumberPicker) findViewById(R.id.nd_num_groups);
		_numGroupGames = (NumberPicker) findViewById(R.id.nd_group_num_games);
		
		_tournamentTypesList = (ExpandableListView) findViewById(R.id.nd_tournament_type_sel);
		_playoffTypesList = (ExpandableListView) findViewById(R.id.nd_playoffs_type_sel);
		
		_isPlayoffs = (CheckBox) findViewById(R.id.nd_playoffs_sel);
		
		_tournamentTypeTitle = (TextView) findViewById(R.id.nd_tournament_type);
		_playoffTypeTitle = (TextView) findViewById(R.id.nd_playoffs_type);
		_numPlyrsPlayoffsTitle = (TextView) findViewById(R.id.nd_playoffs_num_plyrs);
		_numGroupsTitle = (TextView) findViewById(R.id.nd_groups_title);
		_numPrelimGamesTitle = (TextView) findViewById(R.id.nd_group_num_games_title);
		
		_continue = (Button) findViewById(R.id.nd_continue);
	}
	
	// Function to set the minimum and maximum players
	// allowed for the prelims and the playoffs.
	private void setMinAndMaxPlayerValues()
	{
		_numPlyrs.setMinValue(Utility.MIN_PLAYERS);
		_numPlyrs.setMaxValue(Utility.MAX_PLAYERS);
		
		_numPlyrsPlayoffs.setMinValue(Utility.MIN_PLAYERS_PLAYOFFS);
		
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
				
				// Players can play at most everybody else
				// once, i.e. all players except themselves.
				_numGroupGames.setMaxValue((newVal - 1));
				
				// There must be at least two players
				// in each group.
				_numGroups.setMaxValue((newVal / 2));
			}
		});
	}
	
	// Function to load the ListViews with the possible
	// tournament and playoff types.
	private void setPossibleTournamentTypes()
	{
		_tournamentTypesList.setAdapter(new CustomExpandableListAdapter
		(this, Utility.TOURNAMENT_TYPES, Utility.TOURNAMENT_SUBTYPES_ELIM, Utility.TOURNAMENT_SUBTYPES_RR));
		
		_playoffTypesList.setAdapter(new CustomExpandableListAdapter
		(this, Utility.TOURNAMENT_TYPES, Utility.TOURNAMENT_SUBTYPES_ELIM, Utility.TOURNAMENT_SUBTYPES_RR));
		
		// Adjust the heights of the ListViews to counter
		// the effects of the ScrollView.
		Utility.setListViewHeightBasedOnChildren(_tournamentTypesList);
		Utility.setListViewHeightBasedOnChildren(_playoffTypesList);
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
	// prelim games NumberPicker.
	private void setPrelimGamesVisibility(boolean isVisible)
	{
		if (isVisible)
		{
			_numPrelimGamesTitle.setVisibility(View.VISIBLE);
			_numGroupGames.setVisibility(View.VISIBLE);
		}
		else
		{
			_numPrelimGamesTitle.setVisibility(View.INVISIBLE);
			_numGroupGames.setVisibility(View.INVISIBLE);
		}
	}
	
	// Function to toggle the visibility of the
	// playoff content.
	private void setPlayoffContentVisibility(boolean isVisible)
	{
		if (isVisible)
		{
			_playoffTypeTitle.setVisibility(View.VISIBLE);
			_numPlyrsPlayoffsTitle.setVisibility(View.VISIBLE);
			_playoffTypesList.setVisibility(View.VISIBLE);
			_numPlyrsPlayoffs.setVisibility(View.VISIBLE);
		}
		else
		{
			_playoffTypeTitle.setVisibility(View.INVISIBLE);
			_numPlyrsPlayoffsTitle.setVisibility(View.INVISIBLE);
			_playoffTypesList.setVisibility(View.INVISIBLE);
			_numPlyrsPlayoffs.setVisibility(View.INVISIBLE);
		}
	}
	
	// Function to toggle the visibility of the
	// group information based on the selection
	// of the tournament type.
	private void setGroupContentVisibility(boolean isVisible)
	{
		if (isVisible)
		{
			_numGroupsTitle.setVisibility(View.VISIBLE);
			_numGroups.setVisibility(View.VISIBLE);
		}
		else
		{
			_numGroupsTitle.setVisibility(View.INVISIBLE);
			_numGroups.setVisibility(View.INVISIBLE);
		}
	}
	
	// Function to handle clicking an item
	// in the tournament ExpandableListView.
	private void setTournamentTypesOnClickListener(ExpandableListView listView, final boolean isPlayoffs)
	{
		listView.setOnChildClickListener(new OnChildClickListener()
		{
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
			{
				String selectedItem = CustomExpandableListAdapter._children[groupPosition][childPosition];
				
				handleSelectedItem(selectedItem, isPlayoffs);
				
				return false;
			}
		});
	}
	
	// Function to handle the selected item
	// in the ExpandableListView.
	private void handleSelectedItem(String selectedItem, boolean isPlayoffs)
	{
		int type = -1;
		
		// The chosen tournament style is "Pool Play"
		// and the group information should be shown.
		if (selectedItem.equalsIgnoreCase("Pool Play"))
		{
			if (!isPlayoffs)
			{
				setGroupContentVisibility(true);
			}
			
			type = Utility.POOL_PLAY;
		}
		// The chosen tournament style is not "Pool Play"
		// and the group information should be hidden.
		else
		{
			if (!isPlayoffs)
			{
				setGroupContentVisibility(false);
			}
			
			if (selectedItem.equalsIgnoreCase("Single Elimination"))
			{
				type = Utility.SINGLE_ELIM;
			}
			else if (selectedItem.equalsIgnoreCase("Double Elimination"))
			{
				type = Utility.DOUBLE_ELIM;
			}
		}
		
		if (isPlayoffs)
		{
			_playoffTypeSelected = type;
			
			setTournamentTypeTitle(_playoffTypeSelected, true);
		}
		else
		{
			_tournamentTypeSelected = type;
			
			setTournamentTypeTitle(_tournamentTypeSelected, false);
		}
	}
	
	// Function to modify the displayed tournament
	// type title.
	private void setTournamentTypeTitle(int type, boolean isPlayoffs)
	{
		TextView tv = null;
		String str = null;
		
		// Select the appropriate title to modify.
		if (isPlayoffs)
		{
			tv = _playoffTypeTitle;
			str = "Playoff Type: ";
		}
		else
		{
			tv = _tournamentTypeTitle;
			str = "Tournament Type: ";
		}
		
		// Select the appropriate title.
		switch (type)
		{
			case Utility.POOL_PLAY:
			{
				str += "Pool Play";
			}
				break;
				
			case Utility.SINGLE_ELIM:
			{
				str += "Single Elimination";
			}
				break;
				
			case Utility.DOUBLE_ELIM:
			{
				str += "Double Elimination";
			}
				break;
				
			default:
				break;
		}

		tv.setText(str);
		tv.setTextColor(Color.RED);
	}
	
	// Function to check whether the information
	// entered in the fields is valid.
	private boolean allFieldsValid()
	{
		// NEEDS TO BE COMPLETED.
		
		return true;
	}
	
	// Function to encapsulate adding content to
	// be passed to the next Activity.
	private void addExtras(Intent intent)
	{
		intent.putExtra("NUM_PLAYERS", _numPlyrs.getValue());
		intent.putExtra("NUM_GROUPS", _numGroups.getValue());
		intent.putExtra("NUM_PRELIM_GAMES", _numGroupGames.getValue());
		intent.putExtra("NUM_PLAYERS_IN_PLAYOFFS", _numPlyrsPlayoffs.getValue());
		intent.putExtra("TOURNAMENT_TYPE", _tournamentTypeSelected);
		intent.putExtra("PLAYOFF_TYPE", _playoffTypeSelected);
	}
	
	// Function to handle pressing the "Continue"
	// button to begin the tournament.
	private void setOnContinueClickListener()
	{
		_continue.setOnClickListener(new View.OnClickListener()
		{	
			public void onClick(View v)
			{
				// Check to make sure all fields are valid
				// before beginning the tournament.
				if (allFieldsValid())
				{
					Intent intent = new Intent("cody.mtmanager.com.PlayerSetup");

					addExtras(intent);
					startActivity(intent);
				}
			}
		});
	}
}
