SELECT s.*
, c.COMMENT
FROM V_CATALOG.SCHEMATA s
LEFT OUTER JOIN V_CATALOG.COMMENTS c
  ON (s.SCHEMA_ID=c.OBJECT_ID)
WHERE 1=1
  /*if isNotEmpty(schemaName) */
  AND s.SCHEMA_NAME IN /*schemaName*/('%')
  /*end*/
ORDER BY s.SCHEMA_NAME