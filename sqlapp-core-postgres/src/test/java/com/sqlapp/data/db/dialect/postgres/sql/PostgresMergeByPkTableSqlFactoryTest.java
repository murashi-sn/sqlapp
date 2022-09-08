/**
 * Copyright (C) 2007-2017 Tatsuo Satoh <multisqllib@gmail.com>
 *
 * This file is part of sqlapp-core-postgres.
 *
 * sqlapp-core-postgres is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sqlapp-core-postgres is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with sqlapp-core-postgres.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sqlapp.data.db.dialect.postgres.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sqlapp.data.db.datatype.DataType;
import com.sqlapp.data.db.sql.SqlFactory;
import com.sqlapp.data.db.sql.SqlOperation;
import com.sqlapp.data.db.sql.SqlType;
import com.sqlapp.data.schemas.Column;
import com.sqlapp.data.schemas.Row;
import com.sqlapp.data.schemas.Table;
import com.sqlapp.util.CommonUtils;
import com.sqlapp.util.DateUtils;

/**
 * MySQL用のAlterコマンドテスト
 * 
 * @author tatsuo satoh
 * 
 */
public class PostgresMergeByPkTableSqlFactoryTest extends AbstractPostgresSqlFactoryTest {
	SqlFactory<Table> sqlFactory;

	@BeforeEach
	public void before() {
		sqlFactory = this.sqlFactoryRegistry.getSqlFactory(
				new Table(), SqlType.MERGE_BY_PK);
		sqlFactory.getOptions().getTableOptions().setTableComment(t->t.getDisplayName());
	}

	@Test
	public void testMergeRow1() throws ParseException {
		final Table table1 = getTable1("tableA");
		sqlFactory.getOptions().getTableOptions().setWithCoalesceAtUpdate(true);
		final List<SqlOperation> operations=sqlFactory.createSql(table1);
		final SqlOperation operation=CommonUtils.first(operations);
		final String expected = getResource("merge_table1.sql");
		assertEquals(expected, operation.getSqlText());
	}

	@Test
	public void testMergeRow2() throws ParseException {
		final Table table1 = getTable1("tableA");
		sqlFactory.getOptions().getTableOptions().setWithCoalesceAtUpdate(false);
		final List<SqlOperation> operations=sqlFactory.createSql(table1);
		final SqlOperation operation=CommonUtils.first(operations);
		final String expected = getResource("merge_table2.sql");
		assertEquals(expected, operation.getSqlText());
	}
	
	private Table getTable1(final String tableName) throws ParseException {
		final Table table = getTable(tableName);
		Column column = new Column("cola").setDataType(DataType.INT);
		column.setDisplayName("カラムA");
		table.getColumns().add(column);
		column = new Column("colb").setDataType(DataType.VARCHAR).setLength(50);
		column.setDisplayName("カラムB");
		table.getColumns().add(column);
		column = new Column("created_at").setDataType(DataType.TIMESTAMP);
		table.getColumns().add(column);
		column = new Column("updated_at").setDataType(DataType.TIMESTAMP);
		table.getColumns().add(column);
		column = new Column("lock_version").setDataType(DataType.INT);
		table.getColumns().add(column);
		table.setPrimaryKey(table.getColumns().get("cola"));
		//
		final Row row=table.newRow();
		row.put("cola", 1);
		row.put("colb", "bvalue");
		row.put("created_at", DateUtils.parse("2016-01-12 12:32:30", "yyyy-MM-dd HH:mm:ss"));
		row.put("updated_at", DateUtils.parse("2016-12-31 12:32:30", "yyyy-MM-dd HH:mm:ss"));
		table.getRows().add(row);
		return table;
	}

	private Table getTable(final String tableName) {
		final Table table = new Table(tableName);
		table.setDisplayName("テーブル名_"+tableName);
		return table;
	}
}
