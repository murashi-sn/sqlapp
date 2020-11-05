/**
 * Copyright (C) 2007-2017 Tatsuo Satoh <multisqllib@gmail.com>
 *
 * This file is part of sqlapp-core-hsql.
 *
 * sqlapp-core-hsql is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sqlapp-core-hsql is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with sqlapp-core-hsql.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sqlapp.data.db.dialect.hsql.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.sqlapp.data.db.dialect.Dialect;
import com.sqlapp.data.db.dialect.DialectUtils;
import com.sqlapp.data.db.dialect.Hsql;
import com.sqlapp.data.db.dialect.util.SqlSplitter;
import com.sqlapp.data.db.dialect.util.SqlSplitter.SplitResult;
import com.sqlapp.test.AbstractTest;


public class HsqlSqlSplitterTest extends AbstractTest{

	Dialect dialect=DialectUtils.getInstance(Hsql.class);

	@Test
	public void test1() {
		String text=this.getResource("test1.sql");
		SqlSplitter sqlSplitter=dialect.createSqlSplitter();
		
		List<SplitResult> splits=sqlSplitter.parse(text);
		assertEquals(9, splits.size());
		int i=0;
		assertEquals("CREATE TABLE employee (id INT, \n                       name VARCHAR(10), \n                       salary DECIMAL(9,2))", splits.get(i++).getText());
	}
	

}
