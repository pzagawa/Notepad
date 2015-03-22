package pl.pzagawa.extended.ui.calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

class MonthView
	extends View
{
	public interface ItemsVisibilityType
	{
		int NONE = 0;
		int DOT = 1;
		int BOOKMARK = 2;
		int DIGITS = 3;
	};
	
	private final CalendarView calendarView;
	
	private final MonthViewCell[] cells = new MonthViewCell[CalendarView.TOTAL_DAYS];	
	private final MonthViewCellGfx dayCellGfx;
	
	private int itemsVisibilityType = ItemsVisibilityType.NONE;
	
	private MonthView(Context context)
	{
		super(context);

		this.calendarView = null;		
		this.dayCellGfx = null;
	}
	
	public MonthView(CalendarView calendarView)
	{
		super(calendarView.getContext());
		
		this.calendarView = calendarView;
		this.dayCellGfx = new MonthViewCellGfx(calendarView.getContext(), calendarView.attributes);
		
		createCells();

		setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
	}
		
	private void setHeight(int height)
	{
	    ViewGroup.LayoutParams layParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
		this.setLayoutParams(layParams);
	}
	
	private void createCells()
	{	
		for (int cellIndex = 0; cellIndex < CalendarView.TOTAL_DAYS; cellIndex++)
		{
			final DayData dayData = calendarView.dataModel.getDayData(cellIndex);
			final MonthViewCell cell = new MonthViewCell(this, dayData, dayCellGfx, calendarView.attributes);			
			cells[cellIndex] = cell;
		}
	}
	
	private void initializeRectangles(int width, int height)
	{	
		final float cellWidth = width / CalendarView.DAYS_IN_WEEK;

		//calculate cell height according to layout mode
		if (!calendarView.attributes.isFixedRowsCount())
		{
			height = calendarView.attributes.getFixedLayoutHeight() - calendarView.getHeightWithoutMonthView();					
		}
		
		final float cellHeight = height / CalendarView.WEEK_ROWS;				
		
		//offset for centering drawing in view 
		final float horizontalOffset = (width - (cellWidth * CalendarView.DAYS_IN_WEEK)) * 0.5f;
		
		int columnIndex = 0;
		int rowIndex = 0;
		
		for (int cellIndex = 0; cellIndex < CalendarView.TOTAL_DAYS; cellIndex++)
		{
			final MonthViewCell cell = cells[cellIndex];
			
			columnIndex = cellIndex % CalendarView.DAYS_IN_WEEK;

			float left = (columnIndex * cellWidth) + horizontalOffset;
			float top = rowIndex * cellHeight;
					
			cell.set(left, top, left + cellWidth, top + cellHeight);

			if (columnIndex == 6)
				rowIndex++;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		int count = 0; 
		
		if (calendarView.attributes.isFixedRowsCount())
		{
			count = CalendarView.TOTAL_DAYS;
		}
		else
		{
			count = calendarView.dataModel.isLastRowShowingOtherMonth() ? CalendarView.TOTAL_DAYS_WITHOUT_LAST_ROW : CalendarView.TOTAL_DAYS;			
		}
		
		final int width = getWidth();
		final int height = getHeight();
		
		initializeRectangles(width, height);			

		for (int cellIndex = 0; cellIndex < count; cellIndex++)
		{
			final MonthViewCell cell = cells[cellIndex];
						
			cell.onDraw(canvas);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x = event.getX();
		float y = event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN)
			return true;
		
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			for (int cellIndex = 0; cellIndex < CalendarView.TOTAL_DAYS; cellIndex++)
			{
				final MonthViewCell cell = cells[cellIndex];
				if (cell.isInside(x, y))
				{
					final DayData day = cell.getData();
					calendarView.onSelectDayClick(day);
					return true;
				}
			}
		}
		
		return false;
	}

	public void setItemsVisibilityType(int value)
	{
		this.itemsVisibilityType = value;
	}

	public int getItemsVisibilityType()
	{
		return itemsVisibilityType;
	}

	protected Bitmap getDimPattern()
	{
		final Bitmap bitmap = Bitmap.createBitmap(4, 4, Bitmap.Config.ARGB_8888);
	
		int color = 0xff505050;
		
		bitmap.setPixel(2, 2, color);
		bitmap.setPixel(3, 2, color);
		bitmap.setPixel(2, 3, color);
		bitmap.setPixel(3, 3, color);
		
		return bitmap;
	}

	protected static boolean isHoliday(int columnIndex)
	{
		return columnIndex == 5 || columnIndex == 6;
	}
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);        
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (!calendarView.attributes.isFixedRowsCount())
        {        
			if (heightMode == MeasureSpec.EXACTLY)
			{					    				
				final int newHeight = calendarView.attributes.getFixedLayoutHeight() - calendarView.getHeightWithoutMonthView();
				final float cellHeight = newHeight / CalendarView.WEEK_ROWS;
				final boolean hideLastRow = calendarView.dataModel.isLastRowShowingOtherMonth();

	    		heightSize = (int) (cellHeight * (hideLastRow ? CalendarView.WEEK_ROWS_WITHOUT_LAST : CalendarView.WEEK_ROWS));
			}
	    }

        setMeasuredDimension(widthSize, heightSize);
    }
	
}
