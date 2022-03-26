package sql.query.engine.queries;

import java.text.MessageFormat;

public class SelectTableQuery implements IQuery{

	@Override
	public MessageFormat getMessageFormat() {
		
		return new MessageFormat("select {0}, sum(actual_sales)\r\n"
				+ " from\n"
				+ " (\r\n"
				+ "{1}\r\n"
				+ ")\r\n"
				+ " {2}");
	}

}
