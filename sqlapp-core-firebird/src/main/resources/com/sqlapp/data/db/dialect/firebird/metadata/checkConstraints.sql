SELECT CC.RDB$CONSTRAINT_NAME AS constraint_name
, T.RDB$TRIGGER_NAME
, T.RDB$RELATION_NAME AS TABLE_NAME
, T.RDB$TRIGGER_TYPE
, T.RDB$TRIGGER_SOURCE
, T.RDB$DESCRIPTION
, T.RDB$TRIGGER_INACTIVE
, D.RDB$DEPENDED_ON_NAME
, D.RDB$FIELD_NAME AS COLUMN_NAME
FROM RDB$CHECK_CONSTRAINTS CC
INNER JOIN RDB$TRIGGERS T
  ON (CC.RDB$TRIGGER_NAME=T.RDB$TRIGGER_NAME)
INNER JOIN RDB$DEPENDENCIES D
  ON (CC.RDB$TRIGGER_NAME=D.RDB$DEPENDENT_NAME)
WHERE T.RDB$TRIGGER_TYPE=1
  /*if isNotEmpty(tableName) */
  AND T.RDB$RELATION_NAME IN /*tableName*/('%')
  /*end*/
  /*if readerOptions.excludeSystemObjects */
  AND CC.RDB$CONSTRAINT_NAME NOT LIKE 'RDB$%'
  /*end*/
  /*if isNotEmpty(constraintName) */
  AND T.RDB$SYSTEM_FLAG=0
  /*end*/
ORDER BY CC.RDB$CONSTRAINT_NAME, T.RDB$TRIGGER_SEQUENCE

