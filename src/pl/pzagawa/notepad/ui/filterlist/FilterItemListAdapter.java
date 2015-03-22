package pl.pzagawa.notepad.ui.filterlist;

import pl.pzagawa.notepad.R;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FilterItemListAdapter
	implements ListAdapter
{
    private final LayoutInflater inflater;
    private final ListView listView;
	private final FilterListItem[] list;
	
	public FilterItemListAdapter(LayoutInflater inflater, ListView listView, FilterListItem[] list)
	{
	    this.inflater = inflater;
	    this.listView = listView;
	    this.list = list;
	}
	
	public int getCount()
	{
		return list.length;
	}

	public Object getItem(int position)
	{
		return list[position];
	}

	public long getItemId(int position)
	{
		return position;
	}

	public int getItemViewType(int position)
	{
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		LinearLayout view;
        
        if (convertView == null)
        {
            view = (LinearLayout)inflater.inflate(R.layout.filter_list_item, parent, false);
        } else {
        	view = (LinearLayout)convertView;
        }
        
        updateView(view, position);
        
        return view;
	}

	public void updateView(View view, int position)
	{
        final FilterListItem filterItem = list[position];
        
        final TextView textName = (TextView) view.findViewById(R.id.textFilterItemName);
        final TextView textValue = (TextView) view.findViewById(R.id.textFilterItemValue);
        final ImageView itemSelection = (ImageView) view.findViewById(R.id.imgFilterItemSelection);

        textName.setText(filterItem.getText());
        textValue.setText(filterItem.getCountText());

        if (filterItem.isSelected())
        {
        	textName.setTextColor(0xff404040);
        }
        else
        {
        	textName.setTextColor(0xff606060);
        }
	}
	
	public int getViewTypeCount()
	{
		return 1;
	}

	public boolean hasStableIds()
	{
		return true;
	}

	public boolean isEmpty()
	{
		return (list.length == 0);
	}

	public void registerDataSetObserver(DataSetObserver observer)
	{
	}

	public void unregisterDataSetObserver(DataSetObserver observer)
	{
	}

	public boolean areAllItemsEnabled()
	{
		return true;
	}

	public boolean isEnabled(int arg0)
	{
		return true;
	}

	private void deselectAllItems()
	{
		for (int index = 0; index < list.length; index++)
			list[index].setSelected(false);		
	}

	private View getViewByPosition(int position)
	{
		return listView.getChildAt(position);		
	}
	
	public void selectItem(int position)
	{
		deselectAllItems();
	
		list[position].setSelected(true);

		final View view = getViewByPosition(position);		
		if (view != null)
		{
			updateView(view, position);
			listView.invalidateViews();
		}
	}
	
	public int getSelectedItemIndex()
	{
		for (int index = 0; index < list.length; index++)
			if (list[index].isSelected())
				return index;
		
		return -1;
	}

	public FilterListItem getSelectedItem()
	{
		for (int index = 0; index < list.length; index++)
		{
			FilterListItem item = list[index];
			if (list[index].isSelected())
				return item;
		}
		
		return null;
	}
	
	public void setItemValue(int position, int value)
	{
		list[position].setCount(value);

		final View view = getViewByPosition(position);		
		if (view != null)
		{
			updateView(view, position);
			view.invalidate();
		}
	}
	
}
