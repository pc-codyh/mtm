package cody.mtmanager.com;

import android.content.Context;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

public class Utility
{
	////////////////////
	// DEFAULT VALUES //
	////////////////////
	static final int MIN_PLAYERS = 4;
	static final int MAX_PLAYERS = 32;
	static final int MIN_PLAYERS_PLAYOFFS = 2;
	
	static final int POOL_PLAY = 0;
	static final int SINGLE_ELIM = 1;
	static final int DOUBLE_ELIM = 2;
	
	// Reorderable ListView background color.
	static final int BACKGROUND_COLOR = 0xFFFF0000;
	
	// String array that holds the possible draft types.
	static final String TOURNAMENT_TYPES[] = {"Elimination", "Round Robin"};
	
	// String array that holds the possible
	// elimination draft sub-types.
	static final String TOURNAMENT_SUBTYPES_ELIM[] = {"Single Elimination", "Double Elimination"};
	
	// String array that holds the possible
	// round robin draft sub-types. Order
	// here matters - "Pool Play" should be
	// first.
	static final String TOURNAMENT_SUBTYPES_RR[] = {"Pool Play"};
	
	// Default parameters to modify the width and height of a TableRow.
	static TableRow.LayoutParams DEFAULT_ROW_PARAMS = new TableRow.LayoutParams
	(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
	
	// Parameters to modify the width, height, and weight of a TextView.
	// Used in the tournament's standings table (for the name column).
	static TableRow.LayoutParams NAME_ROW_PARAMS = new TableRow.LayoutParams
	(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.4f);
	
	// Parameters to modify the width, height, and weight of a TextView.
	// Used in the tournament's standings table (for the stats columns).
	static TableRow.LayoutParams STAT_ROW_PARAMS = new TableRow.LayoutParams
	(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
	////////////////////
	
	// Default constructor.
	public Utility()
	{
		
	}
	
	// Remove all whitespace in a string.
	public static String removeAllSpaces(String str)
	{
		return str.replaceAll(" ", "");
	}
	
	////////////////////////////////////////////////
	// Display short and long toasts to the user. //
	////////////////////////////////////////////////
	public static void displayShortMessage(String msg, Context context)
	{
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void displayLongMessage(String msg, Context context)
	{
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	////////////////////////////////////////////////
	
	// Function to manually set the height of a ListView.
	// This function is primarily to mend the effects
	// of using a ListView inside a ScrollView.
	public static void setListViewHeightBasedOnChildren(ListView listView)
	{
		ListAdapter listAdapter = listView.getAdapter();
		
		// There are no children in the ListView,
		// so just exit the function.
		if (listAdapter == null)
		{
			return;
		}
		
		int totalHeight = 0;
		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
		
		// Iterate through the children and increment
		// the height accordingly.
		for (int i = 0; i < listAdapter.getCount(); i++)
		{
			View listItem = listAdapter.getView(i, null, listView);
			
			listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			
			totalHeight += listItem.getMeasuredHeight();
		}
		
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		
		// Final adjustments to the total height of
		// the ListView.
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		
		listView.setLayoutParams(params);
		listView.requestLayout();
	}
}
