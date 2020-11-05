SELECT
    r.ROUTINESCHEMA AS schema_name
  , r.ROUTINENAME AS routine_name
  , r.SPECIFICNAME AS specific_name
  , r.ROUTINEID AS id
  , CASE
    WHEN r.ORIGIN = 'E' THEN 'EXTERNAL'
    ELSE 'SQL'
    END AS ROUTINE_BODY
  , r.LANGUAGE
  , r.TEXT AS ROUTINE_DEFINITION
  , r.*
FROM SYSCAT.ROUTINES r
WHERE ROUTINETYPE='P'
  /*if isNotEmpty(schemaName) */
  AND rtrim(r.ROUTINESCHEMA) IN /*schemaName*/('%')
  /*end*/
  /*if isNotEmpty(procedureName) */
  AND rtrim(r.ROUTINENAME) IN /*procedureName*/('%')
  /*end*/
  AND rtrim(r.LANGUAGE) <>'' 
ORDER BY r.ROUTINESCHEMA, r.ROUTINENAME, r.SPECIFICNAME
WITH UR