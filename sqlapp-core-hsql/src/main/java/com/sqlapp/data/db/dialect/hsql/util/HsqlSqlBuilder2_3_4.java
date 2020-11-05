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

import com.sqlapp.data.db.dialect.Dialect;
import com.sqlapp.data.schemas.Column;
import com.sqlapp.util.CommonUtils;

/**
 * HSQL用のSQLビルダー
 * 
 * @author tatsuo satoh
 * 
 */
public class HsqlSqlBuilder2_3_4 extends HsqlSqlBuilder {

	public HsqlSqlBuilder2_3_4(Dialect dialect) {
		super(dialect);
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected HsqlSqlBuilder2_3_4 instance() {
		return (HsqlSqlBuilder2_3_4)super.instance();
	}

	@Override
	protected void onUpdateDefinition(Column column){
		if (!CommonUtils.isEmpty(column.getOnUpdate())) {
			this.on().update().space()._add(column.getOnUpdate());
		}
	}
}
