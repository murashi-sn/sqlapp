/**
 * Copyright (C) 2007-2017 Tatsuo Satoh <multisqllib@gmail.com>
 *
 * This file is part of sqlapp-core-test.
 *
 * sqlapp-core-test is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sqlapp-core-test is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with sqlapp-core-test.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sqlapp.data.db.command.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;

import com.sqlapp.data.db.command.AbstractExportAndGenerateCreateSqlTest;
import com.sqlapp.data.db.command.ExportXmlCommand;
import com.sqlapp.jdbc.JdbcUtils;

public class MySqlExportAndGenerateCreateSqlTest extends AbstractExportAndGenerateCreateSqlTest{
	/**
	 * JDBC URL
	 */
	private String url;

	/**
	 * JDBC Driver Class Name
	 */
	private String driverClassName=JdbcUtils
			.getDriverClassNameByUrl(this.getUrl());
	
	public MySqlExportAndGenerateCreateSqlTest(){
		this.url=getTestProp("mysql.jdbc.url");
		this.username=getTestProp("mysql.jdbc.username");
		this.password=getTestProp("mysql.jdbc.password");
		String schemas=getTestProp("mysql.schemas");
		this.setIncludeSchemas(schemas.split(","));
		this.setTarget("schemas");
	}
	
	@BeforeEach
	public void before(){
		this.setIncludeRowDumpTables("sequence_numbers");
		this.setDumpRows(true);
	}
	
	/**
	 * JDBC User Name
	 */
	private String username;
	/**
	 * JDBC Password
	 */
	private String password;
	
	@Override
	protected void initialize(Connection connection) throws SQLException {
	}

	@Override
	protected void initialize(ExportXmlCommand command) throws SQLException {
	}
	
	/**
	 * @return the driverClassName
	 */
	public String getDriverClassName() {
		return driverClassName;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	
}
