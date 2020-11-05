/**
 * Copyright (C) 2007-2017 Tatsuo Satoh <multisqllib@gmail.com>
 *
 * This file is part of sqlapp-core-mysql.
 *
 * sqlapp-core-mysql is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sqlapp-core-mysql is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with sqlapp-core-mysql.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sqlapp.data.db.dialect.resolver;

import com.sqlapp.data.db.dialect.Dialect;
import com.sqlapp.data.db.dialect.DialectUtils;
import com.sqlapp.data.db.dialect.MySql;
import com.sqlapp.data.db.dialect.MySql565;
import com.sqlapp.data.db.dialect.MySql570;
import com.sqlapp.data.db.dialect.MySql800;
import com.sqlapp.data.db.dialect.MySql801;

/**
 * Dialect resolver for MySql
 * 
 * @author satoh
 * 
 */
public class MySqlDialectResolver extends ProductNameDialectResolver {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public MySqlDialectResolver() {
		super("MySql", new MySqlVersionResolver());
	}

	/**
	 * MySql用のバージョンResolver
	 * 
	 * @author satoh
	 * 
	 */
	static class MySqlVersionResolver implements VersionResolver {

		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		static class DialectHolder {
			final static Dialect mysql801Dialect = DialectUtils
					.getInstance(MySql801.class);
			final static Dialect mysql800Dialect = DialectUtils
					.getInstance(MySql800.class, ()->mysql801Dialect);
			final static Dialect mysql570Dialect = DialectUtils
					.getInstance(MySql570.class, ()->mysql800Dialect);
			final static Dialect mysql565Dialect = DialectUtils
					.getInstance(MySql565.class, ()->mysql570Dialect);
			final static Dialect mysql564Dialect = DialectUtils.getInstance(
					MySql.class, ()->mysql565Dialect);
			final static Dialect defaultDialect = DialectUtils.getInstance(
					MySql.class, ()->mysql564Dialect);
		}

		/**
		 * コンストラクタ
		 */
		public MySqlVersionResolver() {
		}

		@Override
		public Dialect getDialect(int majorVersion, int minorVersion,
				Integer revision) {
			switch (majorVersion) {
			case 5:
				switch (minorVersion) {
				case 6:
					if (revision!=null&&revision.intValue()>=5){
						return DialectHolder.mysql565Dialect;
					}
					if (revision!=null&&revision.intValue()>=4){
						return DialectHolder.mysql564Dialect;
					}
				case 7:
					return DialectHolder.mysql570Dialect;
				case 8:
					return DialectHolder.mysql800Dialect;
				}
				break;
			}
			return DialectHolder.defaultDialect;
		}

	}

}
