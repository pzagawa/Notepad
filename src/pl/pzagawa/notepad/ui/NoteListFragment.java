package pl.pzagawa.notepad.ui;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import android.util.Log;
import pl.pzagawa.notepad.CommonFragment;
import pl.pzagawa.notepad.R;
import pl.pzagawa.notepad.data.NoteFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoteListFragment
	extends CommonFragment
	implements Observer
{
    public NoteListFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_note_list, container, false);
        return rootView;
    }

	@Override
	public void update(Observable observable, Object data)
	{
		if (observable == getFilterFragment().getNoteFilter())
		{
			NoteFilter noteFilter = (NoteFilter)data;
			
			Log.i("NoteListFragment", "NoteFilter change: " + noteFilter.toString());
		}
	}

}
