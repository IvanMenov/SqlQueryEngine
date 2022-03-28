package sql.query.engine.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import sql.query.engine.builders.ASQLBuilder;
import sql.query.engine.builders.KPIQueryBuilder;
import sql.query.engine.builders.TableQueryBuilder;
import sql.query.engine.model.Input;
import sql.query.engine.model.Table;
import sql.query.engine.utils.FileUtils;
import sql.query.engine.utils.Parser;

@TestInstance(Lifecycle.PER_CLASS)
public class TestSet {
	
	private static ClassLoader classLoader = TestSet.class.getClassLoader();
	
	private static final String INPUT_JSON = "./Input.json";
	private static final String TABLE_CONFIG_JSON = "./TableConfig.json";
	private static final String EXPECTED_OUTPUT = "./expected_kpi_output.txt";
	
	private static final String KPI_TOTAL_ASSETS ="Total assets";
	private static final String KPI_TOTAL_ENTITY ="Total entity";
	private static final String KPI_TOTAL_EXPENSE="Total expense";
	private static final String KPI_TOTAL_REVENUE ="Total Revenue";
	
	private static final String EXPECTED_QUERY_TABLE1 = "create temp table Table1 as select * from datahub.xyz;";
	private static final String EXPECTED_QUERY_TABLE3 = "create temp table Table3 as select a,b,c,sum(total_cost) from datahub.a1 join from datahub.b1 on a1.id=b1.id group by a,b,c;";

	private Table[] tables = null;
	private Input[] inputs = null;
	
	private Map<String, String> expectedKPIs = new HashMap<>();
	
	private String expectedKpisQuery;
	
	private String expectedTablesQuery;
	
	@Test
	public void testQueryFileCreation() throws IOException, URISyntaxException {
		createQueryFileAndDelete(expectedTablesQuery, "./tableQuery.txt");
		createQueryFileAndDelete(expectedKpisQuery, "./KPIQuery.txt");		
	}
	
	private void createQueryFileAndDelete(String content, String path) throws IOException, URISyntaxException{
		Path pathToQueryFile =  FileUtils.createQueryFile(content, path);
		assertTrue(Files.exists(pathToQueryFile, LinkOption.NOFOLLOW_LINKS));
		
		boolean isDeleted = FileUtils.deleteQueryFile(pathToQueryFile);
		if(isDeleted) {
			assertTrue(Files.notExists(pathToQueryFile, LinkOption.NOFOLLOW_LINKS));
		}
	}
	@Test
	public void testKPIQueryBuilder() throws FileNotFoundException, IOException {
		KPIQueryBuilder kpiQueryBuilder = new KPIQueryBuilder(inputs, tables);
		Map<String, String>actualKpiMap = kpiQueryBuilder.buildMap();
		String actualKpisQuery = kpiQueryBuilder.buildQuery();
		
		assertEquals(actualKpiMap.size(), expectedKPIs.size());
		
		actualKpiMap.forEach((key, value) ->{
			assertTrue(expectedKPIs.containsKey(key));
			//replace all newlines, tabs, with single whitespace and trim all leading and trailing spaces to compare results
			value = value.replaceAll("\\s+", " ").trim();
			String actualValue = expectedKPIs.get(key).replaceAll("\\s+", " ").trim();
			System.out.println();
			assertEquals(actualValue, value);
		});
//		expectedKpisQuery = expectedKpisQuery.replaceAll("\\s+", " ");
//		actualKpisQuery = actualKpisQuery.replaceAll("\\s+", " ");
		
		assertEquals(expectedKpisQuery.replaceAll("\\s+", " "), actualKpisQuery.replaceAll("\\s+", " "));
	}

	@Test
	public void testTableQueryBuilder() throws FileNotFoundException{
			
			TableQueryBuilder tableQuerybuilder = new TableQueryBuilder(tables);
			Map<String, String> map = tableQuerybuilder.buildMap();
			
			for (Table table : tables) {
				if(!table.getId().equals("Table2")) {
					assertTrue(map.containsKey(table.getId()));
				}
			}
			
			map.forEach((tableId, tableQuery) ->{
				if (tableId.equals("Table1")) {
					assertEquals(EXPECTED_QUERY_TABLE1, tableQuery);
				} else {
					assertEquals(EXPECTED_QUERY_TABLE3, tableQuery);
				}
			});
			
			
			String actualTableQuery = tableQuerybuilder.buildQuery();
			
			assertEquals(expectedTablesQuery, actualTableQuery);
			
	
		
	}

	@BeforeAll
	void loadResources() throws FileNotFoundException, IOException {
		tables = Parser.parseToObject(new File(classLoader.getResource(TABLE_CONFIG_JSON).getFile()), Table[].class);
		inputs = Parser.parseToObject(new File(classLoader.getResource(INPUT_JSON).getFile()), Input[].class);
		
		prepareExpectedTableQuery();
		
		loadExpectedKPIs();

	}

	private void prepareExpectedTableQuery() {
		StringBuilder expectedQuery = new StringBuilder("/*Table1*/\n");
		expectedQuery.append(EXPECTED_QUERY_TABLE1);
		expectedQuery.append("\n");
		expectedQuery.append("/*Table3*/\n");
		expectedQuery.append(EXPECTED_QUERY_TABLE3);
		
		expectedTablesQuery = expectedQuery.toString();
	}

