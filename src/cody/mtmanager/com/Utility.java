package cody.mtmanager.com;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utility
{
	// * DEFAULT VALUES * //
	public final int MIN_PLAYERS = 5;
	public final int MAX_PLAYERS = 8;
	public final int MIN_PLAYERS_PLAYOFFS = 2;
	
	// String array that holds the possible draft types.
	public final String TOURNAMENT_TYPES[] = {"Elimination", "Round Robin"};
	
	// String array that holds the possible
	// elimination draft sub-types.
	public final String TOURNAMENT_SUBTYPES_ELIM[] = {"Single Elimination", "Double Elimination"};
	
	// String array that holds the possible
	// round robin draft sub-types.
	public final String TOURNAMENT_SUBTYPES_RR[] = {"Pool Play"};
	//********************//
	
	// Default constructor.
	public Utility()
	{
		
	}
	
	// Remove all whitespace in a string.
	public String removeAllSpaces(String str)
	{
		return str.replaceAll(" ", "");
	}
	
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
