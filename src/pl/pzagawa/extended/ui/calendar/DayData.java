package pl.pzagawa.extended.ui.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DayData
{
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public final int column;
	public final int row;
		
	private int day;
	private int month;
	private int year;
	
	private boolean isCurrentMonth = true;
	private boolean isToday = false;	
	private boolean isSelected = false;
	
	private DayDetailsData detailsData;
	
	public DayData(int column, int row)
	{
		this.column = column;
		this.row = row;

		this.set(0, 0, 0);
		
		this.detailsData = new DayDetailsData();
	}

	public int hashCode()
	{
		return (year * 10000) + (month * 100) + day;
	}

	public static int hashCode(int year, int monthOfYear, int dayOfMonth)
	{
		return (year * 10000) + (monthOfYear * 100) + dayOfMonth;
	}

	public static int hashCode(Calendar cal)
	{
		final int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		final int monthOfYear = cal.get(Calendar.MONTH);
		final int year = cal.get(Calendar.YEAR);
		
		return (year * 10000) + (monthOfYear * 100) + dayOfMonth;
	}
	
	public static int textDateToHashCode(String textDate)
		throws ParseException
	{
		Calendar dayDate = Calendar.getInstance();		
		dayDate.setTime(DayData.dateFormat.parse(textDate));
		return hashCode(dayDate);
	}
	
	public static String dateToText(Calendar cal)
	{	
		return dateFormat.format(cal.getTime());
	}
	
	public boolean equals(Object obj)
	{
		if (obj == null)
            return false;
		
        if (obj == this)
            return true;
        
        if (obj.getClass() != getClass())
            return false;

        return this.hashCode() == obj.hashCode();
	}
	
	public boolean equals(int year, int monthOfYear, int dayOfMonth)
	{
		return (this.day == dayOfMonth) && (this.month == monthOfYear) && (this.year == year); 
	}
	
	protected void setCurrentMonth(boolean value)
	{
		this.isCurrentMonth = value;
	}
	
	protected void setToday(boolean value)
	{
		this.isToday = value;
	}
	
	protected void setSelected(boolean value)
	{
		this.isSelected = value;
	}
	
	protected void set(int day, int month, int year)
	{
		this.day = day;
		this.month = month;
		this.year = year;
	}

	protected void set(Calendar cal)
	{
		this.day = cal.get(Calendar.DAY_OF_MONTH);
		this.month = cal.get(Calendar.MONTH);
		this.year = cal.get(Calendar.YEAR);
	}
		
	public int getDay()
	{
		return day;
	}

	public int getMonth()
	{
		return month;
	}

	public int getYear()
	{
		return year;
	}

	public boolean isHoliday()
	{
		return MonthView.isHoliday(column);
	}
	
	public boolean isCurrentMonth()
	{
		return isCurrentMonth;
	}
	
	public boolean isToday()
	{
		return isToday;
	}
	
	public boolean isSelected()
	{
		return isSelected;
	}

	public DayDetailsData getDetailsData()
	{
		return detailsData;
	}

	protected void setDetailsData(DayDetailsData data)
	{
		this.detailsData = data;
	}
	
	@Override
	public String toString()
	{
		return String.format("%1$d-%2$d-%3$d", day, month + 1, year);
	}
	
}
