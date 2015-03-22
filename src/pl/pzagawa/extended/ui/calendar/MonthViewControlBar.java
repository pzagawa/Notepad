package pl.pzagawa.extended.ui.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

class MonthViewControlBar
	extends View
{
	private final static float ARROW_WIDTH_FACTOR = 0.35f;
	private final static float ARROW_PADDING = 11f;
	
	private final CalendarView calendarView;
	
	private final Paint penArrow;
	private final Paint penText;
	private final Paint.FontMetrics penTextMetrics;
	
	private final Drawable iconLeft;
	private final Drawable iconRight;
	
	private final RectF rectPrev = new RectF();
	private final RectF rectTitle = new RectF();
	private final RectF rectNext = new RectF();

	private MonthViewControlBar(Context context)
	{
		super(context);
		
		calendarView = null;
		
		penArrow = null;
		penText = null;
		penTextMetrics = null;
		
		iconLeft = null;
		iconRight = null;
	}

	public MonthViewControlBar(CalendarView calendarView)
	{
		super(calendarView.getContext());
		
		this.calendarView = calendarView;
		
		penArrow = new Paint();
		penArrow.setAntiAlias(true);
		penArrow.setStyle(Paint.Style.FILL);		
		penArrow.setColor(calendarView.attributes.getArrowColor());
		
		penText = new Paint();
		penText.setAntiAlias(true);
		penText.setColor(calendarView.attributes.getTitleTextColor());
		penText.setTextSize(calendarView.attributes.getFontTitleSize());
		
		penText.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
		penText.setTextAlign(Paint.Align.CENTER);

		penTextMetrics = penText.getFontMetrics();
		
		iconLeft = calendarView.attributes.getArrowIconLeft();
		iconRight = calendarView.attributes.getArrowIconRight();
		
		setHeight(calendarView.attributes.getControlBarHeight());
	}
	
	private void setHeight(int height)
	{
	    ViewGroup.LayoutParams layParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
		this.setLayoutParams(layParams);
	}
	
	private void initializeRectangles(int width, int height)
	{
		final int size = height;

		rectPrev.set(0, 0, size, size);
		rectNext.set(width - size, 0, width, size);
		rectTitle.set(size, 0, rectNext.left, size);
	}
	
	private Path createPathArrowLeft(RectF rect, int size)
	{
		Path arrowPath = new Path();
		
		float arrowWidth = size * ARROW_WIDTH_FACTOR;
		float arrowCenter = (rect.width() - arrowWidth) * 0.5f;		
		float padding = calendarView.attributes.dm.density * ARROW_PADDING;

		arrowPath.moveTo(rect.left + arrowCenter, rect.centerY());
		arrowPath.lineTo(rect.left + arrowCenter + arrowWidth, rect.top + padding);
		arrowPath.lineTo(rect.left + arrowCenter + arrowWidth, rect.bottom - padding);
		arrowPath.lineTo(rect.left + arrowCenter, rect.centerY());

		return arrowPath;
	}

	private Path createPathArrowRight(RectF rect, int size)
	{
		Path arrowPath = new Path();
		
		float arrowWidth = size * ARROW_WIDTH_FACTOR;
		float arrowCenter = (rect.width() - arrowWidth) * 0.5f;
		float padding = calendarView.attributes.dm.density * ARROW_PADDING;
		
		arrowPath.moveTo(rect.right - arrowWidth - arrowCenter, rect.top + padding);
		arrowPath.lineTo(rect.right - arrowCenter, rect.centerY());
		arrowPath.lineTo(rect.right - arrowWidth - arrowCenter, rect.bottom - padding);
		arrowPath.lineTo(rect.right - arrowWidth - arrowCenter, rect.top + padding);

		return arrowPath;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		final int width = getWidth();
		final int height = getHeight();

		initializeRectangles(width, height);
		
		if (iconLeft != null && iconRight != null)
		{	
			//draw icons
			int iconLeft_X1 = (int) (rectPrev.centerX() - (iconLeft.getIntrinsicWidth() * 0.5f));
			int iconLeft_Y1 = (int) (rectPrev.centerY() - (iconLeft.getIntrinsicHeight() * 0.5f));
			int iconLeft_X2 = (int) (iconLeft_X1 + iconLeft.getIntrinsicWidth());
			int iconLeft_Y2 = (int) (iconLeft_Y1 + iconLeft.getIntrinsicHeight());
			
			iconLeft.setBounds(iconLeft_X1, iconLeft_Y1, iconLeft_X2, iconLeft_Y2);
			iconLeft.draw(canvas);

			int iconRight_X1 = (int) (rectNext.centerX() - (iconRight.getIntrinsicWidth() * 0.5f));
			int iconRight_Y1 = (int) (rectNext.centerY() - (iconRight.getIntrinsicHeight() * 0.5f));
			int iconRight_X2 = (int) (iconRight_X1 + iconRight.getIntrinsicWidth());
			int iconRight_Y2 = (int) (iconRight_Y1 + iconRight.getIntrinsicHeight());
			
			iconRight.setBounds(iconRight_X1, iconRight_Y1, iconRight_X2, iconRight_Y2);
			iconRight.draw(canvas);			
		}
		else
		{
			//draw arrows
			final int arrowSize = height;
			
			//initialize button shape path
			final Path pathArrowLeft = createPathArrowLeft(rectPrev, arrowSize);
			final Path pathArrowRight = createPathArrowRight(rectNext, arrowSize);
			
			//draw button shapes
			canvas.drawPath(pathArrowLeft, penArrow);
			canvas.drawPath(pathArrowRight, penArrow);
		}
				
		//draw title
		float textHeight = Math.abs(penTextMetrics.ascent) - penTextMetrics.descent; 
		float textLeft = rectTitle.centerX();
		float textTop = rectTitle.centerY() + (textHeight / 2);
		
		if (iconLeft != null && iconRight != null)
		{
			//layout for icons
			String titleTextLeft = calendarView.attributes.getMonthTitleTextLeft(calendarView.dataModel.getDate());
			
			penText.setTextAlign(Paint.Align.LEFT);
			penText.setColor(0xffCC0000);
			
			canvas.drawText(titleTextLeft, rectTitle.left, textTop, penText);
			
			String titleTextRight = calendarView.attributes.getMonthTitleTextRight(calendarView.dataModel.getDate());

			penText.setTextAlign(Paint.Align.RIGHT);
			penText.setColor(0xffFF4444);
			
			canvas.drawText(titleTextRight, rectTitle.right, textTop, penText);			
		}
		else
		{
			//layout for arrows
			String titleText = calendarView.attributes.getMonthTitleText(calendarView.dataModel.getDate());

			canvas.drawText(titleText, textLeft, textTop, penText);			
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
			if (rectPrev.contains(x, y))
			{
				calendarView.onPrevDayClick();
				return true;
			}
			
			if (rectNext.contains(x, y))
			{
				calendarView.onNextDayClick();
				return true;
			}
			
			if (rectTitle.contains(x, y))
			{
				calendarView.onCurrentDayClick();
				return true;
			}
		}
		
		return false;
	}
    
}
