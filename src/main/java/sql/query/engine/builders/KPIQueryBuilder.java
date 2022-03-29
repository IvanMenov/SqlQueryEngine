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
import sql.query.engine.queries.SelectPart;
import sql.query.engine.queries.SelectTableOuterQuery;
import sql.query.engine.queries.SelectTablesSubQuery;
import sql.query.engine.queries.SelectTablesSubQuery.WhereClause;
import sql.query.engine.utils.Entities;

public class KPIQueryBuilder extends ASQLBuilder{
	
	private Input[] inputs;
	
	private Map<String, Table> tableNameToTableObj = new HashMap<>();
	
	private Map<String, String>kpiMap = new HashMap<>();
	
	public KPIQueryBuilder(Input[] inputs, Table[] tables) {
		this.inputs = inputs;
		mapTableIdToTableObj(tables);
	}
	
	@Override
	public String buildQuery() {
		if(getBuilder().length() > 0) {
			getBuilder().delete(0, getBuilder().length());
		}
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
				
				constructSelectGroupsFromFilterList(table, filterList, selectGroups);
				
				if(getBuilder().lastIndexOf(COMMA) != -1) {
					getBuilder().deleteCharAt(getBuilder().lastIndexOf(COMMA));
				}
				
				StringBuilder innerSelectBuilder = new StringBuilder(); 
				
				constructSelectWithUnion(selectGroups, innerSelectBuilder);
				
				constructOuterSelect(input, table, selectGroups, innerSelectBuilder);	
				
				if(getBuilder().charAt(getBuilder().length() -1) != SEMICOLON) {
					getBuilder().append(SEMICOLON);
				}
				kpiMap.put(input.getKpiName(), getBuilder().toString());
				
				getBuilder().delete(0, getBuilder().length());
			}
		}
		return kpiMap;
	}

	private void constructOuterSelect(Input input, Table table, List<String> selectGroups,
			StringBuilder innerSelectBuilder) {
		SelectTableOuterQuery selectTableQuery = new SelectTableOuterQuery(!selectGroups.isEmpty());
		String[] colAggList = table.getColumnsAggList();
		String colGroupList = table.getColumnsListAsString(table.getColumnsGroupList());
		String[] aggFuntions = table.getColumnsAggFunction();
		
		
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < aggFuntions.length; i++) {
			for(int j =0; j< colAggList.length; j++) {
				builder.append(aggFuntions[i]);
				builder.append(OPEN_BRACKET);
				builder.append(colAggList[j]);
				builder.append(CLOSE_BRACKET);
				builder.append(AS);
				builder.append(colAggList[j]);
				builder.append(COMMA);
				builder.append(WHITESPACE);
			}
		}
		builder.deleteCharAt(builder.lastIndexOf(COMMA));
		
		SelectPart select = new SelectPart();
		
		
		GroupByQuery groupBy = new GroupByQuery();
		
		getBuilder().append(
				selectTableQuery.build(new String[] { select.build(colGroupList, builder.toString()),
						selectGroups.isEmpty() ? table.getId() : innerSelectBuilder.toString(), groupBy.build(input.getGroupColums()) }));
	}

	private void mapTableIdToTableObj(Table[] tables) {
		for (int i = 0; i < tables.length; i++) {
			tableNameToTableObj.putIfAbsent(tables[i].getId(), tables[i]);
		}

	}
	
	private void constructSelectGroupsFromFilterList(Table table, FilterList[] filterList, List<String> selectGroups) {
		for (int filterListIndex=0; filterListIndex < filterList.length; filterListIndex++ ) {
			selectGroups.add(filterList[filterListIndex].getSelectGroup());
			SelectTablesSubQuery subQuery = new SelectTablesSubQuery();
			StringBuilder whereBuilder = new StringBuilder();	
			FilterCols[] filterCols = filterList[filterListIndex].getFilterCols();
			
			constructWhereClauseFromFilterCols(subQuery, whereBuilder, filterCols);
			String[] colAggList = table.getColumnsAggList();
			String colGroupList = table.getColumnsListAsString(table.getColumnsGroupList());
			
			SelectPart select = new SelectPart();
			StringBuilder builder = new StringBuilder();
			String selectPart;
			if(filterList[filterListIndex].isMinusAsBoolean()) {
				for (int i = 0; i < colAggList.length; i++) {
					builder.append(colAggList[i]);
					builder.append(filterList[filterListIndex].isMinus());
					builder.append(AS);
					builder.append(colAggList[i]);
					builder.append(COMMA);
					builder.append(WHITESPACE);
				}
				builder.deleteCharAt(builder.lastIndexOf(COMMA));
				
				selectPart = select.build(new String[] {colGroupList, builder.toString()});
			}else {
				selectPart = select.build(new String[] {colGroupList, table.getColumnsListAsString(table.getColumnsAggList())});
			}
			getBuilder().append(subQuery.build(new String[] { filterList[filterListIndex].getSelectGroup(), selectPart, table.getId(), whereBuilder.toString() }));
				
		}
	}


	private void constructWhereClauseFromFilterCols(SelectTablesSubQuery subQuery, StringBuilder whereBuilder,
			FilterCols[] filterCols) {
		for (int filterColsIndex=0; filterColsIndex < filterCols.length; filterColsIndex++) {
			WhereClause whereClause = subQuery.new WhereClause();
			
			ValList[] valList = filterCols[filterColsIndex].getValList();
			constructWhereClauseFromValList(whereBuilder, filterCols, filterColsIndex, whereClause, valList);
			
			
			if( filterColsIndex < filterCols.length-1) {
				whereBuilder.append(SQLOperators.AND.getOperatorValue());
			}
								
		}
	}


	private void constructWhereClauseFromValList(StringBuilder whereBuilder, FilterCols[] filterCols, int filterColsIndex,
			WhereClause whereClause, ValList[] valList) {
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
	}


	private void constructSelectWithUnion(List<String> selectGroups, StringBuilder innerSelectBuilder) {
		for (int selectGroupIndex = 0; selectGroupIndex < selectGroups.size(); selectGroupIndex++) {
			
			InnerSelectWithUnionQuery innerSelectWithUnion = new InnerSelectWithUnionQuery();
			innerSelectBuilder.append(innerSelectWithUnion.build(new String[] {selectGroups.get(selectGroupIndex)}));
			
			if(selectGroupIndex < selectGroups.size() -1) {
				innerSelectBuilder.append(InnerSelectWithUnionQuery.UNION_ALL);
			}

		}
	}
	
}
