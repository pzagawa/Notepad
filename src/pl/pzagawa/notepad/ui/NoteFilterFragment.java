package pl.pzagawa.notepad.ui;

import pl.pzagawa.notepad.CommonFragment;
import pl.pzagawa.notepad.R;
import pl.pzagawa.notepad.data.NoteFilter;
import pl.pzagawa.notepad.data.NoteMainFilterType;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

public class NoteFilterFragment
	extends CommonFragment
{
	private final FilterTabController tabController;
	
	private final FilterTabViewByAll viewByAll;
	private final FilterTabViewByReminders viewByReminders;
	private final FilterTabViewByTags viewByTags;
	
	private ViewAnimator viewAnimator = null;
	
	private NoteFilter noteFilter = new NoteFilter();

    public NoteFilterFragment()
    {
        this.tabController = new FilterTabController(this);
        
        this.viewByAll = new FilterTabViewByAll(this);
        this.viewByReminders = new FilterTabViewByReminders(this);
        this.viewByTags = new FilterTabViewByTags(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_note_filter, container, false);
        return rootView;
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		//get views
		this.viewAnimator = (ViewAnimator) getActivity().findViewById(R.id.filterTabController);

		tabController.getViews();

        viewByAll.getViews();
        viewByReminders.getViews();
		viewByTags.getViews();
		
		//setup listeners
		tabController.setOnTabSelectedListener(tabSelectionListener);
	}
	
	public void selectByDateFilter()
	{
		tabController.selectTab(0);
	}
	
	private FilterTabController.OnTabSelectedListener tabSelectionListener = new FilterTabController.OnTabSelectedListener()
	{
		@Override
		public void onClick(NoteMainFilterType filterType)
		{
			//TAB1
			if (filterType == NoteMainFilterType.BY_CREATE_DATE)
			{
				//select TODAY on calendar
                viewByAll.selectToday();

				//show notes for day selected on calendar
				noteFilter.setByCreateDate(viewByAll.getSelectedDayDate());
			}
			
			//TAB2
			if (filterType == NoteMainFilterType.BY_REMINDER_DATE)
			{								
				//select OVERDUE on list
                viewByReminders.selectOverdue();
								
				//show notes with reminders for today
				noteFilter.setByReminderDate(viewByReminders.getStartDate(), viewByReminders.getEndDate());
			}
			
			//TAB3
			if (filterType == NoteMainFilterType.BY_TAGS)
			{
				//select PERSONAL tag on list
				viewByTags.selectPersonalTag();

				//show notes with selected tag
				noteFilter.setByTags(viewByTags.getFilterListType());
			}

			//show tab view by index
			viewAnimator.setDisplayedChild(filterType.index);
		}
	};
	
	public NoteFilter getNoteFilter()
	{
		return noteFilter;
	}

}
