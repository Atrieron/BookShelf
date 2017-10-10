package main.java.dao;

public enum ComparatorType {
	EQUAL("equal", "="), NOT_EQUAL("not equal", "!="), LESSER("before", "<"), HIGHER("after", ">"), BETWEEN("between", " between ");
	
	private String view, exp;

	private ComparatorType(String view, String exp) {
		this.view = view;
		this.exp = exp;
	}
	
	public String toString()
	{
		return view;
	}

	public String getExp() {
		return exp;
	}
}