	private void loadExpectedKPIs() throws FileNotFoundException, IOException {
		StringBuilder builderTotalAssets = new StringBuilder();
		StringBuilder builderTotalEntity = new StringBuilder();
		StringBuilder builderTotalExpense = new StringBuilder();
		StringBuilder builderTotalRevenue = new StringBuilder();

		populateCorrectStringBuffers(builderTotalAssets, builderTotalEntity, builderTotalExpense, builderTotalRevenue);
		
		
		putTogetherInASingleQuery(builderTotalAssets, builderTotalEntity, builderTotalExpense, builderTotalRevenue);
		
		populateKPIMap(builderTotalAssets, builderTotalEntity, builderTotalExpense, builderTotalRevenue);
	}

	private void populateKPIMap(StringBuilder builderTotalAssets, StringBuilder builderTotalEntity,
			StringBuilder builderTotalExpense, StringBuilder builderTotalRevenue) {
		expectedKPIs.put(KPI_TOTAL_ASSETS, builderTotalAssets.toString());
		expectedKPIs.put(KPI_TOTAL_ENTITY, builderTotalEntity.toString());
		expectedKPIs.put(KPI_TOTAL_EXPENSE, builderTotalExpense.toString());
		expectedKPIs.put(KPI_TOTAL_REVENUE, builderTotalRevenue.toString());
	}

	private void putTogetherInASingleQuery(StringBuilder builderTotalAssets, StringBuilder builderTotalEntity,
			StringBuilder builderTotalExpense, StringBuilder builderTotalRevenue) {
		StringBuilder kpiQueryBuilder= new StringBuilder();
		
		kpiQueryBuilder.append(new MessageFormat(ASQLBuilder.WRAP_IN_COMMENT).format(new String[] {KPI_TOTAL_ASSETS}));
		kpiQueryBuilder.append(ASQLBuilder.NEWLINE);
		kpiQueryBuilder.append(builderTotalAssets.toString());
		kpiQueryBuilder.append(ASQLBuilder.NEWLINE);
		
		kpiQueryBuilder.append(new MessageFormat(ASQLBuilder.WRAP_IN_COMMENT).format(new String[] {KPI_TOTAL_ENTITY}));
		kpiQueryBuilder.append(ASQLBuilder.NEWLINE);
		kpiQueryBuilder.append(builderTotalEntity.toString());
		kpiQueryBuilder.append(ASQLBuilder.NEWLINE);
		
		kpiQueryBuilder.append(new MessageFormat(ASQLBuilder.WRAP_IN_COMMENT).format(new String[] {KPI_TOTAL_REVENUE}));
		kpiQueryBuilder.append(ASQLBuilder.NEWLINE);
		kpiQueryBuilder.append(builderTotalRevenue.toString());
		kpiQueryBuilder.append(ASQLBuilder.NEWLINE);
		
		kpiQueryBuilder.append(new MessageFormat(ASQLBuilder.WRAP_IN_COMMENT).format(new String[] {KPI_TOTAL_EXPENSE}));
		kpiQueryBuilder.append(ASQLBuilder.NEWLINE);
		kpiQueryBuilder.append(builderTotalExpense.toString());
		kpiQueryBuilder.append(ASQLBuilder.NEWLINE);
		
		expectedKpisQuery = kpiQueryBuilder.toString();
	}

	private void populateCorrectStringBuffers(StringBuilder builderTotalAssets, StringBuilder builderTotalEntity,
			StringBuilder builderTotalExpense, StringBuilder builderTotalRevenue)
			throws IOException, FileNotFoundException {
		try(BufferedReader reader = new BufferedReader(new FileReader(new File(classLoader.getResource(EXPECTED_OUTPUT).getFile())));){
			String line;
			boolean appendToTotalAssets = false;
			boolean appendToTotalAssets2 = false;
			boolean appendToTotalExpense = false;
			
			while ((line = reader.readLine()) != null) {
				if(line.contains(KPI_TOTAL_ASSETS)) {
					appendToTotalAssets = true;
					appendToTotalAssets2 = false;
					appendToTotalExpense = false;
				}
				else if(line.contains(KPI_TOTAL_ENTITY)) {
					appendToTotalAssets = false;
					appendToTotalAssets2 = true;
					appendToTotalExpense = false;
					
				}
				else if(line.contains(KPI_TOTAL_EXPENSE)) {
					appendToTotalAssets = false;
					appendToTotalAssets2 = false;
					appendToTotalExpense = true;
					
				}
				else if(line.contains(KPI_TOTAL_REVENUE)) {
					appendToTotalAssets = false;
					appendToTotalAssets2 = false;
					appendToTotalExpense = false;
					
				}else {
					if(appendToTotalAssets) {
						builderTotalAssets.append(line);
						builderTotalAssets.append("\n");
					}
					else if(appendToTotalAssets2) {
						builderTotalEntity.append(line);
						builderTotalEntity.append("\n");
					}
					else if (appendToTotalExpense) {
						builderTotalExpense.append(line);
						builderTotalExpense.append("\n");
					}
					else {
						builderTotalRevenue.append(line);
						builderTotalRevenue.append("\n");
					}
				}	
			}	
		}
	}
}
