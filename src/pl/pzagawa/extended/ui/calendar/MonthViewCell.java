package pl.pzagawa.extended.ui.calendar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;

class MonthViewCell
{
	private final static int cellBorder = 1;

	private final MonthView parent;
	private final DayData data;
	private final MonthViewCellGfx gfx;
	private final Attributes attributes;
	
	private final RectF rect = new RectF();	
	private final RectF rectBackground = new RectF();

	public MonthViewCell(MonthView parent, DayData data, MonthViewCellGfx gfx, Attributes attributes)
	{
		this.parent = parent;
		this.data = data;
		this.gfx = gfx;
		this.attributes = attributes;
	}

	public DayData getData()
	{
		return data;
	}
	
	protected void set(float left, float top, float right, float bottom)
	{
		rect.set(left, top, right, bottom);
		
		rectBackground.set(rect);
		rectBackground.inset(cellBorder, cellBorder);
	}
	
	private Path createStatusSymbol(float size)
	{		
		final RectF r = rectBackground;

		final Path path = new Path();

		final float posLeft = r.left;
		final float posTop = r.top;

		path.moveTo(posLeft, posTop);
		path.lineTo(posLeft + size, posTop);
		path.lineTo(posLeft, posTop + size);
		path.lineTo(posLeft, posTop);
				
		return path;
	}
	
	private void drawTriangleSymbol(Canvas canvas)
	{
		gfx.penStatusSymbol.setAntiAlias(false);
		gfx.penStatusSymbol.clearShadowLayer();		
		gfx.penStatusSymbol.setColor(gfx.penBackground.getColor());
		
		final int highlightSize = 2;
		
		final int symbolSize = attributes.getSymbolBookmarkSize() + highlightSize;
		
		final Path outerShape = createStatusSymbol(symbolSize);
				
		LightingColorFilter colorFilter = new LightingColorFilter(0xffffffff, 0xff202020);		
		gfx.penStatusSymbol.setColorFilter(colorFilter);
		
		canvas.drawPath(outerShape, gfx.penStatusSymbol);
		
		//draw inner shape
		final Path innerShape = createStatusSymbol(symbolSize - highlightSize);
		
		gfx.penStatusSymbol.setColor(0xff4080c0);
		gfx.penStatusSymbol.setColorFilter(null);
		
		canvas.drawPath(innerShape, gfx.penStatusSymbol);		
	}
	
	private void drawDotSymbol(Canvas canvas)
	{		
		gfx.penStatusSymbol.setAntiAlias(true);
		
		float margin = attributes.dm.density * 7;
		
		final RectF r = rectBackground;

		float left = r.left + margin;
		float top = r.top + margin;
		
		float size = attributes.getSymbolDotSize();

		gfx.penStatusSymbol.clearShadowLayer();
		
		gfx.penStatusSymbol.setColor(gfx.penBackground.getColor());

		LightingColorFilter colorFilter = new LightingColorFilter(0xff505050, 0xff000000);
		gfx.penStatusSymbol.setColorFilter(colorFilter);
						
		canvas.drawCircle(left, top, size, gfx.penStatusSymbol);
	}

	private void drawCountSymbol(Canvas canvas, DayDetailsData detailsData, boolean isCurrentMonth)
	{
		final String text = Integer.toString(detailsData.getItemsCount());
		
		final float textWidth = (int)(gfx.penSymbolText.measureText(text));
		
		final float width = (int)(textWidth + ((attributes.dm.density * 3) * 2));
		final float height = (int)(Math.abs(gfx.penSymbolTextMetrics.top) + Math.abs(gfx.penSymbolTextMetrics.bottom));

		final int border = 1;
		float marginTop = attributes.dm.density * 6;
		float round = 2;

		final RectF rect = new RectF();

		rect.left = (int)(rectBackground.centerX() - (width * 0.5f));		
		rect.top = (int)(rectBackground.top + marginTop);
		
		rect.right = (int)(rect.left + width);
		rect.bottom = (int)(rect.top + height);
		
		gfx.penStatusSymbol.clearShadowLayer();
		gfx.penStatusSymbol.setAntiAlias(true);

		//draw border
		gfx.penStatusSymbol.setColor(isCurrentMonth ? 0xffa0a0a0 : 0xffb0b0b0);
		gfx.penStatusSymbol.setStyle(Paint.Style.FILL_AND_STROKE);
		gfx.penStatusSymbol.setStrokeWidth(border + border);

		//canvas.drawRect(rect, gfx.penStatusSymbol);
		canvas.drawRoundRect(rect, round, round, gfx.penStatusSymbol);
		
		//draw background
		gfx.penStatusSymbol.setColor(isCurrentMonth ? 0xfff0f0f0 : 0xffe0e0e0);
		gfx.penStatusSymbol.setStyle(Paint.Style.FILL);

		//canvas.drawRect(rect, gfx.penStatusSymbol);
		canvas.drawRoundRect(rect, round, round, gfx.penStatusSymbol);
				
		//draw text
		gfx.penSymbolText.setAntiAlias(true);
		gfx.penSymbolText.setColor(attributes.getSymbolTextColor(isCurrentMonth));

        float textTop = rect.top - border + Math.abs(gfx.penSymbolTextMetrics.top);
		
		canvas.drawText(text, rect.centerX(), textTop, gfx.penSymbolText);
	}
	
