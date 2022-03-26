package sql.query.engine.builders;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import sql.query.engine.model.Table;
import sql.query.engine.queries.CreateTableQuery;

public class TableQueryBuilder extends ASQLBuilder{
	private Table[] table;

	
	public TableQueryBuilder(Table[] table) {
		this.table = table;
		
	}
	
	@Override
	public Map<String, String> buildMap(){
		Map<String, String> map = new HashMap<>();
		for (Table table : table) {
			if(!table.isExist() && table.getCreateQuery() != null) {
				String query = callCreateTableQuery(table);
				map.put(table.getId(), query);
			}
		}
		
		return map;
	}
	
	@Override
	public String buildQuery() {
		for (int i=0;i< table.length;i++) {
			if(!table[i].isExist() && table[i].getCreateQuery() != null) {
				getBuilder().append(new MessageFormat(WRAP_IN_COMMENT).format(new String[] {table[i].getId()}));
				getBuilder().append(NEWLINE);
				String query = callCreateTableQuery(table[i]);
				getBuilder().append(query);
				
				if(i< table.length -1) {
					getBuilder().append(NEWLINE);
				}
			}
		}
		return getBuilder().toString();
	}

	private String callCreateTableQuery(Table table) {
		String query = new CreateTableQuery().build(new String[]{table.getId(), table.getCreateQuery()});
		return query;
	}
}
