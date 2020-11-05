SELECT V.VIEWDEFINITION
, T.TABLENAME AS table_name
, S.SCHEMANAME AS schema_name
FROM SYS.SYSVIEWS V
INNER JOIN SYS.SYSTABLES T
  ON (V.TABLEID=T.TABLEID)
INNER JOIN SYS.SYSSCHEMAS S
  ON (T.SCHEMAID=S.SCHEMAID)
WHERE 1=1 
  /*if isNotEmpty(schemaName)*/
  AND S.SCHEMANAME IN /*schemaName*/('%')
  /*end*/
  /*if isNotEmpty(tableName)*/
  AND T.TABLENAME IN /*tableName*/('%')
  /*end*/
ORDER BY S.SCHEMANAME, T.TABLENAME
