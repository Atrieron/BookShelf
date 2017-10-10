package main.java.dao;

public enum ReadFilterStatus {
	DOESNT_MATTER("Doesn't matter"), READ("Yes"), NOT_READ("No");
	
	private String view;

	private ReadFilterStatus(String view) {
		this.view = view;
	}
	
	public String toString()
	{
		return view;
	}
}