	private void drawTodayMark(Canvas canvas)
	{
		final int markSize = (int) (attributes.dm.density * 4);

		gfx.penSelection.setShader(null);
		gfx.penSelection.setColor(0x33000000);

		canvas.drawRect(rectBackground.left, rectBackground.top, rectBackground.left + markSize, rectBackground.bottom, gfx.penSelection);
	}

	private void drawInnerShadow(Canvas canvas)
	{
		final int shadowSize = (int) (attributes.dm.density * 4);
		
		final int shadowColor = 0x66000000;
		final int shadowColorDark = 0x00000000;		
		
		final LinearGradient shadowTopGradient = new LinearGradient(0, rectBackground.top, 0, rectBackground.top + shadowSize,
			shadowColor, shadowColorDark, Shader.TileMode.MIRROR);

		gfx.penSelection.setShader(shadowTopGradient);
		canvas.drawRect(rectBackground.left, rectBackground.top, rectBackground.right, rectBackground.top + shadowSize, gfx.penSelection);
		
		final LinearGradient shadowBottomGradient = new LinearGradient(0, rectBackground.bottom, 0, rectBackground.bottom - shadowSize,
			shadowColor, shadowColorDark, Shader.TileMode.MIRROR);
		
		gfx.penSelection.setShader(shadowBottomGradient);
		canvas.drawRect(rectBackground.left, rectBackground.bottom, rectBackground.right, rectBackground.bottom - shadowSize, gfx.penSelection);
		
		final LinearGradient shadowLeftGradient = new LinearGradient(rectBackground.left, rectBackground.top, rectBackground.left + shadowSize, rectBackground.top,
			shadowColor, shadowColorDark, Shader.TileMode.MIRROR);
		
		gfx.penSelection.setShader(shadowLeftGradient);
		canvas.drawRect(rectBackground.left, rectBackground.top, rectBackground.left + shadowSize, rectBackground.bottom, gfx.penSelection);

		final LinearGradient shadowRightGradient = new LinearGradient(rectBackground.right, rectBackground.top, rectBackground.right - shadowSize, rectBackground.top,
			shadowColor, shadowColorDark, Shader.TileMode.MIRROR);

		gfx.penSelection.setShader(shadowRightGradient);
		canvas.drawRect(rectBackground.right, rectBackground.top, rectBackground.right - shadowSize, rectBackground.bottom, gfx.penSelection);
	}
	
	protected void drawPatternBkg(Canvas canvas)
	{
		final Bitmap bm = parent.getDimPattern();

		final int bmWidth = bm.getWidth();
		final int bmHeight = bm.getHeight();

		for (int y = 0; y < rectBackground.height() - bmHeight; y += bmHeight)
			for (int x = 0; x < rectBackground.width() - bmWidth; x += bmWidth)
				canvas.drawBitmap(bm, rectBackground.left + x, rectBackground.top + y, gfx.penBackground);
	}
	
	protected void onDraw(Canvas canvas)
	{
		final boolean isSelected = data.isSelected();
		final boolean isCurrentMonth = data.isCurrentMonth();
		final DayDetailsData detailsData = data.getDetailsData();
		
		final int dayBkgColor = attributes.getDayCellBackgroundColor(isCurrentMonth);
						
		//draw cell background
		gfx.penBackground.setAntiAlias(false);
		gfx.penBackground.setStyle(Paint.Style.FILL);
		gfx.penBackground.setColor(dayBkgColor);
		gfx.penBackground.setShader(null);
		
		if (isCurrentMonth)
		{
			final LinearGradient gradient = new LinearGradient(rectBackground.left, rectBackground.top, rectBackground.left, rectBackground.bottom,
				0xffe0e0e0, 0xffc0c0c0, Shader.TileMode.MIRROR);

			gfx.penBackground.setShader(gradient);
		}
		
		canvas.drawRect(rectBackground, gfx.penBackground);

        //draw day selection
		if (data.isToday())
			drawTodayMark(canvas);

		gfx.penSelection.setColor(dayBkgColor);		
		gfx.penBackground.setColor(dayBkgColor);

        //draw inner shadow for today cell
		if (isSelected)
			drawInnerShadow(canvas);
		
		//add status symbol
		if (detailsData.getItemsCount() > 0)
		{
			switch (parent.getItemsVisibilityType())
			{
			case MonthView.ItemsVisibilityType.DOT:
				drawDotSymbol(canvas);
				break;
			case MonthView.ItemsVisibilityType.BOOKMARK:
				drawTriangleSymbol(canvas);
				break;
			case MonthView.ItemsVisibilityType.DIGITS:
				drawCountSymbol(canvas, detailsData, isCurrentMonth);
				break;
			}
		}
		
		//draw text
		final int textColor = attributes.getDayCellTextColor(isCurrentMonth, isSelected);
		
		float textLeft = rectBackground.centerX();		
		float textTop = rectBackground.bottom - attributes.dm.density * 6; 

		String titleText = Integer.toString(data.getDay());
		
		gfx.penText.clearShadowLayer();
		
		if (isCurrentMonth)
		{
			gfx.penText.setColor(0xffffffff);
			canvas.drawText(titleText, textLeft, textTop + 1, gfx.penText);			
		}
		
		gfx.penText.setColor(textColor);						
		canvas.drawText(titleText, textLeft, textTop, gfx.penText);
		
		gfx.penBackground.setColorFilter(null);
	}

	protected boolean isInside(float x, float y)
	{	
		if (rect.contains(x, y))
			return true;
		
		return false;
	}
}

