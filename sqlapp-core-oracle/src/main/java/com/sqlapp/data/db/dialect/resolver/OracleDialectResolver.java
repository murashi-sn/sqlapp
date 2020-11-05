/**
 * Copyright (C) 2007-2017 Tatsuo Satoh <multisqllib@gmail.com>
 *
 * This file is part of sqlapp-core-oracle.
 *
 * sqlapp-core-oracle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sqlapp-core-oracle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sqlapp-core-oracle.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sqlapp.data.db.dialect.resolver;

import com.sqlapp.data.db.dialect.Dialect;
import com.sqlapp.data.db.dialect.DialectUtils;
import com.sqlapp.data.db.dialect.Oracle;
import com.sqlapp.data.db.dialect.Oracle10g;
import com.sqlapp.data.db.dialect.Oracle11g;
import com.sqlapp.data.db.dialect.Oracle11gR2;
import com.sqlapp.data.db.dialect.Oracle12c;

/**
 * Dialect resolver for Oracle
 * 
 * @author satoh
 * 
 */
public class OracleDialectResolver extends ProductNameDialectResolver {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	public OracleDialectResolver() {
		super("Oracle.*", new OracleVersionResolver());
	}

	/**
	 * Oracle用のバージョンResolver
	 * 
	 * @author satoh
	 * 
	 */
	static class OracleVersionResolver implements VersionResolver {

		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;

		static class DialectHolder {
			final static Dialect oracle12cDialect = DialectUtils
					.getInstance(Oracle12c.class);
			final static Dialect oracle11gR2Dialect = DialectUtils.getInstance(
					Oracle11gR2.class, ()->oracle12cDialect);
			final static Dialect oracle11gDialect = DialectUtils.getInstance(
					Oracle11g.class, ()->oracle11gR2Dialect);
			final static Dialect oracle10gDialect = DialectUtils.getInstance(
					Oracle10g.class, ()->oracle11gDialect);
			final static Dialect defaultDialect = DialectUtils.getInstance(
					Oracle.class, ()->oracle10gDialect);
		}

		/**
		 * コンストラクタ
		 */
		public OracleVersionResolver() {
		}

		@Override
		public Dialect getDialect(int majorVersion, int minorVersion,
				Integer revision) {
			switch (majorVersion) {
			case 8:
				return DialectHolder.defaultDialect;
			case 9:
				return DialectHolder.defaultDialect;
			case 10:
				return DialectHolder.oracle10gDialect;
			case 11:
				if (minorVersion > 1) {
					return DialectHolder.oracle11gR2Dialect;
				} else {
					return DialectHolder.oracle11gDialect;
				}
			case 12:
				return DialectHolder.oracle12cDialect;
			}
			return DialectHolder.defaultDialect;
		}
	}
}
