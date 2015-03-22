package pl.pzagawa.notepad.ui;

import pl.pzagawa.notepad.CommonFragment;
import pl.pzagawa.notepad.R;
import pl.pzagawa.notepad.data.NoteMainFilterType;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

public class FilterTabController
{	
	public static interface OnTabSelectedListener
	{
		void onClick(NoteMainFilterType filterType);
	};
	
	private final CommonFragment fragment;
	
	private int selectionColor;
	
	private NoteMainFilterType selectedFilterType = NoteMainFilterType.INVALID;
	
	private OnTabSelectedListener onTabSelectedListener = null;
	
	private LinearLayout layTab1;
	private LinearLayout layTab2;
	private LinearLayout layTab3;

	private LinearLayout layTabUnderline1;
	private LinearLayout layTabUnderline2;
	private LinearLayout layTabUnderline3;
	
	public FilterTabController(CommonFragment fragment)
	{
		this.fragment = fragment;
	}

	public void setOnTabSelectedListener(OnTabSelectedListener listener)
	{
		this.onTabSelectedListener = listener;
	}
	
	public void getViews()
	{
		final Activity activity = fragment.getActivity();
		
		this.selectionColor = activity.getResources().getColor(R.color.tab_selection);
		
		this.layTab1 = (LinearLayout) activity.findViewById(R.id.layoutNoteFilterTab1);
		this.layTab2 = (LinearLayout) activity.findViewById(R.id.layoutNoteFilterTab2);
		this.layTab3 = (LinearLayout) activity.findViewById(R.id.layoutNoteFilterTab3);

		this.layTabUnderline1 = (LinearLayout) activity.findViewById(R.id.layoutNoteFilterTab1_underline);
		this.layTabUnderline2 = (LinearLayout) activity.findViewById(R.id.layoutNoteFilterTab2_underline);
		this.layTabUnderline3 = (LinearLayout) activity.findViewById(R.id.layoutNoteFilterTab3_underline);
				
		//initialize
		layTab1.setOnClickListener(onTabClickListener);
		layTab2.setOnClickListener(onTabClickListener);
		layTab3.setOnClickListener(onTabClickListener);
	}
	
	private View.OnClickListener onTabClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (v == layTab1)
				selectTab(0);
			if (v == layTab2)
				selectTab(1);
			if (v == layTab3)
				selectTab(2);
		}
	};
	
	public void selectTab(int index)
	{
		this.selectedFilterType = NoteMainFilterType.INVALID;
		
		this.layTabUnderline1.setBackgroundColor(0x00000000);	
		this.layTabUnderline2.setBackgroundColor(0x00000000);
		this.layTabUnderline3.setBackgroundColor(0x00000000);
				
		if (index == 0)
		{
			this.layTabUnderline1.setBackgroundColor(selectionColor);
			selectedFilterType = NoteMainFilterType.BY_CREATE_DATE;
		}
		
		if (index == 1)
		{
			this.layTabUnderline2.setBackgroundColor(selectionColor);
			selectedFilterType = NoteMainFilterType.BY_REMINDER_DATE;
		}
		
		if (index == 2)
		{
			this.layTabUnderline3.setBackgroundColor(selectionColor);
			selectedFilterType = NoteMainFilterType.BY_TAGS;
		}
		
		if (onTabSelectedListener != null)
			onTabSelectedListener.onClick(selectedFilterType);
	}
	
	public NoteMainFilterType getSelectedFilterType()
	{
		return selectedFilterType;
	}

}
