INSERT INTO "tableA" ( cola, colb, created_at, updated_at, lock_version ) VALUES (/*cola*/0, /*colb*/'', /*created_at*/CURRENT_TIMESTAMP, /*updated_at*/CURRENT_TIMESTAMP, 0 ) ON CONFLICT ON CONSTRAINT "PK_tableA" DO UPDATE (/*colb*/'', COALESCE(/*updated_at*/CURRENT_TIMESTAMP, CURRENT_TIMESTAMP), COALESCE( lock_version, 0 ) + 1)