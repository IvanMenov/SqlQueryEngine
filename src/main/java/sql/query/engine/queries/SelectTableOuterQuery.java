package sql.query.engine.queries;

import java.text.MessageFormat;

public class SelectTableOuterQuery implements IQuery{
	
	private boolean containsSelectGroups;
	
	public SelectTableOuterQuery(boolean containsSelectGroups) {
		this.containsSelectGroups = containsSelectGroups;
	}
	@Override
	public MessageFormat getMessageFormat() {
		String pattern = containsSelectGroups ? "{0}\r\n"
				+ "from\n"
				+ "(\r\n"
				+ "{1}\r\n"
				+ ") "
						: "{0}\r\n"
						+ " from\r\n"
						+ "{1} ";
		
		return new MessageFormat(pattern);
	}

}
