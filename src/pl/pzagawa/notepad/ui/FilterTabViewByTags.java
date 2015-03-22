package pl.pzagawa.notepad.ui;

import pl.pzagawa.notepad.CommonFragment;
import pl.pzagawa.notepad.R;
import pl.pzagawa.notepad.data.NoteListFilterType;
import pl.pzagawa.notepad.ui.filterlist.FilterItemListAdapter;
import pl.pzagawa.notepad.ui.filterlist.FilterListItem;
import android.app.Activity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

public class FilterTabViewByTags
{
	private final CommonFragment fragment;

	private ListView itemsView = null;
	private FilterListItem[] filterItems;
	private FilterItemListAdapter filterListAdapter = null;        
		
	public FilterTabViewByTags(CommonFragment fragment)
	{
		this.fragment = fragment;		
	}

	public void getViews()
	{
		final Activity activity = fragment.getActivity();

		this.itemsView = (ListView) activity.findViewById(R.id.itemsListTab3);
		
		final String[] filterItemNames = activity.getResources().getStringArray(R.array.ByTagsFilterItems);		
		this.filterItems = FilterListItem.getFilterItemsByTags(filterItemNames);

		//initialize
		this.itemsView.setOnItemClickListener(onItemClickListener);
		this.itemsView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
	}
	
	public void selectPersonalTag()
	{
		final Activity activity = fragment.getActivity();
		
        this.filterListAdapter = new FilterItemListAdapter(activity.getLayoutInflater(), itemsView, filterItems);        
        itemsView.setAdapter(filterListAdapter);
        
        //select default item
        filterListAdapter.selectItem(0);
	}
	
	public NoteListFilterType getFilterListType()
	{
		final FilterListItem item = filterListAdapter.getSelectedItem();
		
		if (item == null)
			return NoteListFilterType.NONE;

		return item.getFilterType();
	}

	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			filterListAdapter.selectItem(position);

			//set filter
			fragment.getFilterFragment().getNoteFilter().setByTags(getFilterListType());
		}
	};
	
}
