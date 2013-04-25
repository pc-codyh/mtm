package cody.mtmanager.com;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainMenu extends ListActivity
{
	// String array that holds the items in the main menu.
	// Any new option for the main menu should be added
	// into this array.
	String _classes[] = {"New Tournament", "Load Tournament", "Completed Tournaments"};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Load up the options into the main menu.
		setListAdapter(new ArrayAdapter<String>(MainMenu.this, android.R.layout.simple_list_item_1, _classes));
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id)
	{
		super.onListItemClick(listView, view, position, id);
				
		// Start a new activity based on the item
		// selected from the main menu.
		try
		{
			// We have to remove all whitespace from the selected item
			// because class/activity names do not allow spaces.
			startActivity(new Intent("cody.mtmanager.com." + Utility.removeAllSpaces(_classes[position])));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
