package pl.pzagawa.notepad.data;

public enum NoteListFilterType
{
	//unset
	NONE(0),
	
	//BY REMINDER
	OVERDUE(1),
	TODAY(2),
	THIS_WEEK(3),
	THIS_MONTH(4),
	
	//BY TAGS	
	PERSONAL(10),
	HOME(11),
	WORK(12),
	TODO(13),
	STARRED(14),
	DONE(15);	

	public final int id;

	private NoteListFilterType(int id)
	{
		this.id = id;
	}
	
}
