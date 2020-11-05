SELECT
    fk.RDB$CONSTRAINT_NAME AS FK_NAME
  , fk.RDB$RELATION_NAME AS FKTABLE_NAME
  , isf.RDB$FIELD_NAME AS FKCOLUMN_NAME
  , pk.RDB$RELATION_NAME AS PKTABLE_NAME
  , isp.RDB$FIELD_NAME AS PKCOLUMN_NAME
  , rc.RDB$MATCH_OPTION AS MATCH_OPTION
  , rc.RDB$UPDATE_RULE AS UPDATE_RULE
  , rc.RDB$DELETE_RULE AS DELETE_RULE
  , pk.RDB$CONSTRAINT_NAME AS PK_NAME
  , fk.RDB$DEFERRABLE AS DEFERRABLE
  , fk.RDB$INITIALLY_DEFERRED AS INITIALLY_DEFERRED
FROM RDB$REF_CONSTRAINTS rc
INNER JOIN RDB$RELATION_CONSTRAINTS fk
  ON (rc.RDB$CONSTRAINT_NAME = fk.RDB$CONSTRAINT_NAME)
INNER JOIN RDB$RELATION_CONSTRAINTS pk
  ON (rc.RDB$CONST_NAME_UQ = pk.RDB$CONSTRAINT_NAME)
INNER JOIN RDB$INDEX_SEGMENTS isf 
  ON (fk.RDB$INDEX_NAME = isf.RDB$INDEX_NAME)
INNER JOIN RDB$INDEX_SEGMENTS isp
  ON (pk.RDB$INDEX_NAME = isp.RDB$INDEX_NAME
  AND isf.RDB$FIELD_POSITION=isp.RDB$FIELD_POSITION)
WHERE 1=1
  /*if isNotEmpty(constraintName)*/
  AND fk.RDB$CONSTRAINT_NAME IN /*constraintName*/('%')
  /*end*/
  /*if readerOptions.excludeSystemObjects */
  AND fk.RDB$RELATION_NAME NOT LIKE 'RDB$%'
  /*end*/
  /*if isNotEmpty(tableName)*/
  AND fk.RDB$RELATION_NAME IN /*tableName*/('%')
  /*end*/
ORDER BY fk.RDB$RELATION_NAME, fk.RDB$CONSTRAINT_NAME, isp.RDB$FIELD_POSITION
