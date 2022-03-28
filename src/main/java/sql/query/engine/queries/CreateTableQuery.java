package sql.query.engine.queries;

import java.text.MessageFormat;

public class CreateTableQuery implements IQuery{
	
	@Override
	public MessageFormat getMessageFormat() {
		return new MessageFormat("create temp table {0} as {1};");
	}

}
