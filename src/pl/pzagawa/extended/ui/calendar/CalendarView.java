package pl.pzagawa.extended.ui.calendar;

import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class CalendarView
	extends LinearLayout
{
	public final static int DAYS_IN_WEEK = 7;
	public final static int WEEK_ROWS = 6;
	public final static int WEEK_ROWS_WITHOUT_LAST = 5;
	public final static int TOTAL_DAYS = DAYS_IN_WEEK * WEEK_ROWS;
	public final static int TOTAL_DAYS_WITHOUT_LAST_ROW = DAYS_IN_WEEK * WEEK_ROWS_WITHOUT_LAST;
	
	/**
	 * 
	 * OnCursorRequest wywoływane przed wyświetleniem widoku miesiąca.
	 * 
	 * Parametry: data początkowa i końcowa zakresu dla którego należy zwrócić kursor
	 * będący listą określającą konfigurację dnia dla podanej daty. 
	 *
	 * Pola zwracanego kursora używane są przez obiekt DayDetailsData:
	 * • DAY_DATE: data dnia w postaci tekstowej "yyyy-MM-dd"
	 * • ITEMS_COUNT: liczba zdarzeń w danym dniu
	 * • THEME_NAME: nazwa wybranego tematu kolorystycznego wg zdefinowanego zestawu w DayDetailsTheme
	 * 
	 * OnCursorError wywoływane w przypadku wystąpienia błędu przetwarzania danych z kursora.
	 * 
	 */
	public interface OnCursorRequestListener
	{
		Cursor OnCursorRequest(Calendar calBegin, Calendar calEnd);
		void OnCursorError(String errorMessage);
	}

	/**
	 * 
	 * Wywoływane po wybraniu dnia na widoku miesiąca.
	 * 
	 * Parametr zdarzenia zwraca obiekt opisu właściwości wybranego dnia, min. datę oraz dane szczegółowe.
	 *
	 */
	public interface OnDaySelectedListener
	{
		void onDaySelected(DayData dataDay);
	}
	
	/**
	 * 
	 * Wywoływane w czasie aktualizacji widoku po np. zmianie miesiąca dla każdego dnia miesiąca przed rysowaniem.
	 * 
	 * Parametr zwraca obiekty danych aktualnie przetwarzanego dnia.
	 * 
	 */
	public interface OnDayUpdateListener
	{
		void OnDayUpdate(final DayData day);
	}

	/**
	 * 
	 * Wywoływane w czasie aktualizacji widoku po np. zmianie miesiąca.
	 * 
	 * Parametr zawiera datę aktualnie wyświetlanego miesiąca.
	 *
	 */
	public interface OnViewChangeListener
	{
		void OnViewDateChange(Calendar viewDate);
	}
	
	//fields
    protected final Attributes attributes;
    protected final DataModel dataModel;
    
    private MonthViewControlBar controlBar;
    private MonthView monthView;
    private WeekDaysView weekDaysView;
        
    private OnDaySelectedListener OnDaySelected = null;
    private OnViewChangeListener OnViewChange = null;
    
    //ctor used for creating instance in code
	public CalendarView(Context context)
	{
		super(context);

        attributes = new Attributes(context, null);
    	dataModel = new DataModel();
		
		initialize(context, null);
	}

	//ctor used for creating instance via xml layout
    public CalendarView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        attributes = new Attributes(context, attrs);
    	dataModel = new DataModel();

		initialize(context, attrs);
    }
	
    private void initialize(Context context, AttributeSet attrs)
    {	    
	    controlBar = new MonthViewControlBar(this);	    	    
	    monthView = new MonthView(this);
	    weekDaysView = new WeekDaysView(this);

	    this.setOrientation(LinearLayout.VERTICAL);

	    this.addView(controlBar);
	    this.addView(weekDaysView);
	    this.addView(monthView);	    

	    showControlBar(attributes.isControlBarVisible());
	    showDayNamesBar(attributes.isDayNamesBarVisible());
	    setItemsVisibilityType(attributes.getItemsVisibilityType());

	    setViewToday();	    
    }
	
    protected void onPrevDayClick()
	{
		dataModel.setPrevMonth();
		updateHeight();		
		invalidateAll();
		
		if (OnViewChange != null)
			OnViewChange.OnViewDateChange(dataModel.getDate());
	}

    protected void onNextDayClick()
	{
		dataModel.setNextMonth();
		updateHeight();
		invalidateAll();
		
		if (OnViewChange != null)
			OnViewChange.OnViewDateChange(dataModel.getDate());
	}

    protected void onCurrentDayClick()
	{
		dataModel.setToday();
		updateHeight();
		invalidateAll();
		
		if (OnViewChange != null)
			OnViewChange.OnViewDateChange(dataModel.getDate());		
	}

    protected void onSelectDayClick(DayData dayData)
	{
		final int year = dayData.getYear();
		final int monthOfYear = dayData.getMonth();
		final int dayOfMonth = dayData.getDay();
		
		dataModel.selectDay(year, monthOfYear, dayOfMonth);
		
		updateHeight();
		invalidateAll();

		//get day data item from grid coords, in case when view has been switched to other month 
		final DayData selectedDay = dataModel.getDayData(year, monthOfYear, dayOfMonth);
		
		if (OnDaySelected != null)
			OnDaySelected.onDaySelected(selectedDay);		
	}
	
	public void update()
	{
		dataModel.update();
		updateHeight();		
		invalidateAll();
	}
	
	private void updateHeight()
	{
		if (attributes.isFixedRowsCount())
			return;
		
		monthView.requestLayout();		
		this.requestLayout();
	}

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);        
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        
        if (!attributes.isFixedRowsCount())
        {
			if (heightMode == MeasureSpec.EXACTLY)
			{       				
            	heightSize = getHeightWithoutMonthView();
            	heightSize += monthView.getMeasuredHeight();
			}
        }

        setMeasuredDimension(widthSize, heightSize);
    }
	    
	//use if you want to process each day data manually
	public void setOnDayUpdateListener(OnDayUpdateListener onDayUpdate)
	{
		dataModel.setOnDayUpdateListener(onDayUpdate);
	}

	//use if you want to set data at once for whole view
	public void setOnCursorRequestListener(OnCursorRequestListener OnDataRequest)
	{
		dataModel.setOnCursorRequestListener(OnDataRequest);
	}
	
	//event for day selection
	public void setOnDaySelectedListener(OnDaySelectedListener OnDaySelected)
	{
		this.OnDaySelected = OnDaySelected;
	}

	//event for view change
	public void setOnViewChangeListener(OnViewChangeListener OnViewChange)
	{
		this.OnViewChange = OnViewChange;
	}

	//set reference date for today
	public void setTodayReferenceDate(Calendar referenceDate)
	{
		dataModel.setTodayReferenceDate(referenceDate);
		invalidateAll();		
	}
	
	//update view to show given date
	public void setViewToDate(int year, int monthOfYear, int dayOfMonth)
	{
		dataModel.setDate(year, monthOfYear, dayOfMonth);
		invalidateAll();		
	}

	//update view to show current date
	public void setViewToday()
	{
		dataModel.setToday();
		invalidateAll();		
	}
	
	//update view to show given date and selects its day 
	public void selectDay(int year, int monthOfYear, int dayOfMonth)
	{
		dataModel.selectDay(year, monthOfYear, dayOfMonth);				
		invalidateAll();		
	}

	public void resetSelectedDay()
	{
		dataModel.resetSelectedDay();
		invalidateAll();		
	}
	
	public Calendar getSelectedDayDate()
	{
		return dataModel.getSelectedDayDate();
	}
	
	public DayData getSelectedDay()
	{
		return dataModel.getSelectedDay();
	}
	
	//value: DayData.ItemsVisibilityType
	public void setItemsVisibilityType(int value)
	{
		monthView.setItemsVisibilityType(value);
		invalidateAll();		
	}
	
	public int getItemsVisibilityType()
	{
		return monthView.getItemsVisibilityType();
	}
	
	public void showControlBar(boolean show)
	{
		controlBar.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	public void showDayNamesBar(boolean show)
	{
		weekDaysView.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	protected int getHeightWithoutMonthView()
	{
    	int height = 0;

    	if (controlBar.getVisibility() == View.VISIBLE)
    		height += attributes.getControlBarHeight();

    	if (weekDaysView.getVisibility() == View.VISIBLE)
    		height += attributes.getDayNamesBarHeight();

    	return height;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state)
	{		
	    if (state instanceof CalendarViewState)
	    {
		    CalendarViewState calendarViewState = (CalendarViewState)state;		    
		    calendarViewState.restore(dataModel);
		    
		    super.onRestoreInstanceState(calendarViewState.getSuperState());
	    	return;
	    }

    	super.onRestoreInstanceState(state);
	}

	@Override
	protected Parcelable onSaveInstanceState()
	{
		Parcelable superState = super.onSaveInstanceState();
		
		CalendarViewState calendarViewState = new CalendarViewState(superState);		
		calendarViewState.save(dataModel);
		
		return calendarViewState;
	}

	private void invalidateAll()
	{
		this.invalidate();
		controlBar.invalidate();
	}
	
}
