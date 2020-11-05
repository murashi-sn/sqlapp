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
package com.sqlapp.data.schemas;

import java.util.function.Supplier;

/**
 * ExternalTableコレクション
 * 
 * @author satoh
 * 
 */
public final class ExternalTableCollection extends
		AbstractSchemaObjectCollection<ExternalTable> implements
		HasParent<Schema>
, NewElement<ExternalTable, ExternalTableCollection>{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * コンストラクタ
	 */
	protected ExternalTableCollection() {
	}

	/**
	 * コンストラクタ
	 */
	protected ExternalTableCollection(Schema schema) {
		super(schema);
	}

	@Override
	protected Supplier<ExternalTableCollection> newInstance(){
		return ()->new ExternalTableCollection();
	}
	
	@Override
	public ExternalTableCollection clone(){
		return (ExternalTableCollection)super.clone();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqlapp.dataset.AbstractNamedObjectList#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj, EqualsHandler equalsHandler) {
		if (!(obj instanceof ExternalTableCollection)) {
			return false;
		}
		if (!super.equals(obj, equalsHandler)) {
			return false;
		}
		return equalsHandler.equalsResult(this, obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqlapp.data.schemas.Parent#getParent()
	 */
	@Override
	public Schema getParent() {
		return (Schema) super.getParent();
	}

	@Override
	public ExternalTable newElement() {
		return super.newElementInternal();
	}

	@Override
	protected Supplier<ExternalTable> getElementSupplier() {
		return ()->new ExternalTable();
	}
}
