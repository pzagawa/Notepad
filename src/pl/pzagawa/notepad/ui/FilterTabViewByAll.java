package pl.pzagawa.notepad.ui;

import java.util.Calendar;

import pl.pzagawa.extended.ui.calendar.CalendarView;
import pl.pzagawa.extended.ui.calendar.DayData;
import pl.pzagawa.extended.ui.calendar.DayDetailsData;
import pl.pzagawa.notepad.CommonFragment;
import pl.pzagawa.notepad.R;
import android.app.Activity;
import android.database.Cursor;
import android.database.MatrixCursor;

public class FilterTabViewByAll
{
	private final CommonFragment fragment;

	private CalendarView calendarView = null;
		
	public FilterTabViewByAll(CommonFragment fragment)
	{
		this.fragment = fragment;
	}

	public void getViews()
	{
		final Activity activity = fragment.getActivity();
		
		this.calendarView = (CalendarView) activity.findViewById(R.id.calendarView);
		
		//initialize		
        calendarView.setOnCursorRequestListener(calendarCursorRequestListener);
        calendarView.setOnDaySelectedListener(calendarDaySelectedListener);
        calendarView.setOnViewChangeListener(calendarViewChangeListener);			
	}
	
	public void selectToday()
	{
		final Calendar cal = Calendar.getInstance();
		
		calendarView.selectDay(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));		
	}
	
	public Calendar getSelectedDayDate()
	{
		return calendarView.getSelectedDayDate();
	}
	
	public CalendarView.OnViewChangeListener calendarViewChangeListener = new CalendarView.OnViewChangeListener()
	{
		public void OnViewDateChange(Calendar viewDate)
		{
			calendarView.resetSelectedDay();
			
			//reset filter
			fragment.getFilterFragment().getNoteFilter().reset();
		}
	};
	
	public CalendarView.OnDaySelectedListener calendarDaySelectedListener = new CalendarView.OnDaySelectedListener()
	{
		public void onDaySelected(DayData dataDay)
		{
			if (dataDay != null)
			{
				//set filter
				fragment.getFilterFragment().getNoteFilter().setByCreateDate(calendarView.getSelectedDayDate());
			}
		}
	};

	public CalendarView.OnCursorRequestListener calendarCursorRequestListener = new CalendarView.OnCursorRequestListener()
	{
		public Cursor OnCursorRequest(Calendar calBegin, Calendar calEnd)
		{
			//DayDetailsData.QUERY_FIELDS: projection definition for returned cursor
			final MatrixCursor cr = new MatrixCursor(DayDetailsData.QUERY_FIELDS);
			
			cr.newRow().add("2014-08-01").add(1);
			cr.newRow().add("2014-08-03").add(9);
			cr.newRow().add("2014-08-05").add(10).add("blue");
			cr.newRow().add("2014-08-14").add(22).add("green");
			cr.newRow().add("2014-08-24").add(12).add("orange");
			cr.newRow().add("2014-08-19").add(7).add("red");
			cr.newRow().add("2014-08-29").add(14).add("yellow");
			cr.newRow().add("2014-09-12").add(200).add("green");
			cr.newRow().add("2014-09-28").add(1).add("orange");
						
			return cr;			
		}

		public void OnCursorError(String errorMessage)
		{
			//TODO: calendar cursor error
		}
	};

}
