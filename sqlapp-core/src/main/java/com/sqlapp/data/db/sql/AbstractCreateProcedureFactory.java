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
package com.sqlapp.data.db.sql;

import static com.sqlapp.util.CommonUtils.isEmpty;

import com.sqlapp.data.schemas.Procedure;
import com.sqlapp.util.AbstractSqlBuilder;
import com.sqlapp.util.CommonUtils;

/**
 * Create Procedure生成クラス
 * 
 * @author satoh
 * 
 */
public abstract class AbstractCreateProcedureFactory<S extends AbstractSqlBuilder<?>>
		extends AbstractCreateNamedObjectFactory<Procedure, S> {

	@Override
	protected void addCreateObject(final Procedure obj, S builder) {
		if (!isEmpty(obj.getDefinition())) {
			builder._add(obj.getDefinition());
		} else {
			builder.create().procedure();
			builder.name(obj, this.getOptions().isDecorateSchemaName());
			if (!CommonUtils.isEmpty(obj.getArguments())) {
				builder.arguments(obj.getArguments());
			}
			builder.lineBreak()._add(obj.getStatement());
		}
	}

}
