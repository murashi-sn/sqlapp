SELECT
  d.*
FROM sys.databases d
WHERE 1=1
  /*if isNotEmpty(catalogName) */
  AND d.name IN /*catalogName*/('%')
  /*end*/
ORDER BY d.name