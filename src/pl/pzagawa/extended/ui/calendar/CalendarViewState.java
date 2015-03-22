package pl.pzagawa.extended.ui.calendar;

import java.util.Calendar;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View.BaseSavedState;

public class CalendarViewState
	extends BaseSavedState 
{	
	private int viewYear = 0;
	private int viewMonth = 0;
	private int viewDay = 0;

	private int selectedYear = 0;
	private int selectedMonth = 0;
	private int selectedDay = 0;
		
	public CalendarViewState(Parcelable state)
	{
		super(state);		
	}

	public CalendarViewState(Parcel input)
	{
		super(input);
		
		viewYear = input.readInt();
		viewMonth = input.readInt();
		viewDay = input.readInt();
		
		selectedYear = input.readInt();
		selectedMonth = input.readInt();
		selectedDay = input.readInt();
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags)
	{
		super.writeToParcel(out, flags);
		
		out.writeInt(viewYear);
		out.writeInt(viewMonth);
		out.writeInt(viewDay);

		out.writeInt(selectedYear);
		out.writeInt(selectedMonth);
		out.writeInt(selectedDay);
	}

	public void save(DataModel dm)
    {
        //view date
        Calendar cal = dm.getDate();
        
        viewYear = cal.get(Calendar.YEAR);
        viewMonth = cal.get(Calendar.MONTH);
        viewDay = cal.get(Calendar.DAY_OF_MONTH);

        //selected day
        if (dm.isDaySelected())
        {
        	Calendar sel = dm.getSelectedDayDate();

        	selectedYear = sel.get(Calendar.YEAR);        	
        	selectedMonth = sel.get(Calendar.MONTH);        	
        	selectedDay = sel.get(Calendar.DAY_OF_MONTH);
        }
        else
        {
        	selectedYear = 0;        	
        	selectedMonth = 0;        	
        	selectedDay = 0;
        }
    }
	
    public void restore(DataModel dm)
    {
	    //check if selected day info has been stored
	    if (selectedYear != 0 && selectedMonth != 0 && selectedDay != 0)
	    {
	        dm.selectDay(selectedYear, selectedMonth, selectedDay);
	    }

        dm.setDate(viewYear, viewMonth, viewDay);
    }
	
	public static final Parcelable.Creator<CalendarViewState> CREATOR = new Parcelable.Creator<CalendarViewState>()
	{
		public CalendarViewState createFromParcel(Parcel state)
		{
			return new CalendarViewState(state);
		}

		public CalendarViewState[] newArray(int size)
		{
			return new CalendarViewState[size];
		}
	};

}
