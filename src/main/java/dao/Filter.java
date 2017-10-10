package main.java.dao;

public class Filter {
	private boolean useTitleFilter;
	private String titleFilter;
	private boolean useAuthorFilter;
	private String authorFilter;
	private ReadFilterStatus readFilter;
	private boolean useDateFilter;
	private ComparatorType dateComparatorType;
	private int dateAfter, dateBefore;
	
	public String getSelectionLine()
	{
		boolean hasFilter = false;
		StringBuilder stringBuilder = new StringBuilder();
		
		if(useTitleFilter&&!titleFilter.isEmpty())
		{
			stringBuilder.append("title LIKE ");
			stringBuilder.append("'%");
			stringBuilder.append(titleFilter);
			stringBuilder.append("%'");
			hasFilter = true;
		}
		
		if(useAuthorFilter&&!authorFilter.isEmpty())
		{
			if(hasFilter)
				stringBuilder.append(" AND ");
			else
				hasFilter = true;
			stringBuilder.append("author LIKE ");
			stringBuilder.append("'%");
			stringBuilder.append(authorFilter);
			stringBuilder.append("%'");
			hasFilter = true;
		}
		
		if(useDateFilter)
		{
			if(hasFilter)
				stringBuilder.append(" AND ");
			else
				hasFilter = true;			
			stringBuilder.append("printYear");
			stringBuilder.append(dateComparatorType.getExp());
			if(dateComparatorType==ComparatorType.BETWEEN)
			{
				if(dateAfter>dateBefore)
				{
					stringBuilder.append(dateBefore);
					stringBuilder.append(" AND ");
					stringBuilder.append(dateAfter);
				}
				else
				{
					stringBuilder.append(dateAfter);
					stringBuilder.append(" AND ");
					stringBuilder.append(dateBefore);
				}
			}
			else
			{
				stringBuilder.append(dateAfter);
			}
			hasFilter = true;
		}
		
		switch(readFilter)
		{
			case READ:
			{
				if(hasFilter)
					stringBuilder.append(" AND ");
				else
					hasFilter = true;
				stringBuilder.append("readAlready=1");
				break;
			}
			case NOT_READ:
			{
				if(hasFilter)
					stringBuilder.append(" AND ");
				else
					hasFilter = true;
				stringBuilder.append("readAlready=0");
				break;
			}
		}
		if(hasFilter)
			stringBuilder.insert(0, " WHERE ");
		return stringBuilder.toString();
	}
	
	public Filter()
	{
		readFilter = ReadFilterStatus.DOESNT_MATTER;
		dateComparatorType = ComparatorType.EQUAL;
	}

	public ReadFilterStatus getReadFilter() {
		return readFilter;
	}

	public void setReadFilter(ReadFilterStatus readFilter) {
		this.readFilter = readFilter;
	}

	public boolean getUseDateFilter() {
		return useDateFilter;
	}

	public void setUseDateFilter(boolean useDateFilter) {
		this.useDateFilter = useDateFilter;
	}

	public ComparatorType getDateComparatorType() {
		return dateComparatorType;
	}

	public void setDateComparatorType(ComparatorType dateComparatorType) {
		this.dateComparatorType = dateComparatorType;
	}

	public int getDateAfter() {
		return dateAfter;
	}

	public void setDateAfter(int dateAfter) {
		this.dateAfter = dateAfter;
	}

	public int getDateBefore() {
		return dateBefore;
	}

	public void setDateBefore(int dateBefore) {
		this.dateBefore = dateBefore;
	}

	public boolean getUseTitleFilter() {
		return useTitleFilter;
	}

	public void setUseTitleFilter(boolean useTitleFilter) {
		this.useTitleFilter = useTitleFilter;
	}

	public String getTitleFilter() {
		return titleFilter;
	}

	public void setTitleFilter(String titleFilter) {
		this.titleFilter = titleFilter;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Filter");
		boolean hasFilter = false;
		if(useTitleFilter&&!titleFilter.isEmpty())
		{
			stringBuilder.append(" [Title contains '");
			stringBuilder.append(titleFilter);
			stringBuilder.append("'");
			hasFilter = true;
		}
		
		if(useAuthorFilter&&!authorFilter.isEmpty())
		{
			if(hasFilter)
				stringBuilder.append("; ");
			else
			{
				stringBuilder.append(" [");
				hasFilter = true;
			}	
			stringBuilder.append("Author contains '");
			stringBuilder.append(authorFilter);
			stringBuilder.append("'");
		}
		
		if(useDateFilter)
		{
			if(hasFilter)
				stringBuilder.append("; ");
			else
			{
				stringBuilder.append(" [");
				hasFilter = true;
			}			
			stringBuilder.append("Date ");
			stringBuilder.append(dateComparatorType.toString());
			stringBuilder.append(dateAfter);
			if(dateComparatorType==ComparatorType.BETWEEN)
			{
				stringBuilder.append(" AND ");
				stringBuilder.append(dateBefore);
			}
		}
		switch(readFilter)
		{
			case READ:
			{
				if(hasFilter)
					stringBuilder.append("; ");
				else
				{
					stringBuilder.append(" [");
					hasFilter = true;
				}	
				stringBuilder.append("not read yet");
				break;
			}
			case NOT_READ:
			{
				if(hasFilter)
					stringBuilder.append("; ");
				else
				{
					stringBuilder.append(" [");
					hasFilter = true;
				}	
				stringBuilder.append("read already");
				break;
			}
		}
		if(hasFilter)
			stringBuilder.append("]");
		return stringBuilder.toString();
	}

	public boolean getUseAuthorFilter() {
		return useAuthorFilter;
	}

	public void setUseAuthorFilter(boolean useAuthorFilter) {
		this.useAuthorFilter = useAuthorFilter;
	}

	public String getAuthorFilter() {
		return authorFilter;
	}

	public void setAuthorFilter(String authorFilter) {
		this.authorFilter = authorFilter;
	}
}