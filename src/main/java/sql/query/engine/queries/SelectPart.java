package sql.query.engine.queries;

import java.text.MessageFormat;

public class SelectPart implements IQuery {
	
	@Override
	public MessageFormat getMessageFormat() {
		return new MessageFormat("select {0}, {1}");
		 
	}

}
