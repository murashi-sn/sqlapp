/**
 * Copyright (C) 2007-2017 Tatsuo Satoh <multisqllib@gmail.com>
 *
 * This file is part of sqlapp-core.
 *
 * sqlapp-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sqlapp-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with sqlapp-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sqlapp.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * JDBCのStatementをラップするクラス
 * 
 * @author satoh
 * 
 */
public class SqlappStatement extends AbstractStatement<Statement> {

	public SqlappStatement(Statement nativeObject, Connection connection) {
		super(nativeObject, connection);
	}

	@Override
	protected ResultSet getResultSet(ResultSet rs, Statement statement) {
		SqlappResultSet<Statement> resultSet=new SqlappResultSet<Statement>(rs, statement);
		this.initializeChild(resultSet);
		return resultSet;
	}

}
