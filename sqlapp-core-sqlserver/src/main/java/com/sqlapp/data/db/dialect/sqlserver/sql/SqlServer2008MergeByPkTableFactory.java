/**
 * Copyright (C) 2007-2017 Tatsuo Satoh <multisqllib@gmail.com>
 *
 * This file is part of sqlapp-core-sqlserver.
 *
 * sqlapp-core-sqlserver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sqlapp-core-sqlserver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with sqlapp-core-sqlserver.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sqlapp.data.db.dialect.sqlserver.sql;

import static com.sqlapp.util.CommonUtils.list;

import java.util.List;

import com.sqlapp.data.db.dialect.sqlserver.util.SqlServerSqlBuilder;
import com.sqlapp.data.db.sql.AbstractMergeByPkTableFactory;
import com.sqlapp.data.db.sql.SqlOperation;
import com.sqlapp.data.db.sql.SqlType;
import com.sqlapp.data.schemas.Column;
import com.sqlapp.data.schemas.Table;
import com.sqlapp.data.schemas.UniqueConstraint;
import com.sqlapp.util.CommonUtils;

public class SqlServer2008MergeByPkTableFactory extends AbstractMergeByPkTableFactory<SqlServerSqlBuilder>{
	
	@Override
	public List<SqlOperation> createSql(final Table table) {
		List<SqlOperation> sqlList = list();
		UniqueConstraint constraint=table.getConstraints().getPrimaryKeyConstraint();
		if (constraint==null){
			constraint=CommonUtils.first(table.getConstraints().getUniqueConstraints());
		}
		if (constraint==null){
			return super.createSql(table);
		}
		String targetTable=this.getOptions().getTableOptions().getTemporaryAlias().apply(table);
		SqlServerSqlBuilder builder = createSqlBuilder();
		builder.merge().space().name(table, this.getOptions().isDecorateSchemaName());
		builder.lineBreak();
		builder.using().space()._add("(");
		builder.appendIndent(1);
		builder.lineBreak();
		builder.select().space();
		boolean[] first=new boolean[]{true};
		for(Column column:table.getColumns()){
			if (this.isFormulaColumn(column)) {
				continue;
			}
			if (this.isAutoIncrementColumn(column)){
				String def=this.getValueDefinitionSimple(column);
				builder.$if(!CommonUtils.isEmpty(def), ()->{
					builder.comma(!first[0])._add(def).as().name(column);
					first[0]=false;
				});
			} else{
				String def=this.getValueDefinitionForInsert(column);
				builder.$if(!CommonUtils.isEmpty(def), ()->{
					builder.comma(!first[0])._add(def).as().name(column);
					first[0]=false;
				});
			}
		}
		builder.appendIndent(-1);
		builder.lineBreak();
		builder._add(")").as().space()._add(targetTable);
		builder.lineBreak();
		builder.on();
		first[0]=true;
		for(Column column:table.getColumns()){
			if (!constraint.getColumns().contains(column.getName())){
				continue;
			}
			builder.and(!first[0]).columnName(column, true).eq().names(targetTable, column.getName());
			first[0]=false;
		}
		builder.lineBreak();
		builder.when().matched().then();
		builder.appendIndent(1);
		builder.lineBreak();
		builder.update().set();
		first[0]=true;
		for(Column column:table.getColumns()){
			if (constraint.getColumns().contains(column.getName())){
				continue;
			}
			if (this.isFormulaColumn(column)) {
				continue;
			}
			String def=this.getValueDefinitionForUpdate(column);
			builder.and(!first[0]).name(column).eq();
			if (this.isOptimisticLockColumn(column)){
				builder._add(def);
			} else{
				if (this.withCoalesceAtUpdate(column)){
					builder.coalesce()._add('(', ()->{
						builder.names(column.getName()).comma();
						builder.names(targetTable, column.getName()).space();
					}, ')');
				} else{
					builder.names(targetTable, column.getName());
				}
			}
			first[0]=false;
		}
		builder.appendIndent(-1);
		builder.lineBreak();
		builder.when().not().matched().then();
		builder.appendIndent(1);
		builder.lineBreak();
		builder.insert().space()._add("(");
		first[0]=true;
		for(Column column:table.getColumns()){
			String def=this.getValueDefinitionForInsert(column);
			builder.$if(!CommonUtils.isEmpty(def), ()->{
				if (!this.isFormulaColumn(column)) {
					builder.comma(!first[0]).name(column);
					first[0]=false;
				}
			});
		}
		builder.space()._add(")").values();
		builder.space()._add("(");
		first[0]=true;
		for(Column column:table.getColumns()){
			String def=this.getValueDefinitionForInsert(column);
			builder.$if(!CommonUtils.isEmpty(def), ()->{
				if (!this.isFormulaColumn(column)) {
					builder.comma(!first[0]).names(targetTable, column.getName());
					first[0]=false;
				}
			});
		}
		builder.space()._add(")");
		builder.appendIndent(-1);
		addSql(sqlList, builder, SqlType.MERGE_BY_PK, table);
		return sqlList;
	}

}
