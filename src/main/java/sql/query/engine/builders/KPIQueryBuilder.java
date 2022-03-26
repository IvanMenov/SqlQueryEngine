package sql.query.engine.builders;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sql.query.engine.model.FilterCols;
import sql.query.engine.model.FilterList;
import sql.query.engine.model.Input;
import sql.query.engine.model.Table;
import sql.query.engine.model.ValList;
import sql.query.engine.queries.GroupByQuery;
import sql.query.engine.queries.InnerSelectWithUnionQuery;
import sql.query.engine.queries.SQLOperators;
import sql.query.engine.queries.SelectTableQuery;
import sql.query.engine.queries.SubQueryFilterList;
import sql.query.engine.queries.SubQueryFilterList.WhereClause;
import sql.query.engine.utils.Entities;

public class KPIQueryBuilder extends ASQLBuilder{
	
	private Input[] inputs;
	
	private Map<String, Table> tableNameToTableObj = new HashMap<>();
	
	private Map<String, String>kpiMap = new HashMap<>();
	
	public KPIQueryBuilder(Input[] inputs, Table[] tables) {
		this.inputs = inputs;
		mapTableIdToTableObj(tables);
	}


	private void mapTableIdToTableObj(Table[] tables) {
		for (int i = 0; i < tables.length; i++) {
			tableNameToTableObj.putIfAbsent(tables[i].getId(), tables[i]);
		}

	}


	@Override
	public Map<String, String> buildMap() {
		if(kpiMap.isEmpty()) {
			for (Input input : inputs) {
				Table table = tableNameToTableObj.get(input.getTableId());
				FilterList[] filterList = input.getFilterList();
				
				if(filterList.length != 0) {
					getBuilder().append(SQLOperators.WITH.getOperatorValue());
				}
				List<String>selectGroups = new ArrayList<>();
				for (int filterListIndex=0; filterListIndex < filterList.length; filterListIndex++ ) {
					selectGroups.add(filterList[filterListIndex].getSelectGroup());
					SubQueryFilterList subQuery = new SubQueryFilterList();
					StringBuilder whereBuilder = new StringBuilder();	
					FilterCols[] filterCols = filterList[filterListIndex].getFilterCols();
					
					for (int filterColsIndex=0; filterColsIndex < filterCols.length; filterColsIndex++) {
						WhereClause whereClause = subQuery.new WhereClause();
						
						ValList[] valList = filterCols[filterColsIndex].getValList();
						for(int valListIndex = 0; valListIndex < valList.length; valListIndex++) {
							if(filterCols[filterColsIndex].getCol().endsWith(ASTERISK)) {
								String columnName = filterCols[filterColsIndex].getCol();
								String key = columnName.substring(0, columnName.lastIndexOf(ASTERISK));
								whereBuilder.append(whereClause.build(new String[]{valList[valListIndex].getValue(),  Entities.getEntity(key)}));
							}else {
								whereBuilder.append(whereClause.build(new String[]{valList[valListIndex].getValue(),  filterCols[filterColsIndex].getCol()}));
							}
							
							if( valListIndex < valList.length -1) {
								whereBuilder.append(SQLOperators.OR.getOperatorValue());
							}
						}
						
						
						if( filterColsIndex < filterCols.length-1) {
							whereBuilder.append(SQLOperators.AND.getOperatorValue());
						}
											
					}
								
					getBuilder().append(subQuery.build(new String[] { filterList[filterListIndex].getSelectGroup(), table.getColumnsAsString(table.getColumns().length),
							filterList[filterListIndex].isMinus(), table.getId(), whereBuilder.toString() }));
						
				}
				getBuilder().deleteCharAt(getBuilder().lastIndexOf(COMMA));
				
				SelectTableQuery selectTableQuery = new SelectTableQuery();
				StringBuilder innerSelectBuilder = new StringBuilder(); 
				for (int selectGroupIndex = 0; selectGroupIndex < selectGroups.size(); selectGroupIndex++) {
					
					InnerSelectWithUnionQuery innerSelectWithUnion = new InnerSelectWithUnionQuery();
					innerSelectBuilder.append(innerSelectWithUnion.build(new String[] {selectGroups.get(selectGroupIndex)}));
					
					if(selectGroupIndex < selectGroups.size() -1) {
						innerSelectBuilder.append(InnerSelectWithUnionQuery.UNION_ALL);
					}

				}
				GroupByQuery groupBy = new GroupByQuery();
				
				getBuilder().append(
						selectTableQuery.build(new String[] { table.getColumnsAsString(table.getColumns().length - 1),
								innerSelectBuilder.toString(), groupBy.build(input.getGroupColums()) }));			
				kpiMap.put(input.getKpiName(), getBuilder().toString());
				
				getBuilder().delete(0, getBuilder().length());
			}
		}
		return kpiMap;
	}


	@Override
	public String buildQuery() {
		if(kpiMap.isEmpty()) {
			buildMap();
		}
		kpiMap.forEach((kpiID, query) -> {
			getBuilder().append(new MessageFormat(WRAP_IN_COMMENT).format(new String[] {kpiID}));
			getBuilder().append(NEWLINE);
			getBuilder().append(query);
			getBuilder().append(NEWLINE);
		});
		return getBuilder().toString();
	}
	

	
}
