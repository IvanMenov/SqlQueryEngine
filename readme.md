The steps to create table query files:

1. deserialize .json file (in the project it is TableConfig.json) into Table[] objects using Parser.parseToObject (see sql.query.engine.tests.TestSet.loadResources)

2. create TableQueryBuilder instance passing in its constructor the tables array from the previous step 
(TableQueryBuilder tableQuerybuilder = new TableQueryBuilder(tables);

3. You can call the TableQueryBuilder instance object - buildMap to produce map of table name to create query, or buildQuery to produce the content of all the create tables as a string.

4. call FileUtils.createQueryFile with the content from step 3 and the path where you would like to save it

KPI query steps:
1. deserialize .json file (in the project it is Input.json) into Table[] and Input[] objects using Parser.parseToObject (see sql.query.engine.tests.TestSet.loadResources)

2. create KPIQueryBuilder instance passing in its constructor the innput and tables array from the previous step 
KPIQueryBuilder kpiQueryBuilder = new KPIQueryBuilder(inputs, tables);

Step 3, 4 are the same as above.