package pl.pzagawa.extended.ui.calendar;

import java.util.HashMap;
import java.util.Map;

public enum DayDetailsTheme
{
	NONE(0, 0, 0, 0),
	LIGHT(0xffe0e0e0, 0xff202020, 0xff808080, 0xff303030),
	YELLOW(0xffffc040, 0xff221100, 0xff808080, 0xff303030),	
	ORANGE(0xffff8f1e, 0xff331900, 0xff808080, 0xff303030),	
	RED(0xffff8080, 0xff500000, 0xff808080, 0xff303030),
	BLUE(0xff00a0ff, 0xff14143c, 0xff808080, 0xff303030),
	GREEN(0xff70b030, 0xff002800, 0xff808080, 0xff303030);
	
	private final int bkgColor;
	private final int bkgInactiveColor;
	private final int textColor;
	private final int textInactiveColor;
	
	private final static Map<String, DayDetailsTheme> mapOfNames;
	
	static
	{
		mapOfNames = toMapOfNames();
	}
	
	DayDetailsTheme(int bkgColor, int textColor, int bkgInactiveColor, int textInactiveColor)
	{
		this.bkgColor = bkgColor;
		this.bkgInactiveColor = bkgInactiveColor;
		this.textColor = textColor;
		this.textInactiveColor = textInactiveColor;
	}

	protected int getBkgColor(boolean active)
	{
		return active ? bkgColor : bkgInactiveColor;
	}

	protected int getTextColor(boolean active)
	{
		return active ? textColor : textInactiveColor;
	}
	
	private static Map<String, DayDetailsTheme> toMapOfNames()
	{
		final Map<String, DayDetailsTheme> map = new HashMap<String, DayDetailsTheme>(); 
		
		final DayDetailsTheme[] values = values();
		
		for (DayDetailsTheme theme : values)
			map.put(theme.name().toLowerCase(), theme);
				
		return map;
	}
	
	protected static DayDetailsTheme customizedValueOf(String name)
	{
		if (name == null || name.length() == 0)
			return null;

		final String key = name.trim().toLowerCase();

		if (!mapOfNames.containsKey(key))			
			throw new RuntimeException("DayDetailsTheme.getByName(): can't find DayDetailsTheme value for key <" + name + ">");

		return mapOfNames.get(key);
	}
	
}
