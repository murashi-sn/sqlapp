SELECT 
CC.CONSTRAINT_CATALOG
, CC.CONSTRAINT_SCHEMA
, CC.CONSTRAINT_NAME
, CC.CHECK_CLAUSE
, CTU.TABLE_NAME
, CCU.COLUMN_NAME
FROM information_schema.CHECK_CONSTRAINTS CC
INNER JOIN information_schema.CONSTRAINT_TABLE_USAGE CTU
  ON (CC.CONSTRAINT_CATALOG=CTU.CONSTRAINT_CATALOG
  AND CC.CONSTRAINT_SCHEMA=CTU.CONSTRAINT_SCHEMA
  AND CC.CONSTRAINT_NAME=CTU.CONSTRAINT_NAME
  )
LEFT OUTER JOIN information_schema.CONSTRAINT_COLUMN_USAGE CCU
  ON (CC.CONSTRAINT_CATALOG=CCU.CONSTRAINT_CATALOG
  AND CC.CONSTRAINT_SCHEMA=CCU.CONSTRAINT_SCHEMA
  AND CC.CONSTRAINT_NAME=CCU.CONSTRAINT_NAME
  )
WHERE 1=1
  /*if isNotEmpty(catalogName)*/
  AND CC.CONSTRAINT_CATALOG IN /*catalogName*/('%')
  /*end*/
  /*if isNotEmpty(schemaName)*/
  AND CC.CONSTRAINT_SCHEMA IN /*schemaName*/('%')
  /*end*/
  /*if isNotEmpty(tableName)*/
  AND CTU.TABLE_NAME IN /*tableName*/('%')
  /*end*/
  /*if isNotEmpty(checkClause)*/
  AND CC.CHECK_CLAUSE IN /*checkClause*/('%')
  /*end*/
ORDER BY CC.CONSTRAINT_CATALOG, CC.CONSTRAINT_SCHEMA, CC.CONSTRAINT_NAME
