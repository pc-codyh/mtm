package cody.mtmanager.com;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("FloatMath")
public class ActiveTournament extends Activity
{
	// Number of players in the tournament.
	int _numPlyrs;
	
	// Standings Table.
	TableLayout _standings;
	
	// Record a game.
	TableLayout _recordGame;
	
	// Schedule (shows game results).
	ListView _schedule;
	
	// Help text.
	TextView _help;
	
	// ArrayList holding the Views selectable
	// in the Options Menu.
	ArrayList<View> _views;
	
	// Tournament participants (just the names).
	ArrayList<String> _participants;
	
	// Tournament players (all the information).
	ArrayList<Player> _players;
	
	// Tournament groups (used with auto schedule).
	Player[][] _groups;
	
	// Record Game button and background.
	Button _saveGame;
	LinearLayout _recordGameBackground;
	
	// Group information.
	int _numGroups;
	int _numGames;
	int _playersPerGroup;
	
	// ArrayList of header rows.
	ArrayList<TableRow> _headers = new ArrayList<TableRow>();
	
	// Default Animation.
	Animation _defaultAnimation;
	
	// ArrayList to hold results
	// of each game to be shown.
	List<Map<String, String>> _gameResults;
	
	// Two-dimensional Array to hold
	// the result of each game in detail.
	ArrayList<String[]> _results;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.active_tournament);
		
		loadExtras();
		initializeGlobalVariables();
		setOnRecordGameClickListener();
		
		generatePlayers();
		generateGroups();
		createStandings();
		createScheduleView();
		
		// Hide the Record Game button initially since
		// the Standings are shown by default.
		hideSaveGame();
	}
	
	// Function to initialize all global variables.
	// This prevents cluttering in the onCreate method.
	@SuppressLint("FloatMath")
	private void initializeGlobalVariables()
	{
		_standings = (TableLayout) findViewById(R.id.at_standings);
		_schedule = (ListView) findViewById(R.id.at_schedule);
		_recordGame = (TableLayout) findViewById(R.id.at_record_game);
		_help = (TextView) findViewById(R.id.at_help);
		
		_saveGame = (Button) findViewById(R.id.at_save_game);
		_recordGameBackground = (LinearLayout) findViewById(R.id.at_record_game_background);
		
		_views = new ArrayList<View>();
		
		_views.add(_standings);
		_views.add(_schedule);
		_views.add(_recordGame);
		_views.add(_help);
		
		_numGroups = loadExtraInt("NUM_GROUPS");
		_numGames = loadExtraInt("NUM_PRELIM_GAMES");
		_playersPerGroup = (int) Math.ceil((float) _numPlyrs / (float) _numGroups);
		
		// Create the default View Animation.
		_defaultAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
												   Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		_defaultAnimation.setDuration(Utility.ANIMATION_DURATION);
		
		// Set the standings as the default View.
		bringViewToFront(_standings, false);
	}
	
	// Function to handle the click event for
	// recording (entering) a game.
	private void setOnRecordGameClickListener()
	{
		_saveGame.setOnClickListener(new View.OnClickListener()
		{	
			public void onClick(View v)
			{
				final View view = getLayoutInflater().inflate(R.layout.record_game, _recordGame, false);
				final Spinner pOne;
				final Spinner pTwo;
				final EditText sOne;
				final EditText sTwo;
				final CheckBox ot;
				Button submitGame;
				
				// Add the inflated view to the screen and
				// hide the Record Game button.
				_recordGame.addView(view);
				hideSaveGame();
				
				submitGame = (Button) findViewById(R.id.rg_submit);
				pOne = (Spinner) findViewById(R.id.rg_player_one);
				pTwo = (Spinner) findViewById(R.id.rg_player_two);
				sOne = (EditText) findViewById(R.id.rg_score_one);
				sTwo = (EditText) findViewById(R.id.rg_score_two);
				ot = (CheckBox) findViewById(R.id.rg_overtime);
				
				// Set up the two adapters to hold all the players.
				ArrayAdapter<String> adapter = new ArrayAdapter<String>
											   (getApplicationContext(), android.R.layout.simple_spinner_item);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				
				for (Player player : _players)
				{
					adapter.add(player.getName());
				}
				
				pOne.setAdapter(adapter);
				pTwo.setAdapter(adapter);
				
				// Set up the click event for entering
				// a recorded game.
				submitGame.setOnClickListener(new View.OnClickListener()
				{	
					public void onClick(View v)
					{
						Player playerOne;
						Player playerTwo;
						int playerOneScore;
						int playerTwoScore;
						boolean overtime;
						
						// Check if the players are the same.
						// Do not allow a player to play himself.
						if (pOne.getSelectedItem().toString().equals(pTwo.getSelectedItem().toString()))
						{
							Utility.displayShortMessage("Players are the same!", getApplicationContext());
							
							return;
						}
						
						// Currently, MTM does not support tie games.
						// Bail if the scores are the same.
						if (sOne.getText().toString().equals(sTwo.getText().toString()))
						{
							Utility.displayShortMessage("Scores are the same!", getApplicationContext());
							
							return;
						}

						playerOne = _players.get(pOne.getSelectedItemPosition());
						playerTwo = _players.get(pTwo.getSelectedItemPosition());
						
						playerOneScore = Integer.parseInt(sOne.getText().toString());
						playerTwoScore = Integer.parseInt(sTwo.getText().toString());
						
						overtime = ot.isChecked();
						
						///////////////////////
						// POSSIBLE RESULTS: //
						// 2: Win            //
						// 1: OT Loss		 //
						// 0: Loss			 //
						///////////////////////
						
						// Player one wins.
						if (playerOneScore > playerTwoScore)
						{
							playerOne.addResult(playerTwo.getId(), 2, (playerOneScore - playerTwoScore));
							
							// The game went into overtime.
							if (overtime)
							{
								playerTwo.addResult(playerOne.getId(), 1, (playerTwoScore - playerOneScore));
							}
							// No overtime.
							else
							{
								playerTwo.addResult(playerOne.getId(), 0, (playerTwoScore - playerOneScore));
							}
						}
						// Player two wins.
						else
						{
							playerTwo.addResult(playerOne.getId(), 2, (playerTwoScore - playerOneScore));
							
							// The game went into overtime.
							if (overtime)
							{
								playerOne.addResult(playerTwo.getId(), 1, (playerOneScore - playerTwoScore));
							}
							// No overtime.
							else
							{
								playerOne.addResult(playerTwo.getId(), 0, (playerOneScore - playerTwoScore));
							}
						}
						
						// If we've reached here, then the game
						// has been recorded successfully.
						addNewResult(playerOne, playerTwo, playerOneScore, playerTwoScore, overtime);
						updateScheduleView();
						
						Utility.displayShortMessage("Game recorded successfully!", getApplicationContext());
						
						showSaveGame();
						sortStandings();
						
						_recordGame.removeView(view);
					}
				});
			}
		});
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
	
	// Function to return a boolean from the Bundle
	// of extras with the key "key".
	private boolean loadExtraBoolean(String key)
	{
		Bundle extras = getIntent().getExtras();
		
		return extras.getBoolean(key);
	}
	
	// Function to return an integer from the Bundle
	// of extras with the key "key".
	private int loadExtraInt(String key)
	{
		Bundle extras = getIntent().getExtras();
		
		return extras.getInt(key);
	}
	
	// Function to create a TableLayout to display
	// the standings of the tournament.
	private void createStandings()
	{
		// Round Robin.
		if (loadExtraInt("TOURNAMENT_TYPE") == 0)
		{
			Player[] group;
			Player player;
			boolean shouldDisplay = false;
			
			// Display the standings group by group.
			for (int i = 0; i < _numGroups; i++)
			{
				// Choose the current group.
				group = _groups[i];
				
				// Choose each player in the current group.
				for (int j = 0; j <= _playersPerGroup; j++)
				{
					TableRow row = new TableRow(getApplicationContext());
					TextView name = new TextView(getApplicationContext());
					TextView wins = new TextView(getApplicationContext());
					TextView losses = new TextView(getApplicationContext());
					TextView otLosses = new TextView(getApplicationContext());
					TextView points = new TextView(getApplicationContext());
					TextView score = new TextView(getApplicationContext());
					
					shouldDisplay = false;
					
					// Column headers for the first row.
					if (j == 0)
					{
						name.setText("Group " + (i + 1));
						wins.setText("W");
						losses.setText("L");
						otLosses.setText("OTL");
						points.setText("PTS");
						score.setText("+/-");
						
						name.setTextColor(Color.RED);
						wins.setTextColor(Color.RED);
						losses.setTextColor(Color.RED);
						otLosses.setTextColor(Color.RED);
						points.setTextColor(Color.RED);
						score.setTextColor(Color.RED);
						
						shouldDisplay = true;
					}
					// Player information.
					else
					{
						player = group[j - 1];
						
						if (player != null)
						{
							// Set the name equal to the relevant player.
							name.setText(player.getName());
							
							// Default the stats columns to "0".
							wins.setText("0");
							losses.setText("0");
							otLosses.setText("0");
							points.setText("0");
							score.setText("0");
							
							player.addStandingsReference(name, wins, losses, otLosses, points, score, row);
							
							shouldDisplay = true;
						}
					}
					
					// Modify the width, height, and weight
					// of the content in the row.
					name.setLayoutParams(Utility.NAME_ROW_PARAMS);
					wins.setLayoutParams(Utility.STAT_ROW_PARAMS);
					losses.setLayoutParams(Utility.STAT_ROW_PARAMS);
					otLosses.setLayoutParams(Utility.STAT_ROW_PARAMS);
					points.setLayoutParams(Utility.STAT_ROW_PARAMS);
					score.setLayoutParams(Utility.STAT_ROW_PARAMS);
					
					if (shouldDisplay)
					{
						// Modify the width and height of the row
						// and add the TextViews to it.
						row.setLayoutParams(Utility.DEFAULT_ROW_PARAMS);
						row.addView(name);
						row.addView(wins);
						row.addView(losses);
						row.addView(otLosses);
						row.addView(points);
						row.addView(score);
						
						// Finally, add the row to the TableLayout.
						_standings.addView(row);
						
						// Add the row to the list of headers
						// so we can access it when we sort
						// the standings.
						if (j == 0)
						{
							_headers.add(row);
						}
					}
				}
			}
		}
		// Elimination.
		else
		{
			// NEEDS TO BE COMPLETED.
		}
	}
	
	// Function to initialize all the players.
	private void generatePlayers()
	{
		_players = new ArrayList<Player>();
		
		int count = 0;
		
		// Generates the player array (size of player count).
		for (String participant : _participants)
		{
			_players.add(new Player(count, participant, _numPlyrs));
			
			count++;
		}
	}
	
	// Function to automatically generate a schedule
	// if the user has chosen to do so.
	private void generateGroups()
	{
		// If Round Robin.
		if (loadExtraInt("TOURNAMENT_TYPE") == 0)
		{
			// Returned true, so auto-generate a schedule.
			hideSaveGame();
			
			// Adjust the top margin of the schedule.
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) _recordGame.getLayoutParams();
			
			params.setMargins(0, 0, 0, 0);
			_recordGame.setLayoutParams(params);
			
			// Gather the relevant data for assembling the schedule.
			Player[] group;
			int groupId;
			
			// Shuffle the players.
			Collections.shuffle(_players);
			
			_groups = new Player[_numGroups][_playersPerGroup];
			
			// Assign the players to groups.
			for (int i = 0; i < _numPlyrs; i++)
			{
				groupId = (i % _numGroups);
				
				group = _groups[groupId];
				
				group[(i / _numGroups)] = _players.get(i);
			}
		}
		// Elimination.
		else
		{
			// NEEDS TO BE COMPLETED.
		}
	}
	
	// Function to hide the appearance of the
	// Record Game button.
	private void hideSaveGame()
	{
		_recordGameBackground.setVisibility(View.INVISIBLE);
	}
	
	// Function to show the appearance of the
	// Record Game button.
	private void showSaveGame()
	{
		_recordGameBackground.setVisibility(View.VISIBLE);
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
				hideSaveGame();
			
				return true;
			}
			
			case R.id.menu_schedule:
			{
				bringViewToFront(_schedule, true);
				hideSaveGame();
				
				return true;
			}
			
			case R.id.menu_playoffs:
			{
				return true;
			}
			
			case R.id.menu_record_game:
			{
				bringViewToFront(_recordGame, true);
				showSaveGame();
				
				return true;
			}
			
			case R.id.menu_sync:
			{
				return true;
			}
			
			case R.id.menu_help:
			{
				bringViewToFront(_help, true);
				hideSaveGame();
				
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
	
	// Function to sort the standings.
	// This is where tiebreakers come into play.
	private void sortStandings()
	{
		// Candidate standings list. Standings are
		// sorted on a group basis.
		ArrayList<Player> candidates = new ArrayList<Player>();
		
		// Candidate to be added to the standings
		// list next.
		ArrayList<Player> candidate = new ArrayList<Player>();
		
		// Players left to be sorted overall.
		ArrayList<Player> playersLeft = new ArrayList<Player>();
		
		Player[] group;
		int maxPoints = -1;
		int count = 0;
		
		// Remove any legacy tiebreaker indications.
		for (Player player : _players)
		{
			player.removeTiebreaker();
		}
		
		// Clear the standings table and re-add the
		// rows in sorted order.
		_standings.removeAllViews();
		
		for (int i = 0; i < _numGroups; i++)
		{
			candidates.clear();
			
			// Grab the appropriate group.
			group = _groups[i];
			
			for (int j = 0; j < _playersPerGroup; j++)
			{
				// Make a list of non-null players.
				if (group[j] != null)
				{
					playersLeft.add(group[j]);
				}
			}
			
			// Continue until all players have been sorted.
			while (!playersLeft.isEmpty())
			{
				candidate.clear();
				
				maxPoints = -1;
				count = 0;
				
				for (Player player : playersLeft)
				{
					// A player has more points than the field.
					if (player.getPoints() > maxPoints)
					{
						maxPoints = player.getPoints();
						
						candidate.clear();
						candidate.add(player);
						
						count = 1;
					}
					// A player is tied for the most points in the field.
					else if (player.getPoints() == maxPoints)
					{
						candidate.add(player);
						
						count++;
					}
				}
				
				// Need to sort based on wins.
				if (count > 1)
				{
					candidate = sortWins(candidate);
				}
				
				// Add the players to the list
				// of sorted candidates.
				for (Player player : candidate)
				{
					candidates.add(player);
					playersLeft.remove(player);
				}
			}
			
			_standings.addView(_headers.get(i));
			
			for (Player player : candidates)
			{
				_standings.addView(player.getStandingsRow());
			}
		}
	}
	
	// Function to further sort the standings based on wins.
	private ArrayList<Player> sortWins(ArrayList<Player> candidate)
	{
		// New sub-candidate list.
		ArrayList<Player> subCandidate = new ArrayList<Player>();
		
		// New sub-candidates list.
		ArrayList<Player> subCandidates = new ArrayList<Player>();
		
		int maxWins = -1;
		int count = 0;
		
		// Continue sorting until all the candidates
		// have been sorted.
		while (!candidate.isEmpty())
		{
			subCandidate.clear();
			
			maxWins = -1;
			count = 0;
			
			for (Player player : candidate)
			{
				// Player leads the field in wins.
				if (player.getWins() > maxWins)
				{
					maxWins = player.getWins();
					
					subCandidate.clear();
					subCandidate.add(player);
					
					count = 1;
				}
				// Player is tied for the lead in wins.
				else if (player.getWins() == maxWins)
				{
					subCandidate.add(player);
					
					count++;
				}
			}
			
			// Need to sort based on score differential.
			// This will also take care of head-to-head.
			if (count > 1)
			{
				subCandidate = sortScoreDifferential(subCandidate);
			}
			
			// Sort the new sub candidates list.
			for (Player player : subCandidate)
			{
				subCandidates.add(player);
				candidate.remove(player);
			}
		}
		
		return subCandidates;
	}
	
	// Function to further sort the standings
	// based on head-to-head and score.
	private ArrayList<Player> sortScoreDifferential(ArrayList<Player> candidate)
	{
		Player playerOne;
		Player playerTwo;
		int result;
		
		// New sub-candidate list.
		ArrayList<Player> subCandidate = new ArrayList<Player>();
		
		// New sub-candidates list.
		ArrayList<Player> subCandidates = new ArrayList<Player>();
		
		int maxScore = -100000;
		int count = 0;
		
		// We can do head-to-head.
		if (candidate.size() == 2)
		{
			playerOne = candidate.get(0);
			playerTwo = candidate.get(1);
			
			result = playerOne.getHeadToHeadResult(playerTwo.getId());
			
			// Player one won.
			if (result == 2)
			{
				subCandidates.add(playerOne);
				
				return subCandidates;
			}
			// Player two won.
			else if (result == 1 || result == 0)
			{
				subCandidates.add(playerTwo);
				
				return subCandidates;
			}
		}
		
		// Continue sorting until all the
		// candidates have been sorted.
		while (!candidate.isEmpty())
		{
			subCandidate.clear();
			
			maxScore = -100000;
			count = 0;
			
			for (Player player : candidate)
			{
				// Player leads the field in score.
				if (player.getScoreDif() > maxScore)
				{
					maxScore = player.getScoreDif();
					
					subCandidate.clear();
					subCandidate.add(player);
					
					count = 1;
				}
				// Player is tied for the lead in score.
				else if (player.getScoreDif() == maxScore)
				{
					subCandidate.add(player);
					
					count++;
				}
			}
			
			// Sort the new sub candidates list.
			for (Player player : subCandidate)
			{
				// Need tiebreaker games.
				if (count == 2)
				{
					player.needsTiebreaker();
				}
				else if (count > 2)
				{
					player.needsMultipleTiebreakers();
				}
				
				subCandidates.add(player);
				candidate.remove(player);
			}
		}
		
		return subCandidates;
	}
	
	// Function that creates a ListView to display
	// the results of each game.
	private void createScheduleView()
	{
		_gameResults = new ArrayList<Map<String, String>>();
		_results = new ArrayList<String[]>();
		
		// Create the placeholder HashMap and add
		// it to the ListView.
		Map<String, String> datum = new HashMap<String, String>(2);
		
		datum.put("title", "The result of each game will be shown here.");
		datum.put("subTitle", "Subtitle");
		
		_gameResults.add(datum);
		
		// Set up the placeholder adapter.
		SimpleAdapter adapter = new SimpleAdapter(this, _gameResults, android.R.layout.simple_list_item_2,
												  new String[] {"title", "subTitle"},
												  new int[] {android.R.id.text1, android.R.id.text2});
		
		_schedule.setAdapter(adapter);
	}
	
	// Function to update the ListView
	// that displays game results.
	private void updateScheduleView()
	{
		SimpleAdapter adapter;
		String title, subTitle;
		
		_gameResults.clear();
		
		for (String[] result : _results)
		{
			Map<String, String> datum = new HashMap<String, String>(2);
			
			// Add the game result String.
			title = (Integer.parseInt(result[2]) > Integer.parseInt(result[3])) ?
					 result[0] + " def. " + result[1] :
					 result[1] + " def. " + result[0];
					 
			datum.put("title", title);
	
			// If the game went to overtime, the
			// subtitle will indicate this. Also
			// shows the score for the game.
			subTitle = "Final ";
			subTitle += (Integer.parseInt(result[2]) > Integer.parseInt(result[3])) ?
					  	 result[2] + "-" + result[3] :
					  	 result[3] + "-" + result[2]; 
			subTitle += (result[4].equals("1")) ? " (OT)" : "";
			
			datum.put("subTitle", subTitle);
			
			_gameResults.add(datum);
		}
		
		// Set up the new adapter.
		adapter = new SimpleAdapter(this, _gameResults, android.R.layout.simple_list_item_2,
									new String[] {"title", "subTitle"},
									new int[] {android.R.id.text1, android.R.id.text2});
		
		// Re-add the game results list.
		_schedule.setAdapter(adapter);
	}
	
	// Function to add a new result
	// or replace an existing one
	// if it already exists.
	private void addNewResult(Player playerOne, Player playerTwo, int playerOneScore, int playerTwoScore, boolean overtime)
	{
		String[] newResult = new String[5];
		int result = resultAlreadyExists(playerOne, playerTwo);
		
		// The players haven't played each other yet.
		if (result == -1)
		{
			newResult[0] = playerOne.getName();
			newResult[1] = playerTwo.getName();
			newResult[2] = Integer.toString(playerOneScore);
			newResult[3] = Integer.toString(playerTwoScore);
			
			newResult[4] = (overtime) ? Integer.toString(1) : Integer.toString(0);
			
			// Add the new result to the list
			// of existing results.
			_results.add(newResult);
		}
		else
		{
			newResult = _results.get(result);
			
			// Modify the existing result.
			newResult[0] = playerOne.getName();
			newResult[1] = playerTwo.getName();
			newResult[2] = Integer.toString(playerOneScore);
			newResult[3] = Integer.toString(playerTwoScore);
			
			newResult[4] = (overtime) ? Integer.toString(1) : Integer.toString(0);
		}
	}
	
	// Function to indicate whether a result
	// is new or replacing an existing one.
	private int resultAlreadyExists(Player playerOne, Player playerTwo)
	{
		String pOneName, pTwoName;
		int count = 0;
		
		for (String[] result : _results)
		{
			pOneName = result[0];
			pTwoName = result[1];
			
			// Check if these two players have played each other before.
			if ((pOneName.equals(playerOne.getName()) && pTwoName.equals(playerTwo.getName())) ||
				(pOneName.equals(playerTwo.getName()) && pTwoName.equals(playerOne.getName())))
			{
				// Return the index in the Array
				// to be modified.
				return count;
			}
			
			count++;
		}
		
		// The players have not played each other
		// before. Return -1.
		return -1;
	}
}
