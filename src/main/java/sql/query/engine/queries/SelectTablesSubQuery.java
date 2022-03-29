package sql.query.engine.queries;

import java.text.MessageFormat;

public class SelectTablesSubQuery implements IQuery {
	
	@Override
	public MessageFormat getMessageFormat() {
		return new MessageFormat("S{0} as \n"
				+ "(\r\n {1}\r\n"
				+ " from {2}\r\n"
				+ " where {3}\n"
				+ "), \n");
	}


	
	public class WhereClause implements IQuery{
				
		@Override
		public MessageFormat getMessageFormat() {
			return  new MessageFormat(" ''{0}'' in ({1}) ");
		}
	}

	
}
