package sql.query.engine.queries;

import java.text.MessageFormat;

public class SubQueryFilterList implements IQuery {
	
	@Override
	public MessageFormat getMessageFormat() {
		return new MessageFormat("S{0} as \n"
				+ "(\r\n select {1} {2} as actual_sales\r\n"
				+ " from {3}\r\n"
				+ " where {4}\n"
				+ "), \n");
	}


	
	public class WhereClause implements IQuery{
				
		@Override
		public MessageFormat getMessageFormat() {
			return  new MessageFormat(" ''{0}'' in ({1}) ");
		}
	}

	
}
