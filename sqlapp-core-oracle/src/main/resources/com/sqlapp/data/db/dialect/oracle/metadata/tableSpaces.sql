SELECT
      A.*
    , B.GROUP_NAME
FROM DBA_TABLESPACES A
LEFT OUTER JOIN DBA_TABLESPACE_GROUPS B
  ON (A.TABLESPACE_NAME=B.TABLESPACE_NAME)
WHERE 1=1 
  /*if isNotEmpty(tableSpaceName)*/
  AND A.TABLESPACE_NAME IN /*tableSpaceName*/('%')
  /*end*/
ORDER BY A.TABLESPACE_NAME
