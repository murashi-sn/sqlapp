INSERT INTO "tableA" ( cola, colb, colc ) VALUES (1, 'bvalue', TIMESTAMP '2016-01-12 12:32:30' ) ON CONFLICT ON CONSTRAINT "PK_tableA" DO UPDATE ('bvalue', TIMESTAMP '2016-01-12 12:32:30')