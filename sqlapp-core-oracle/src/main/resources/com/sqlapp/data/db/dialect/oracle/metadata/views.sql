SELECT
  V.*
, O.*
, C.COMMENTS
FROM ALL_VIEWS V
INNER JOIN ALL_OBJECTS O
ON (V.OWNER=O.OWNER
    AND
    V.VIEW_NAME=O.OBJECT_NAME
    AND
    O.OBJECT_TYPE='VIEW')
LEFT OUTER JOIN ALL_TAB_COMMENTS C
ON (V.OWNER=C.OWNER
    AND
    V.VIEW_NAME=C.TABLE_NAME
    AND
    C.TABLE_TYPE='VIEW'
    AND
    C.COMMENTS IS NOT NULL)
WHERE 1=1
  /*if isNotEmpty(schemaName)*/
  AND V.OWNER IN /*schemaName*/('%')
  /*end*/
  /*if isNotEmpty(tableName)*/
  AND V.VIEW_NAME IN /*tableName*/('%')
  /*end*/
ORDER BY V.OWNER, V.VIEW_NAME