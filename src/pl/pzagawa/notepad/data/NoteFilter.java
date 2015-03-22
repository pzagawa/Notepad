package pl.pzagawa.notepad.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;

public class NoteFilter
	extends Observable
{
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private NoteMainFilterType mainfilterType = NoteMainFilterType.INVALID;

	private Calendar startDate = null;
	private Calendar endDate = null;
	private NoteListFilterType listFilterType = NoteListFilterType.NONE;	

	public NoteFilter()
	{
		resetWithoutNotify();
	}

	public void reset()
	{
		resetWithoutNotify();
		
		this.setChanged();	
		this.notifyObservers(this);
	}
	
	private void resetWithoutNotify()
	{
		this.mainfilterType = NoteMainFilterType.INVALID;

		this.startDate = null;
		this.endDate = null;
		this.listFilterType = NoteListFilterType.NONE;
	}
	
	public void setByCreateDate(Calendar startDate)
	{
		resetWithoutNotify();
		
		this.mainfilterType = NoteMainFilterType.BY_CREATE_DATE;
		this.startDate = startDate;		

		this.setChanged();
		this.notifyObservers(this);
	}

	public void setByReminderDate(Calendar startDate, Calendar endDate)
	{		
		resetWithoutNotify();
		
		this.mainfilterType = NoteMainFilterType.BY_REMINDER_DATE;
		this.startDate = startDate;
		this.endDate = endDate;
		
		this.setChanged();
		this.notifyObservers(this);
	}

	public void setByTags(NoteListFilterType listFilterType)
	{		
		resetWithoutNotify();
		
		this.mainfilterType = NoteMainFilterType.BY_TAGS;
		this.listFilterType = listFilterType;
		
		this.setChanged();
		this.notifyObservers(this);
	}

	public NoteMainFilterType getFilterType()
	{
		return mainfilterType;
	}

	public Calendar getStartDate()
	{
		return startDate;
	}

	public String getStartDateText()
	{
		return dateFormat.format(startDate.getTime());
	}
	
	public Calendar getEndDate()
	{
		return endDate;
	}

	public String getEndDateText()
	{
		return dateFormat.format(endDate.getTime());
	}
	
	public NoteListFilterType getListFilterType()
	{
		return listFilterType;
	}
	
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		
		sb.append(mainfilterType.toString());
		sb.append(", ");
		sb.append((startDate == null) ? "*" : getStartDateText()); 
		sb.append(", ");
		sb.append((endDate == null) ? "*" : getEndDateText());		
		sb.append(", list: ");
		sb.append(listFilterType);
				
		return sb.toString();
	}
	
}
