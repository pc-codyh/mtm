package cody.mtmanager.com;

import android.content.Context;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Utility
{
	////////////////////
	// DEFAULT VALUES //
	////////////////////
	public final int MIN_PLAYERS = 5;
	public final int MAX_PLAYERS = 8;
	public final int MIN_PLAYERS_PLAYOFFS = 2;
	
	// String array that holds the possible draft types.
	public final String TOURNAMENT_TYPES[] = {"Elimination", "Round Robin"};
	
	// String array that holds the possible
	// elimination draft sub-types.
	public final String TOURNAMENT_SUBTYPES_ELIM[] = {"Single Elimination", "Double Elimination"};
	
	// String array that holds the possible
	// round robin draft sub-types. Order
	// here matters - "Pool Play" should be
	// first.
	public final String TOURNAMENT_SUBTYPES_RR[] = {"Pool Play"};
	////////////////////
	
	// Default constructor.
	public Utility()
	{
		
	}
	
	// Remove all whitespace in a string.
	public String removeAllSpaces(String str)
	{
		return str.replaceAll(" ", "");
	}
	
	////////////////////////////////////////////////
	// Display short and long toasts to the user. //
	////////////////////////////////////////////////
	public void displayShortMessage(String msg, Context context)
	{
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public void displayLongMessage(String msg, Context context)
	{
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	////////////////////////////////////////////////
	
	// Function to manually set the height of a ListView.
	// This function is primarily to mend the effects
	// of using a ListView inside a ScrollView.
	public void setListViewHeightBasedOnChildren(ListView listView)
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
