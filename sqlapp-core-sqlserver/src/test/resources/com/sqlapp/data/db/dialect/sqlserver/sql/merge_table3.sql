MERGE tableA
USING
(
	SELECT 
	/*cola*/0 AS cola
	, /*colb*/'' AS colb
	, /*created_at*/CURRENT_TIMESTAMP AS created_at
	, /*updated_at*/CURRENT_TIMESTAMP AS updated_at
	, 0 AS lock_version
) AS _target
ON tableA.cola=_target.cola
WHEN MATCHED THEN
	UPDATE SET
		colb=_target.colb
		, updated_at=_target.updated_at
		, lock_version=lock_version + 1
WHEN NOT MATCHED THEN
	INSERT
	(
		colb
		, created_at
		, updated_at
		, lock_version
	)
	VALUES
	(
		_target.colb
		, _target.created_at
		, _target.updated_at
		, _target.lock_version
	)