package cody.mtmanager.com;

import android.graphics.Color;
import android.widget.TableRow;
import android.widget.TextView;

public class Player
{
	// Unique ID.
	private int _id;
	
	// Player name.
	private String _name;
	
	// Current points.
	private int _points;
	
	// Current score differential.
	private int _scoreDif;
	
	// Current win total.
	private int _wins;
	
	// Current loss total.
	private int _losses;
	
	// Current ot loss total.
	private int _otLosses;
	
	// Total number of players.
	private int _numPlyrs;
	
	// Array to keep track of game results.
	//
	// 0: Opponent ID
	// 1: Result (0 = Loss, 1 = OT Loss, 2 = Win)
	// 2: Score Difference
	private int _results[][]; 
	
	// References to the TextViews in the standings
	// table that belong to this player.
	TextView _sName;
	TextView _sWins;
	TextView _sLosses;
	TextView _sOTLosses;
	TextView _sPoints;
	TextView _sScore;
	
	// Reference to the Row that holds
	// all of the TextViews.
	TableRow _sRow;
	
	// Default constructor.
	public Player(int id, String name, int numPlayers)
	{
		_id = id;
		_name = name;
		_numPlyrs = numPlayers;
		
		// This will include the player itself, but
		// this row will always be -1, 0, 0 since a
		// player cannot play himself.
		_results = new int[numPlayers][3];
		
		_points = 0;
		_scoreDif = 0;
		_wins = 0;
		_losses = 0;
		_otLosses = 0;
		
		initializeResultsArray(numPlayers);
	}
	
	// Function to complete the reference to
	// the appropriate TextViews.
	public void addStandingsReference(TextView name, TextView wins, TextView losses,
									  TextView otLosses, TextView points, TextView score,
									  TableRow row)
	{
		_sName = name;
		_sWins = wins;
		_sLosses = losses;
		_sOTLosses = otLosses;
		_sPoints = points;
		_sScore = score;
		_sRow = row;
	}
	
	// Function to initialize all the opponent ID's
	// to -1 in the results array.
	private void initializeResultsArray(int count)
	{
		// A value of -1 for the Opponent ID means
		// that the player has not played against
		// this opponent.
		for (int i = 0; i < count; i++)
		{
			_results[i][0] = -1;
		}
	}
	
	// Function to return a player's unique ID.
	public int getId()
	{
		return _id;
	}
	
	// Function to return a player's name.
	public String getName()
	{
		return _name;
	}
	
	// Function to add a game result.
	public void addResult(int opponentId, int result, int difference)
	{
		_results[opponentId][0] = opponentId;
		_results[opponentId][1] = result;
		_results[opponentId][2] = difference;
		
		calculateTotals();
	}
	
	// Function to re-calculate the total points
	// and total score differential of the player.
	private void calculateTotals()
	{
		int score = 0;
		int points = 0;
		int wins = 0;
		int losses = 0;
		int otLosses = 0;
		
		for (int i = 0; i < _numPlyrs; i++)
		{
			// The opponent has been played.
			if (_results[i][0] != -1)
			{
				points += _results[i][1];
				score += _results[i][2];
				
				switch (_results[i][1])
				{
					// Loss.
					case 0:
					{
						losses++;
					}
						break;
						
					// OT Loss.	
					case 1:
					{
						otLosses++;
					}
						break;
						
					// Win.
					case 2:
					{
						wins++;
					}
						break;
						
					default:
						break;
				}
			}
		}
		
		_points = points;
		_scoreDif = score;
		_wins = wins;
		_losses = losses;
		_otLosses = otLosses;
		
		// Update the standings table.
		_sWins.setText(Integer.toString(_wins));
		_sLosses.setText(Integer.toString(_losses));
		_sOTLosses.setText(Integer.toString(_otLosses));
		_sPoints.setText(Integer.toString(_points));
		_sScore.setText(Integer.toString(_scoreDif));
	}
	
	// Function to get player's point total.
	public int getPoints()
	{
		return _points;
	}
	
	// Function to get player's win total.
	public int getWins()
	{
		return _wins;
	}
	
	// Function to get player's score differential.
	public int getScoreDif()
	{
		return _scoreDif;
	}
	
	// Function to check the head-to-head
	// result of a game. Returns -1 if the
	// opponent hasn't been played.
	public int getHeadToHeadResult(int opponentId)
	{
		if (_results[opponentId][0] != -1)
		{
			return _results[opponentId][1];
		}
		
		return -1;
	}
	
	// Function to indicate that this player
	// needs to play a tiebreaker game.
	public void needsTiebreaker()
	{
		_sName.setTextColor(Color.YELLOW);
	}
	
	// Function to indicate that this player
	// needs to play multiple tiebreakers.
	public void needsMultipleTiebreakers()
	{
		_sName.setTextColor(Color.BLUE);
	}
	
	// Function to remove any indication of
	// a tiebreaker game.
	public void removeTiebreaker()
	{
		_sName.setTextColor(Color.WHITE);
	}
	
	// Function to return the row with all
	// the standings information.
	public TableRow getStandingsRow()
	{
		return _sRow;
	}
}
