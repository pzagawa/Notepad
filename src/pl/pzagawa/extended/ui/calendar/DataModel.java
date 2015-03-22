package pl.pzagawa.extended.ui.calendar;

import java.text.ParseException;
import java.util.Calendar;

import pl.pzagawa.extended.ui.calendar.CalendarView.OnCursorRequestListener;
import pl.pzagawa.extended.ui.calendar.CalendarView.OnDayUpdateListener;
import android.database.Cursor;
import android.util.MonthDisplayHelper;
import android.util.SparseArray;

class DataModel
{	
	private Calendar referenceDate = Calendar.getInstance();	
	
	private Calendar date = (Calendar) referenceDate.clone();	
	private Calendar dateSelected = null;
		
	private MonthDisplayHelper resolver = null;
	
	private OnDayUpdateListener onDayUpdate = null;
	private OnCursorRequestListener OnCursorRequest = null;
	
	private DayData[] days = new DayData[CalendarView.TOTAL_DAYS];
	private SparseArray<DayData> daysMap = new SparseArray<DayData>(); 

	private boolean isLastRowShowingOtherMonth = false;

	public DataModel()
	{
		initialize();
	}
	
	public void setOnDayUpdateListener(OnDayUpdateListener onDayUpdate)
	{
		this.onDayUpdate = onDayUpdate;
	}

	public void setOnCursorRequestListener(OnCursorRequestListener OnCursorRequest)
	{
		this.OnCursorRequest = OnCursorRequest;
	}
	
	private void initialize()
	{
		//create days cells
		int columnIndex = 0;
		int rowIndex = 0;
		
		for (int dayIndex = 0; dayIndex < CalendarView.TOTAL_DAYS; dayIndex++)
		{
			columnIndex = dayIndex % CalendarView.DAYS_IN_WEEK;
			
			final DayData day = new DayData(columnIndex, rowIndex);			
			days[dayIndex] = day;

			if (columnIndex == 6)
				rowIndex++;
		}		
	}

	public void setTodayReferenceDate(Calendar referenceDate)
	{
		this.referenceDate = (Calendar) referenceDate.clone();		
	}

	private void setDate(Calendar date)
	{
		this.date = (Calendar)date.clone();
		alignDateToFirstDayOfMonth();

		update();
	}
	
	public void setDate(int year, int monthOfYear, int dayOfMonth)
	{
		Calendar cal = (Calendar) referenceDate.clone();
		cal.set(year, monthOfYear, dayOfMonth);
		
		setDate(cal);
	}	

	public void setToday()
	{
		Calendar cal = (Calendar) referenceDate.clone();
		
		setDate(cal);
	}
	
	public void selectDay(int year, int monthOfYear, int dayOfMonth)
	{
		resetSelectedDay();

		dateSelected = (Calendar)referenceDate.clone();
		dateSelected.set(year, monthOfYear, dayOfMonth);

		setDate(dateSelected);
	}
	
	public void resetSelectedDay()
	{
		dateSelected = null;
		clearSelection();
	}

	public boolean isDaySelected()
	{
		return (dateSelected != null);
	}

	public Calendar getSelectedDayDate()
	{
		return dateSelected;
	}

	public DayData getSelectedDay()
	{
		for (int dayIndex = 0; dayIndex < CalendarView.TOTAL_DAYS; dayIndex++)
		{
			final DayData day = days[dayIndex];			
			if (day.isSelected())
				return day;
		}
		
		return null;
	}
	
	public DayData getDayData(int index)
	{
		return days[index];
	}
	
	public DayData getDayData(int year, int monthOfYear, int dayOfMonth)
	{
		final int key = DayData.hashCode(year, monthOfYear, dayOfMonth);

		return daysMap.get(key);
	}
	
	public Calendar getDate()
	{
		return date;
	}
	
	private void alignDateToFirstDayOfMonth()
	{
		date.set(Calendar.DAY_OF_MONTH, 1);
	}
			
	public void setNextMonth()
	{		
		date.add(Calendar.MONTH, 1);
		alignDateToFirstDayOfMonth();
		
		update();
	}

	public void setPrevMonth()
	{		
		date.add(Calendar.MONTH, -1);
		alignDateToFirstDayOfMonth();
		
		update();
	}	
		
	public int getYear()
	{
		return date.get(Calendar.YEAR);
	}

	public int getMonth()
	{
		return date.get(Calendar.MONTH);
	}
	
	public boolean isCurrentMonth(DayData dayData)
	{		
		return resolver.isWithinCurrentMonth(dayData.row, dayData.column);
	}

	private boolean isToday(final Calendar cal)
	{
		if (referenceDate.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH))
			if (referenceDate.get(Calendar.MONTH) == cal.get(Calendar.MONTH))
				if (referenceDate.get(Calendar.YEAR) ==  cal.get(Calendar.YEAR))
					return true;
		
