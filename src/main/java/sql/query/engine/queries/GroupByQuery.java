package sql.query.engine.queries;

import java.text.MessageFormat;

public class GroupByQuery implements IQuery{

	@Override
	public MessageFormat getMessageFormat() {
		return new MessageFormat("\r\ngroup by {0};");
	}

}
