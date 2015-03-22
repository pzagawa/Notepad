package pl.pzagawa.extended.ui.calendar;

import java.util.Calendar;

import pl.pzagawa.extended.ui.calendar.MonthView.ItemsVisibilityType;
import pl.pzagawa.notepad.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;

class Attributes
{
	public final DisplayMetrics dm = new DisplayMetrics();
		
	private final String[] monthNames;
	private final String[] daysNames;
	
	//constant default values
	private final static float FONT_DAY_SIZE = 20.0f;
	private final static float FONT_TITLE_SIZE = 18.0f;
	private final static float FONT_SYMBOL_SIZE = 12.0f;
	private final static float FONT_DAYNAMES_SIZE = 12.0f;
	
	private final static float SYMBOL_BOOKMARK_SIZE = 8f;
	private final static float SYMBOL_DOT_SIZE = 3.5f;
	
	private final static int CONTROL_BAR_HEIGHT = 40;
	private final static int DAYNAMES_HEIGHT = 24;	
	
	//custom values red from xml definition
	private float fontDaySize = 0;
	private float fontTitleSize = 0;
	private float fontSymbolSize = 0;
	
	private int symbolBookmarkSize = 0;
	private int symbolDotSize = 0;

	private boolean controlBarVisible = true;
	private int controlBarHeight = 0;	
	private int controlBarArrowColor = 0;
	private int controlBarTitleColor = 0;	
	
	private int itemsVisibilityType = ItemsVisibilityType.DIGITS;

	private float fontDayNamesSize = 0;
	private boolean dayNamesVisible = true;
	private boolean dayNamesBackground = false;
	private int dayNamesHeight = 0;
	private int dayNamesTextColor = 0;

	private boolean fixedRowsCount = true;
	private int layoutHeight = 0;
	
	//drawables
	private Drawable controlBarArrowIconLeft = null;
	private Drawable controlBarArrowIconRight = null;
	
	//ICS colors
	protected int WHITE = 0xffffffff;
	protected int BLACK = 0xff000000;
	protected int GRAY = 0xff888888;
	
	protected int LIGHT_BLUE = 0xff33B5E5;
	protected int LIGHT_ROSE = 0xffAA66CC;
	protected int LIGHT_GREEN = 0xff99CC00;
	protected int LIGHT_ORANGE = 0xffFFBB33;
	protected int LIGHT_RED = 0xffFF4444;

	protected int BLUE = 0xff0099CC;
	protected int ROSE = 0xff9933CC;
	protected int GREEN = 0xff669900;
	protected int ORANGE = 0xffFF8800;
	protected int RED = 0xffCC0000;
	
	//custom colors
	protected int DAY_BKG_CURRENT_MONTH = 0xffd8d8d8;
	protected int DAY_BKG_OTHER_MONTH = 0xffe8e8e8;
	
	protected int DAY_TEXT_CURRENT_MONTH = 0xff404040;
	protected int DAY_TEXT_OTHER_MONTH = 0xffa0a0a0;

	protected int CONTROL_BAR_ARROW = 0xffc0c0c0;

	protected int SELECTION_LIGHT = 0xff0b84ff;
	protected int SELECTION_DARK = 0xff0050E0;

	protected int SYMBOL_TEXT_CURRENT_MONTH = 0xff0066cc;
	protected int SYMBOL_TEXT_OTHER_MONTH = 0xff606060;
	protected int SYMBOL_BKG_CURRENT_MONTH = 0xffffbb33;
	protected int SYMBOL_BKG_OTHER_MONTH = 0xff808080;

	protected int DAYNAME_BKG = 0xff202020;
	protected int DAYNAME_BKG_HOLIDAY = 0xff401010;
	
