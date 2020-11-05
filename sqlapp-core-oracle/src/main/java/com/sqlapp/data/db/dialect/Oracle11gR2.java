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
package com.sqlapp.data.db.dialect;

import java.util.function.Supplier;

import com.sqlapp.data.db.dialect.oracle.metadata.Oracle11gR2CatalogReader;
import com.sqlapp.data.db.dialect.oracle.sql.Oracle11gR2OperationFactoryRegistry;
import com.sqlapp.data.db.metadata.CatalogReader;
import com.sqlapp.data.db.sql.SqlFactoryRegistry;

/**
 * Oracle固有情報クラス
 * @author SATOH
 *
 */
public class Oracle11gR2 extends Oracle10g {
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6436736419643892528L;
	/**
	 * コンストラクタ
	 */
    protected Oracle11gR2(Supplier<Dialect> nextVersionDialectSupplier) {
		super(nextVersionDialectSupplier);
	}

	@Override
    public int hashCode(){
    	return getProductName().hashCode()+1;
    }

    /**
     * 同値判定
     */
	@Override
	public boolean equals(Object obj){
		if (!super.equals(obj)){
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.sqlapp.data.db.dialect.DbDialect#getCatalogReader()
	 */
	@Override
	public CatalogReader getCatalogReader() {
		return new Oracle11gR2CatalogReader(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sqlapp.data.db.dialect.Dialect#createDbOperationFactory()
	 */
	@Override
	protected SqlFactoryRegistry createSqlFactoryRegistry() {
		return new Oracle11gR2OperationFactoryRegistry(this);
	}
}
