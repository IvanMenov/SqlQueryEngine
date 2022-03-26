package sql.query.engine.queries;

import java.text.MessageFormat;

public class InnerSelectWithUnionQuery implements IQuery{
	
	public static final String UNION_ALL =" union all ";
	@Override
	public MessageFormat getMessageFormat() {
		return new MessageFormat("select * from S{0}");
	}

}
