package sql.query.engine.queries;

import java.text.MessageFormat;

public interface IQuery {
		
	public MessageFormat getMessageFormat();
	
	public default String build(String... parts) {
		return getMessageFormat().format(parts);
	}
}
