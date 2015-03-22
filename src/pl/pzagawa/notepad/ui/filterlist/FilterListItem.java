package pl.pzagawa.notepad.ui.filterlist;

import pl.pzagawa.notepad.data.NoteListFilterType;

public class FilterListItem
{
	private final NoteListFilterType filterType;
	
	private String text = "";
	
	private int count = 0;	
	private boolean selected = false;
	
	public FilterListItem(NoteListFilterType filterType)
	{
		this.filterType = filterType;
	}

	public void setText(String text)
	{
		this.text = text;
	}
	
	public String getText()
	{
		return text;
	}

	public void setCount(int value)
	{
		this.count = value;		
	}
		
	public String getCountText()
	{
		return Integer.toString(count);
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean value)
	{
		this.selected = value;
	}

	public NoteListFilterType getFilterType()
	{
		return filterType;
	}

	public static FilterListItem[] getFilterItemsByReminderDate(final String[] itemNames)
	{
		FilterListItem[] items =
		{
			new FilterListItem(NoteListFilterType.OVERDUE),
			new FilterListItem(NoteListFilterType.TODAY),
			new FilterListItem(NoteListFilterType.THIS_WEEK),
			new FilterListItem(NoteListFilterType.THIS_MONTH),
		};
		
		//set filter item names
		for (int index = 0; index < items.length; index++)
			items[index].setText(itemNames[index]);
		
		return items;
	}

	public static FilterListItem[] getFilterItemsByTags(final String[] itemNames)
	{
		FilterListItem[] items =
		{
			new FilterListItem(NoteListFilterType.PERSONAL),
			new FilterListItem(NoteListFilterType.HOME),
			new FilterListItem(NoteListFilterType.WORK),
			new FilterListItem(NoteListFilterType.TODO),
			new FilterListItem(NoteListFilterType.STARRED),
			new FilterListItem(NoteListFilterType.DONE),
		};
		
		//set filter item names
		for (int index = 0; index < items.length; index++)
			items[index].setText(itemNames[index]);
		
		return items;
	}
	
}