		return false;
	}

	private boolean isDaySelectedEqual(final Calendar cal)
	{
		if (isDaySelected())
		{
			if (dateSelected.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH))
				if (dateSelected.get(Calendar.MONTH) == cal.get(Calendar.MONTH))
					if (dateSelected.get(Calendar.YEAR) ==  cal.get(Calendar.YEAR))
						return true;
		}
		
		return false;
	}

	private Calendar getWeekStartDate()
	{
		Calendar weekStart = (Calendar)date.clone();

		while (weekStart.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
			weekStart.add(Calendar.DAY_OF_WEEK, -1);
		
		return weekStart;
	}
	
	private void updateData()
	{		
		final Calendar calBegin = this.getWeekStartDate();
		final Calendar calEnd = (Calendar)calBegin.clone();
		calEnd.add(Calendar.DAY_OF_MONTH, CalendarView.TOTAL_DAYS - 1);
		
		daysMap.clear();

		//update day objects date reference
		final Calendar cal = (Calendar)calBegin.clone();
		
		for (int dayIndex = 0; dayIndex < CalendarView.TOTAL_DAYS; dayIndex++)
		{
			final DayData day = days[dayIndex];
			
			day.set(cal);
			day.setCurrentMonth(isCurrentMonth(day));
			day.setToday(isToday(cal));
			day.setSelected(isDaySelectedEqual(cal));
			
			//map day object to date key
			daysMap.put(day.hashCode(), day);
			
			day.getDetailsData().reset();
			
			if (this.onDayUpdate != null)
				this.onDayUpdate.OnDayUpdate(day);

			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

		updateDaysDetailsFromCursor(calBegin, calEnd);
	}

	private boolean checkIfLastRowShowsOtherMonth()
	{
		final int lastRowStartIndex = CalendarView.DAYS_IN_WEEK * 5;

		boolean isCurrentMonth = false;
		
		for (int dayIndex = lastRowStartIndex; dayIndex < CalendarView.TOTAL_DAYS; dayIndex++)
		{			
			final DayData day = days[dayIndex];					
			if (day.isCurrentMonth())
				isCurrentMonth = true;
		}
		
		return !isCurrentMonth;
	}
	
	public void update()
	{
		resolver = new MonthDisplayHelper(date.get(Calendar.YEAR), date.get(Calendar.MONTH), Calendar.MONDAY);

		updateData();
		
		isLastRowShowingOtherMonth = checkIfLastRowShowsOtherMonth();
	}
	
	protected boolean isLastRowShowingOtherMonth()
	{
		return this.isLastRowShowingOtherMonth;		
	}
	
	private void clearSelection()
	{	
		for (int dayIndex = 0; dayIndex < CalendarView.TOTAL_DAYS; dayIndex++)
		{
			final DayData day = days[dayIndex];			
			day.setSelected(false);
		}
	}
	
	private void updateDaysDetailsFromCursor(final Calendar calBegin, final Calendar calEnd)
	{		
		if (this.OnCursorRequest != null)
		{
			//request listener for cursor data
			final Cursor cr = this.OnCursorRequest.OnCursorRequest((Calendar)calBegin.clone(), calEnd);
			if (cr == null)
				return;

			try
			{
				final int fieldDayDateIndex = cr.getColumnIndexOrThrow(DayDetailsData.FIELD_DAY_DATE);			
				final int fieldItemsCountIndex = cr.getColumnIndexOrThrow(DayDetailsData.FIELD_ITEMS_COUNT);
				final int fieldThemeNameIndex = cr.getColumnIndexOrThrow(DayDetailsData.FIELD_THEME_NAME);
				
				//iterate through
				cr.moveToFirst();
	
				while (cr.isAfterLast() == false) 
				{
					final String dayTextDate = cr.isNull(fieldDayDateIndex) ? "" : cr.getString(fieldDayDateIndex);
					final int itemsCount = cr.getInt(fieldItemsCountIndex);
					final String themeName = cr.getString(fieldThemeNameIndex);
	
					try
					{
						final int dateKey = DayData.textDateToHashCode(dayTextDate);
	
						//get day object by date key
						final DayData dayData = daysMap.get(dateKey);
						if (dayData != null)
						{
							final DayDetailsData details = dayData.getDetailsData();

							details.setItemsCount(itemsCount);
							details.setTheme(themeName);
						}
					}
					catch (ParseException e)
					{
						e.printStackTrace();
						this.OnCursorRequest.OnCursorError("CalendarView: incorrect date value '" +  dayTextDate + "' in cursor field " + DayDetailsData.FIELD_DAY_DATE + ".\n" + e.getMessage());
					}
	
				    cr.moveToNext();
				}				
			}
			catch (Exception e)
			{
				e.printStackTrace();
				this.OnCursorRequest.OnCursorError("CalendarView: cursor error.\n" + e.getMessage());
			}
			finally
			{
				cr.close();				
			}
		}
	}
	
}
