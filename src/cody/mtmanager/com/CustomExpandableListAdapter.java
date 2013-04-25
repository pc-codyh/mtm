package cody.mtmanager.com;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter
{
	private Context _context;
	
	// The parent group for the ExpandableListView.
	static String _parent[];
	static String _children[][];
	
	private final int PARENT_OFFSET = 60;
	private final int CHILD_OFFSET = (PARENT_OFFSET * 2);
	private final int LIST_ITEM_PADDING = 10;
	
	// Default constructor.
	public CustomExpandableListAdapter(Context context, String[] parent, String[]... children)
	{
		_context = context;
		_parent = parent;
		_children = children;
	}
	
	public Object getChild(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public long getChildId(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	// Method to get the child view of a given parent.
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{
		TextView tv = new TextView(_context);
		
		tv.setText(_children[groupPosition][childPosition]);
		tv.setPadding(CHILD_OFFSET, LIST_ITEM_PADDING, LIST_ITEM_PADDING, LIST_ITEM_PADDING);
		
		return tv;
	}

	// Method to return the amount of children
	// in a given sub-group.
	public int getChildrenCount(int groupPosition)
	{
		return _children[groupPosition].length;
	}

	// Method to return a given child group.
	public Object getGroup(int groupPosition)
	{
		return _parent[groupPosition];
	}

	// Method to return the amount of
	// items in the parent.
	public int getGroupCount()
	{
		return _parent.length;
	}

	// Method to return the id of a given group.
	public long getGroupId(int groupPosition)
	{
		return groupPosition;
	}

	// Method to get a given parent view.
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		TextView tv = new TextView(_context);
		
		tv.setText(_parent[groupPosition]);
		tv.setPadding(PARENT_OFFSET, LIST_ITEM_PADDING, LIST_ITEM_PADDING, LIST_ITEM_PADDING);
		
		return tv;
	}

	public boolean hasStableIds()
	{
		// TODO Auto-generated method stub
		return false;
	}

	// Method to determine if a given child is selectable.
	// By default, all children all selectable.
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}
}
