package pl.pzagawa.notepad;

import pl.pzagawa.notepad.ui.NoteFilterFragment;
import pl.pzagawa.notepad.ui.NoteListFragment;
import android.app.Fragment;

public class CommonFragment
	extends Fragment
{
	public CommonFragment()
	{
	}
	
	public NoteFilterFragment getFilterFragment()
    {
    	return (NoteFilterFragment)getFragmentManager().findFragmentById(R.id.note_filter_fragment);
    }

	public NoteListFragment getListFragment()
    {
    	return (NoteListFragment)getFragmentManager().findFragmentById(R.id.note_list_fragment);
    }

}
