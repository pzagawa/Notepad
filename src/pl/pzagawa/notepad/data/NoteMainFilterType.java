package pl.pzagawa.notepad.data;

public enum NoteMainFilterType
{
	INVALID(-1),
	
	BY_CREATE_DATE(0),
	BY_REMINDER_DATE(1),
	BY_TAGS(2);
	
	public final int index;
	
	NoteMainFilterType(int index)
	{
		this.index = index;
	}

}