	public Attributes(Context context, AttributeSet attrs)
	{
		final WindowManager wm = (WindowManager)context.getSystemService(Activity.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		
		monthNames = context.getResources().getStringArray(R.array.CalendarViewMonthNames);		
		daysNames = context.getResources().getStringArray(R.array.CalendarViewDayNames);
		
		if (attrs == null)		
		{
			setDefaultXmlAttributesValues(context);			
		}		
		else
		{
			processXmlAttributes(context, attrs);
		}
	}

	private void setDefaultXmlAttributesValues(Context context)
    {
        fontDaySize = FONT_DAY_SIZE * dm.scaledDensity;
        fontTitleSize = FONT_TITLE_SIZE * dm.scaledDensity;

        fontSymbolSize = FONT_SYMBOL_SIZE * dm.scaledDensity;
        symbolBookmarkSize = (int)(SYMBOL_BOOKMARK_SIZE * dm.density);
        symbolDotSize = (int)(SYMBOL_DOT_SIZE * dm.density);

        controlBarVisible = true;
        controlBarHeight = (int) (CONTROL_BAR_HEIGHT * dm.scaledDensity);;
        controlBarArrowColor = CONTROL_BAR_ARROW;
        controlBarTitleColor = LIGHT_ORANGE;

        itemsVisibilityType = ItemsVisibilityType.NONE;

        dayNamesVisible = true;
        fontDayNamesSize = FONT_DAYNAMES_SIZE * dm.scaledDensity;
        dayNamesHeight = (int) (DAYNAMES_HEIGHT * dm.scaledDensity);;
        dayNamesTextColor = GRAY;
    	dayNamesBackground = false;

		fixedRowsCount = true;
		layoutHeight = 0;
    }
	
	private void processXmlAttributes(Context context, AttributeSet attrs)
    {
        final TypedArray items = context.obtainStyledAttributes(attrs, R.styleable.CalendarViewAttributes);

        fontDaySize = items.getDimensionPixelSize(R.styleable.CalendarViewAttributes_calendarDayTextSize, (int)(FONT_DAY_SIZE * dm.scaledDensity));
        fontTitleSize = items.getDimensionPixelSize(R.styleable.CalendarViewAttributes_calendarTitleTextSize, (int)(FONT_TITLE_SIZE * dm.scaledDensity));

        fontSymbolSize = items.getDimensionPixelSize(R.styleable.CalendarViewAttributes_calendarSymbolTextSize, (int)(FONT_SYMBOL_SIZE * dm.scaledDensity));                
        symbolBookmarkSize = items.getDimensionPixelSize(R.styleable.CalendarViewAttributes_calendarSymbolBookmarkSize, (int)(SYMBOL_BOOKMARK_SIZE * dm.density));
        symbolDotSize = items.getDimensionPixelSize(R.styleable.CalendarViewAttributes_calendarSymbolDotSize, (int)(SYMBOL_DOT_SIZE * dm.density));

        controlBarVisible = items.getBoolean(R.styleable.CalendarViewAttributes_calendarControlBarVisible, true);
        controlBarHeight = items.getDimensionPixelSize(R.styleable.CalendarViewAttributes_calendarControlBarHeight, (int)(CONTROL_BAR_HEIGHT * dm.density));
        controlBarArrowColor = items.getColor(R.styleable.CalendarViewAttributes_calendarControlBarArrowColor, CONTROL_BAR_ARROW);
        controlBarTitleColor = items.getColor(R.styleable.CalendarViewAttributes_calendarControlBarTitleColor, LIGHT_ORANGE);

        if (items.hasValue(R.styleable.CalendarViewAttributes_calendarControlBarArrowIconLeft))
        	controlBarArrowIconLeft = items.getDrawable(R.styleable.CalendarViewAttributes_calendarControlBarArrowIconLeft);
        
        if (items.hasValue(R.styleable.CalendarViewAttributes_calendarControlBarArrowIconRight))
        	controlBarArrowIconRight = items.getDrawable(R.styleable.CalendarViewAttributes_calendarControlBarArrowIconRight);

        itemsVisibilityType = items.getInt(R.styleable.CalendarViewAttributes_calendarItemsVisibilityType, ItemsVisibilityType.NONE);

        dayNamesVisible = items.getBoolean(R.styleable.CalendarViewAttributes_calendarDayNamesVisible, true);
        fontDayNamesSize = items.getDimensionPixelSize(R.styleable.CalendarViewAttributes_calendarDayNamesTextSize, (int)(FONT_DAYNAMES_SIZE * dm.scaledDensity));
        dayNamesHeight = items.getDimensionPixelSize(R.styleable.CalendarViewAttributes_calendarDayNamesHeight, (int)(DAYNAMES_HEIGHT * dm.density));
        dayNamesTextColor = items.getColor(R.styleable.CalendarViewAttributes_calendarDayNamesTextColor, GRAY);
    	dayNamesBackground = items.getBoolean(R.styleable.CalendarViewAttributes_calendarDayNamesBackground, false);

		fixedRowsCount = items.getBoolean(R.styleable.CalendarViewAttributes_calendarFixedRowsCount, true);

        items.recycle();
        
		//get layout fixed height if rows count set to be variable
		if (!fixedRowsCount)
		{
			layoutHeight = readLayoutHeightSize(context, attrs);		
		}		
    }
	
	private int readLayoutHeightSize(Context context, AttributeSet attrs)
	{
		int value = 0;
		
		final int[] layoutHeightAttr = new int[] { android.R.attr.layout_height };
		final int layoutHeightAttrIndex = 0;
		
		TypedArray a = context.obtainStyledAttributes(attrs, layoutHeightAttr);
		
		try
		{
			value = a.getDimensionPixelSize(layoutHeightAttrIndex, 0);
		}
		catch(UnsupportedOperationException e)
		{
			throw new RuntimeException("CalendarView: because fixedRowsCount is enabled, layout_height must be set to fixed size."); 
		}

		a.recycle();
		
		return value;
	}
	
	public int getFixedLayoutHeight()
	{
		return layoutHeight;
	}
	
	public String getMonthName(Calendar date)
	{
		final int monthIndex = date.get(Calendar.MONTH);
		
		return monthNames[monthIndex];
	}
	
	public String getMonthTitleText(Calendar date)
	{
		return String.format("%1$s %2$tY", getMonthName(date), date);
	}

	public String getMonthTitleTextLeft(Calendar date)
	{
		return getMonthName(date);
	}

	public String getMonthTitleTextRight(Calendar date)
	{
		return String.format("%1$tY", date);
	}
	
	public String getWeekDayName(int columnIndex)
	{
		return daysNames[columnIndex];
	}
	
	public float getFontDaySize()
	{
		return fontDaySize;
	}
	
	public float getFontTitleSize()
	{
		return fontTitleSize;
	}
	
	public float getFontSymbolSize()
	{
		return fontSymbolSize;
	}

	public int getSymbolBookmarkSize()
	{
		return symbolBookmarkSize;
	}

	public int getSymbolDotSize()
	{
		return symbolDotSize;
	}
	
	public boolean isControlBarVisible()
	{
		return controlBarVisible;
	}

	public int getControlBarHeight()
	{
		return controlBarHeight;
	}
	
	public int getArrowColor()
	{
		return controlBarArrowColor;
	}

	public int getTitleTextColor()
	{
		return controlBarTitleColor;
	}

	public int getItemsVisibilityType()
	{
		return itemsVisibilityType;
	}

	public int getDayCellBackgroundColor(boolean isCurrentMonth)
	{
		if (isCurrentMonth)
			return DAY_BKG_CURRENT_MONTH;
		
		return DAY_BKG_OTHER_MONTH;
	}
	
	public int getDayCellTextColor(boolean isCurrentMonth, boolean isSelected)
	{		
		if (isCurrentMonth)
			return DAY_TEXT_CURRENT_MONTH;
		
		if (isSelected)
			return DAY_TEXT_OTHER_MONTH;
		
		return DAY_TEXT_OTHER_MONTH;
	}

	public int getDayCellBorderColor(boolean isHoliday)
	{
		if (isHoliday)
			return LIGHT_RED;
			
		return GREEN;
	}

	public int getSelectionLightColor()
	{
		return SELECTION_LIGHT;
	}

	public int getSelectionDarkColor()
	{
		return SELECTION_DARK;
	}
	
	public int getSymbolTextColor(boolean isCurrentMonth)
	{
		if (isCurrentMonth)
			return SYMBOL_TEXT_CURRENT_MONTH;
		
		return SYMBOL_TEXT_OTHER_MONTH;
	}
	
	public int getSymbolBackgroundColor(boolean isCurrentMonth)
	{
		if (isCurrentMonth)
			return SYMBOL_BKG_CURRENT_MONTH;
		
		return SYMBOL_BKG_OTHER_MONTH;
	}
	
	public boolean isDayNamesBarVisible()
	{
		return dayNamesVisible;
	}

	public boolean isDayNamesBackground()
	{
		return dayNamesBackground;
	}
	
	public int getDayNamesBarHeight()
	{
		return dayNamesHeight;
	}
	
	public int getDayNamesTextColor()
	{
		return dayNamesTextColor;
	}

	public boolean isFixedRowsCount()
	{
		return fixedRowsCount;
	}
	
	public float getFontDayNamesSize()
	{
		return fontDayNamesSize;
	}

	public int getDayNameBkgColor(boolean isHoliday)
	{
		if (isHoliday)
			return DAYNAME_BKG_HOLIDAY;
		
		return DAYNAME_BKG;
	}

	public Drawable getArrowIconLeft()
	{
		return controlBarArrowIconLeft;
	}

	public Drawable getArrowIconRight()
	{
		return controlBarArrowIconRight;
	}
	
}
