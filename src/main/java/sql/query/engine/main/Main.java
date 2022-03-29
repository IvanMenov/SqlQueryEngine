package sql.query.engine.main;

import sql.query.engine.builders.KPIQueryBuilder;
import sql.query.engine.builders.TableQueryBuilder;
import sql.query.engine.model.Input;
import sql.query.engine.model.Table;
import sql.query.engine.utils.Parser;

public class Main {
	
	public static void main(String[] args) {		
		String tableConf = "[\r\n"
				+ "	{\r\n"
				+ "		\"Table\": \"Table1\",\r\n"
				+ "		\"Dataset\": \"datahub\",\r\n"
				+ "		\"columns_group_list\": \"a,b\",\r\n"
				+ "		\"columns_agg_list\":\"c,d\",\r\n"
				+ "		\"columns_agg_func\":\"sum\",\r\n"
				+ "		\"isExist\": false,\r\n"
				+ "		\"createQuery\": \"select * from datahub.xyz\"\r\n"
				+ "	},\r\n"
				+ "	{\r\n"
				+ "		\"Table\": \"Table2\",\r\n"
				+ "		\"Dataset\": \"datahub\",\r\n"
				+ "		\"columns_group_list\": \"a,b,c\",\r\n"
				+ "		\"columns_agg_list\":\"d,e\",\r\n"
				+ "		\"columns_agg_func\":\"sum\",\r\n"
				+ "		\"isExist\": true,\r\n"
				+ "		\"createQuery\": \"\"\r\n"
				+ "	},\r\n"
				+ "	{\r\n"
				+ "		\"Table\": \"Table3\",\r\n"
				+ "		\"Dataset\": \"datahub\",\r\n"
				+ "		\"columns_group_list\": \"a,b,c\",\r\n"
				+ "		\"columns_agg_list\":\"d\",\r\n"
				+ "		\"columns_agg_func\":\"sum\",\r\n"
				+ "		\"isExist\": false,\r\n"
				+ "		\"createQuery\": \"select a,b,c,sum(total_cost) from datahub.a1 join from datahub.b1 on a1.id=b1.id group by a,b,c\"\r\n"
				+ "	}\r\n"
				+ "]";
//		
//		String kpi = "[{\r\n"
//				+ "\"kpi_name\": \"Total Revenue-with-no-filter\",\r\n"
//				+ "\"id\": 255,\r\n"
//				+ "\"table\": \"Table1\",\r\n"
//				+ "\"filter_list\": [],\r\n"
//				+ "\"group_cols\": \"1,2,3,4\"\r\n"
//				+ "}]";
		
		String kpi2 = "[{\r\n"
				+ "		\"kpi_name\": \"Total assets\",\r\n"
				+ "		\"id\": 123,\r\n"
				+ "		\"table\": \"Table1\",\r\n"
				+ "		\"filter_list\": [\r\n"
				+ "			{\r\n"
				+ "				\"select_group\": \"1\",\r\n"
				+ "				\"is_minus\": \"false\",\r\n"
				+ "				\"filter_cols\": [\r\n"
				+ "					{\r\n"
				+ "						\"col\": \"business_code\",\r\n"
				+ "						\"val_list\": [\r\n"
				+ "							{\r\n"
				+ "								\"value\": \"BI_100\",\r\n"
				+ "								\"scope\": \"I\"\r\n"
				+ "							}\r\n"
				+ "						]\r\n"
				+ "					}\r\n"
				+ "				]\r\n"
				+ "			},\r\n"
				+ "			{\r\n"
				+ "				\"select_group\": \"2\",\r\n"
				+ "				\"is_minus\": \"true\",\r\n"
				+ "				\"filter_cols\": [\r\n"
				+ "					{\r\n"
				+ "						\"col\": \"business_code\",\r\n"
				+ "						\"val_list\": [\r\n"
				+ "							{\r\n"
				+ "								\"value\": \"BI_200\",\r\n"
				+ "								\"scope\": \"I\"\r\n"
				+ "							}\r\n"
				+ "						]\r\n"
				+ "					}\r\n"
				+ "				]\r\n"
				+ "			}\r\n"
				+ "		],\r\n"
				+ "		\"group_cols\": \"1,2,3,4\"\r\n"
				+ "	}]";
		Table[] tables = Parser.parseToObject(tableConf, Table[].class);
		Input[] input = Parser.parseToObject(kpi2, Input[].class);
		KPIQueryBuilder builder = new KPIQueryBuilder(input, tables);
		System.out.println(builder.buildQuery());
		
		
	}
}
