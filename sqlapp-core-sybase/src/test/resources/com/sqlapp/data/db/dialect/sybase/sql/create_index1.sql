CREATE INDEX IDX_tableA1 ON tableA ( colC DESC )
INCLUDE ( colB )
WHERE colC>1
PAD_INDEX=ON
FILLFACTOR=40
ALLOW_ROW_LOCKS=ON
ALLOW_PAGE_LOCKS=ON