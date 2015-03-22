package pl.pzagawa.extended.ui.calendar;

public class DayDetailsData
{
	//date of a day within requested calendar date range, formatted to: "yyyy-MM-dd"
	public final static String FIELD_DAY_DATE = "DAY_DATE";
	
	//count of items for a day
	public final static String FIELD_ITEMS_COUNT = "ITEMS_COUNT";
	
	//color theme to display (light, yellow, orange, red, blue, green), can be empty: ""	
	public final static String FIELD_THEME_NAME = "THEME_NAME";
	
	public final static String[] QUERY_FIELDS =
	{
		FIELD_DAY_DATE,
		FIELD_ITEMS_COUNT,
		FIELD_THEME_NAME,
	};
	
	private DayDetailsTheme theme = DayDetailsTheme.YELLOW;
	private int itemsCount = 0;

	public DayDetailsData()
	{
	}
	
	protected void reset()
	{
		itemsCount = 0;
		theme = DayDetailsTheme.YELLOW;
	}
	
	public void setTheme(DayDetailsTheme value)
	{
		this.theme = value;
	}
	
	public void setTheme(String name)
	{
		final DayDetailsTheme theme = DayDetailsTheme.customizedValueOf(name);
		
		if (theme == null)
			return;
						
		setTheme(theme);
	}
	
	public void setItemsCount(int value)
	{
		this.itemsCount = value;
	}

	public int getItemsCount()
	{
		return itemsCount;
	}

	public DayDetailsTheme getTheme()
	{
		return theme;
	}
	
}
