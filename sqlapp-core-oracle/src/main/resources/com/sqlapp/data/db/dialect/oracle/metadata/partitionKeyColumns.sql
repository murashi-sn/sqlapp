SELECT *
FROM ALL_PART_KEY_COLUMNS
WHERE 1=1 
  /*if isNotEmpty(schemaName)*/
  AND OWNER IN /*schemaName*/('%')
  /*end*/
  /*if isNotEmpty(objectType)*/
  AND OBJECT_TYPE IN /*objectType*/('%')
  /*end*/
  /*if isNotEmpty(objectName)*/
  AND NAME IN /*objectName*/('%')
  /*end*/
ORDER BY COLUMN_POSITION
