package pl.pzagawa.extended.ui.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;

class WeekDaysView
	extends View
{    
	private final static int cellBorder = 1;
	
	private final CalendarView calendarView;
	
	public final Paint penBkg;
	public final Paint penText;	
	public final Paint.FontMetrics penTextMetrics;

	private final RectF[] rectDays = new RectF[CalendarView.DAYS_IN_WEEK];
	private final RectF[] rectDaysBackgrounds = new RectF[CalendarView.DAYS_IN_WEEK];	
	
	private WeekDaysView(Context context)
	{
		super(context);
		
		calendarView = null;
		
		penBkg = null;
		penText = null;
		penTextMetrics = null;
	}
	
	public WeekDaysView(CalendarView calendarView)
	{
		super(calendarView.getContext());

		this.calendarView = calendarView;
		
		penBkg = new Paint();
		penBkg.setAntiAlias(false);
		penBkg.setStyle(Paint.Style.FILL);
		penBkg.setColor(0xff802020);
		
		penText = new Paint();
		penText.setAntiAlias(true);
		penText.setColor(calendarView.attributes.getDayNamesTextColor());
		penText.setTextSize(calendarView.attributes.getFontDayNamesSize());
		penText.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));		
		penText.setTextAlign(Paint.Align.CENTER);

		penTextMetrics = penText.getFontMetrics();		

		createRectangles();
		
		setHeight(calendarView.attributes.getDayNamesBarHeight());
	}

	private void setHeight(int height)
	{
	    ViewGroup.LayoutParams layParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
		this.setLayoutParams(layParams);
	}
	
	private void createRectangles()
	{	
		for (int columnIndex = 0; columnIndex < CalendarView.DAYS_IN_WEEK; columnIndex++)
		{
			rectDays[columnIndex] = new RectF();
			rectDaysBackgrounds[columnIndex] = new RectF();
		}
	}
	
	private void initializeRectangles(int width, int height)
	{
		final float cellWidth = width / CalendarView.DAYS_IN_WEEK;
		final float cellHeight = this.getHeight();

		//offset for centering drawing in view
		final float horizontalOffset = (width - (cellWidth * CalendarView.DAYS_IN_WEEK)) * 0.5f;
		
		for (int columnIndex = 0; columnIndex < CalendarView.DAYS_IN_WEEK; columnIndex++)
		{
			float left = (columnIndex * cellWidth) + horizontalOffset;

			rectDays[columnIndex].set(left, 0, left + cellWidth, cellHeight);
			rectDaysBackgrounds[columnIndex].set(rectDays[columnIndex]);
			rectDaysBackgrounds[columnIndex].inset(cellBorder, cellBorder);
		}
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		final int width = getWidth();
		final int height = getHeight();

		initializeRectangles(width, height);

		for (int columnIndex = 0; columnIndex < CalendarView.DAYS_IN_WEEK; columnIndex++)
		{
			final RectF rect = rectDays[columnIndex];

			//draw background
			if (calendarView.attributes.isDayNamesBackground())
			{
				final RectF rectBkg = rectDaysBackgrounds[columnIndex];
				penBkg.setColor(calendarView.attributes.getDayNameBkgColor(MonthView.isHoliday(columnIndex)));
				canvas.drawRect(rectBkg, penBkg);
			}
			
			//draw text
			penText.setColor(calendarView.attributes.getDayNamesTextColor());

			if (columnIndex == 5 || columnIndex == 6)
				penText.setColor(0xffFF4444);			
			
			float textLeft = rect.centerX();
			float textTop = rect.bottom - Math.abs(penTextMetrics.bottom);

			final String titleText = calendarView.attributes.getWeekDayName(columnIndex);			
			
			canvas.drawText(titleText, textLeft, textTop, penText);
		}
	}
	
}
