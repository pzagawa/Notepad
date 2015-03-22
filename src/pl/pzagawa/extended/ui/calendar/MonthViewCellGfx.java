package pl.pzagawa.extended.ui.calendar;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;

class MonthViewCellGfx
{
	public final Paint penBackground;
	public final Paint penLine;
	public final Paint penText;
	public final Paint penSymbolText;
	public final Paint penSelection;
	public final Paint penStatusSymbol;

	public final Paint.FontMetrics penSymbolTextMetrics;
	public final Paint.FontMetrics penTextMetrics;

	public MonthViewCellGfx(Context context, Attributes attributes)
	{
		penBackground = new Paint();
		penBackground.setAntiAlias(false);
		penBackground.setStyle(Paint.Style.FILL);
		penBackground.setStrokeWidth(1);
		penBackground.setColor(0xffa0a0a0);	
		
		penLine = new Paint();
		penLine.setAntiAlias(true);
		penLine.setStyle(Paint.Style.FILL);
		penLine.setStrokeWidth(1);
		penLine.setColor(attributes.getDayCellBorderColor(false));	
		
		penText = new Paint();
		penText.setAntiAlias(true);
		penText.setColor(attributes.getDayCellTextColor(true, false));
		penText.setTextSize(attributes.getFontDaySize());
		penText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
		penText.setTextAlign(Paint.Align.CENTER);

		penTextMetrics = penText.getFontMetrics();
		
		penSymbolText = new Paint();
		penSymbolText.setAntiAlias(true);
		penSymbolText.setColor(attributes.getDayCellTextColor(true, false));
		penSymbolText.setTextSize(attributes.getFontSymbolSize());
		penSymbolText.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
		penSymbolText.setTextAlign(Paint.Align.CENTER);

		penSymbolTextMetrics = penSymbolText.getFontMetrics();
		
		penSelection = new Paint();
		penSelection.setAntiAlias(false);
		penSelection.setStyle(Paint.Style.FILL);		
		
		penStatusSymbol = new Paint();
		penStatusSymbol.setStyle(Paint.Style.FILL);		
		penStatusSymbol.setColor(0xff000000);
	}
	
}
