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

import java.util.function.Function;
import java.util.function.Predicate;

import com.sqlapp.data.schemas.Table;
import com.sqlapp.data.schemas.function.ColumnPredicate;
import com.sqlapp.data.schemas.function.RowColumnStringFunction;
import com.sqlapp.data.schemas.function.TableIntegerFunction;
import com.sqlapp.data.schemas.function.TablePredicate;
import com.sqlapp.data.schemas.function.TableSqlBuilder;
import com.sqlapp.data.schemas.function.TableStringFunction;
import com.sqlapp.util.AbstractSqlBuilder;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * テーブルオプション
 * 
 * @author tatsuo satoh
 * 
 */
/**
 * @author tatsuo satoh
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class TableOptions extends AbstractBean {
	/**
	 * Foreign Key Constraintを出力するか?
	 */
	private TablePredicate withForeignKeyConstraint = (table->true);

	public void setWithForeignKeyConstraint(TablePredicate withForeignKeyConstraint){
		this.withForeignKeyConstraint= withForeignKeyConstraint;
	}

	public void setWithForeignKeyConstraint(boolean bool){
		this.withForeignKeyConstraint= (table->bool);
	}

	/**
	 * Unique Constraintを出力するか?
	 */
	private TablePredicate withUniqueConstraint = (table->true);

	public void setWithUniqueConstraint(boolean bool){
		this.withUniqueConstraint= (table->bool);
	}

	public void setWithUniqueConstraint(TablePredicate withUniqueConstraint){
		this.withUniqueConstraint= withUniqueConstraint;
	}

	/**
	 * Check Constraintを出力するか?
	 */
	private TablePredicate withCheckConstraint = (table->true);

	public void setWithCheckConstraint(boolean bool){
		this.withCheckConstraint= (table->bool);
	}

	public void setWithCheckConstraint(TablePredicate withCheckConstraint){
		this.withCheckConstraint= withCheckConstraint;
	}
	
	/**
	 * Exclude Constraintを出力するか?
	 */
	private TablePredicate withExcludeConstraint = (table->true);

	public void setWithExcludeConstraint(boolean bool){
		this.withExcludeConstraint= (table->bool);
	}

	public void setWithExcludeConstraint(TablePredicate withExcludeConstraint){
		this.withExcludeConstraint= withExcludeConstraint;
	}

	/**
	 * DROP PARTITIONを出力するか?
	 */
	private TablePredicate allowDropPartition = (table->true);

	public void setAllowDropPartition(boolean bool){
		this.allowDropPartition= (table->bool);
	}

	public void setAllowDropPartition(TablePredicate allowDropPartition){
		this.allowDropPartition= allowDropPartition;
	}

	/**
	 * ADD PARTITIONを出力するか?
	 */
	private TablePredicate allowAddPartition = (table->true);

	public void setAllowAddPartition(boolean bool){
		this.allowAddPartition= (table->bool);
	}

	public void setAllowAddPartition(TablePredicate allowAddPartition){
		this.allowAddPartition= allowAddPartition;
	}
	/**
	 * DML COMMIT PER TABLE
	 */
	private boolean commitPerTable = false;
	private static int DEFAULT_DML_BATCH_SIZE=1;
	/**
	 * Batch Size for INSERT or UPDATE OR DELETE OR MERGE
	 */
	private TableIntegerFunction dmlBatchSize=(t->DEFAULT_DML_BATCH_SIZE);

	public void setDmlBatchSize(TableIntegerFunction dmlBatchSize){
		this.dmlBatchSize= dmlBatchSize;
	}
			
	public void setDmlBatchSize(int value){
		this.dmlBatchSize= (table->value);
	}
	/**
	 * Temporary Alias 
	 */
	private TableStringFunction temporaryAlias=(t->"_target");

	public void setTemporaryAlias(TableStringFunction temporaryAlias){
		this.temporaryAlias= temporaryAlias;
	}
			
	public void setTemporaryAlias(String value){
		this.temporaryAlias= (table->value);
	}
	
	/**
	 * Created At column Predicate
	 */
	private ColumnPredicate createdAtColumn = (c->c.getDataType().isDateTime()&&c.getName().equalsIgnoreCase("created_at"));
	/**
	 * Updated At column Predicate
	 */
	private ColumnPredicate updatedAtColumn = (c->c.getDataType().isDateTime()&&c.getName().equalsIgnoreCase("updated_at"));
	/**
	 * Optimistic Lock column Predicate
	 */
	private ColumnPredicate optimisticLockColumn = (c->c.getName().equalsIgnoreCase("lock_version")||c.getName().equalsIgnoreCase("version_no"));
	/**
	 * Function for insert row value.
	 */
	private RowColumnStringFunction insertRowSqlValue=(r, c, v)->v;
	/**
	 * ${readFileAsBytes('src/main/resources/path')}
	 */
	private Predicate<String> dynamicValue=(v)->v!=null&&v.startsWith("${")&&v.endsWith("}");
	/**
	 * Function for insert row value.
	 */
	private RowColumnStringFunction updateRowSqlValue=(r, c, v)->v;
	/**
	 * Optimistic Lock column insert
	 * COALESCE( column, 0 )
	 */
	private ColumnPredicate withCoalesceAtInsert = (c->false);

	public void setWithCoalesceAtInsert(boolean bool){
		this.withCoalesceAtInsert= (c->bool);
	}

	public void setWithCoalesceAtInsert(ColumnPredicate withCoalesceAtInsert){
		this.withCoalesceAtInsert= withCoalesceAtInsert;
	}

	/**
	 * Optimistic Lock column update
	 * COALESCE( column, 0 )
	 */
	private ColumnPredicate withCoalesceAtUpdate = (c->false);

	public ColumnPredicate getWithCoalesceAtUpdate(){
		return this.withCoalesceAtUpdate;
	}
	
	public void setWithCoalesceAtUpdate(boolean bool){
		this.withCoalesceAtUpdate= (c->bool);
	}

	public void setWithCoalesceAtUpdate(ColumnPredicate withCoalesceAtUpdate){
		this.withCoalesceAtUpdate= withCoalesceAtUpdate;
	}

	/**
	 * Auto Increment column Predicate
	 */
	private ColumnPredicate autoIncrementColumn = (c->c.isIdentity()||c.getDataType().isAutoIncrementable());
	/**
	 * 
	 */
	private TableSqlBuilder<AbstractSqlBuilder<?>> selectAllSql=null;
	/**
	 * INSERT SQL TYPE
	 */
	private SqlType insertSqlType = SqlType.INSERT;
	/**
	 * UPDATE SQL TYPE
	 */
	private SqlType updateSqlType = SqlType.UPDATE;
	/**
	 * DELETE SQL TYPE
	 */
	private SqlType deleteSqlType = SqlType.DELETE_BY_PK;
	/**
	 * TRUNCATE SQL TYPE
	 */
	private SqlType truncateSqlType = SqlType.TRUNCATE;
	/**
	 * TABLE LOCK MODE
	 */
	private Function<Table, TableLockMode> lockMode=t->TableLockMode.EXCLUSIVE;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public TableOptions clone() {
		return (TableOptions) super.clone();
	}

}
