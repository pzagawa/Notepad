package pl.pzagawa.notepad;

import pl.pzagawa.notepad.ui.NoteFilterFragment;
import pl.pzagawa.notepad.ui.NoteListFragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class NotepadActivity
	extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
    }

    @Override
	protected void onStart()
	{
    	//observe NoteFilter changes
    	getFilterFragment().getNoteFilter().addObserver(getListFragment());
    	
		//select first tab
		getFilterFragment().selectByDateFilter();
    	
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		getFilterFragment().getNoteFilter().deleteObserver(getListFragment());
		
		super.onStop();
	}

	protected NoteFilterFragment getFilterFragment()
    {
    	return (NoteFilterFragment)getFragmentManager().findFragmentById(R.id.note_filter_fragment);
    }

    protected NoteListFragment getListFragment()
    {
    	return (NoteListFragment)getFragmentManager().findFragmentById(R.id.note_list_fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_item_add_note)
		{
            //TODO: add note
		}
		
		return super.onOptionsItemSelected(item);
	}    

}
