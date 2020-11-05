/**
 * Copyright (C) 2007-2017 Tatsuo Satoh <multisqllib@gmail.com>
 *
 * This file is part of sqlapp-command.
 *
 * sqlapp-command is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sqlapp-command is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with sqlapp-command.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sqlapp.data.db.command.version;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.sqlapp.data.db.command.version.DbVersionFileHandler.SqlFile;
import com.sqlapp.data.db.dialect.Dialect;
import com.sqlapp.data.db.dialect.util.SqlSplitter.SplitResult;
import com.sqlapp.data.schemas.DbConcurrencyException;
import com.sqlapp.data.schemas.Row;
import com.sqlapp.data.schemas.Table;

public class VersionDownCommand extends VersionUpCommand{

	@Override
	protected List<Row> getVersionRows(Table table, DbVersionHandler dbVersionHandler){
		List<Row> rows=dbVersionHandler.getRowsForVersionDown(table, getLastChangeToApply());
		return rows;
	}

	@Override
	protected boolean preCheck(Connection connection, Dialect dialect, Table table, Long id, Row row, DbVersionHandler dbVersionHandler) throws SQLException{
		if(!dbVersionHandler.exists(dialect, connection, table, id)){
			throw new DbConcurrencyException("row="+row);
		}
		return true;
	}

	@Override
	protected boolean startVersion(Connection connection, Dialect dialect, Table table, Row row, Long seriesNumber, DbVersionHandler dbVersionHandler) throws SQLException{
		return true;
	}
	
	@Override
	protected void finalizeVersion(Connection connection, Dialect dialect, Table table, Row row, Long id, DbVersionHandler dbVersionHandler) throws SQLException{
		dbVersionHandler.deleteVersion(connection, dialect, table, row);
	}

	@Override
	protected void errorVersion(Connection connection, Dialect dialect, Table table, Row row, Long id, DbVersionHandler dbVersionHandler) throws SQLException{
	}

	
	@Override
	protected List<SplitResult> getSqls(SqlFile sqlFile){
		return sqlFile.getDownSqls();
	}

	/**
	 * @return the setupSqlDirectory
	 */
	@Override
	public File getSetupSqlDirectory() {
		return null;
	}

	/**
	 * @return the finalizeSqlDirectory
	 */
	@Override
	public File getFinalizeSqlDirectory() {
		return null;
	}

}
