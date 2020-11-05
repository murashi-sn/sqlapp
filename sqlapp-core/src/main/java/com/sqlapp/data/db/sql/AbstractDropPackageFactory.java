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

import com.sqlapp.data.schemas.Package;
import com.sqlapp.util.AbstractSqlBuilder;

/**
 * Package削除クラス
 * 
 * @author satoh
 * 
 */
public abstract class AbstractDropPackageFactory<S extends AbstractSqlBuilder<?>>
		extends AbstractDropNamedObjectFactory<Package, S> {

	@Override
	protected void addDropObject(Package obj, S builder) {
		builder.drop().space()._add("PACKAGE");
		builder.name(obj, this.getOptions().isDecorateSchemaName());
	}

}
