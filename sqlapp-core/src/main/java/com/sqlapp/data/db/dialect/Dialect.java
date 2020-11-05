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
package com.sqlapp.data.db.dialect;

import static com.sqlapp.data.db.datatype.DataType.BIGINT;
import static com.sqlapp.data.db.datatype.DataType.BIT;
import static com.sqlapp.data.db.datatype.DataType.DECIMAL;
import static com.sqlapp.data.db.datatype.DataType.INT;
import static com.sqlapp.data.db.datatype.DataType.SMALLINT;
import static com.sqlapp.data.db.datatype.DataType.TINYINT;
import static com.sqlapp.util.CommonUtils.LEN_2GB;
import static com.sqlapp.util.CommonUtils.cast;
import static com.sqlapp.util.CommonUtils.isEmpty;
import static com.sqlapp.util.CommonUtils.size;
import static com.sqlapp.util.DateUtils.truncateMilisecond;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import com.sqlapp.data.converter.Converter;
import com.sqlapp.data.db.datatype.DbDataType;
import com.sqlapp.data.db.datatype.DbDataTypeCollection;
import com.sqlapp.data.db.datatype.DefaultJdbcTypeHandler;
import com.sqlapp.data.db.datatype.LengthProperties;
import com.sqlapp.data.db.datatype.PrecisionProperties;
import com.sqlapp.data.db.datatype.ScaleProperties;
import com.sqlapp.data.db.datatype.DataType;
import com.sqlapp.data.db.dialect.jdbc.metadata.JdbcCatalogReader;
import com.sqlapp.data.db.dialect.util.SqlSplitter;
import com.sqlapp.data.db.metadata.CatalogReader;
import com.sqlapp.data.db.metadata.MetadataReaderUtils;
import com.sqlapp.data.db.sql.SimpleSqlFactoryRegistry;
import com.sqlapp.data.db.sql.SqlFactoryRegistry;
import com.sqlapp.data.db.sql.SqlOperation;
import com.sqlapp.data.schemas.CascadeRule;
import com.sqlapp.data.schemas.Column;
import com.sqlapp.data.schemas.DbCommonObject;
import com.sqlapp.data.schemas.Domain;
import com.sqlapp.data.schemas.Index;
import com.sqlapp.data.schemas.IndexType;
import com.sqlapp.data.schemas.Schema;
import com.sqlapp.data.schemas.SchemaProperties;
import com.sqlapp.data.schemas.SchemaUtils;
import com.sqlapp.data.schemas.Table;
import com.sqlapp.data.schemas.properties.DataTypeLengthProperties;
import com.sqlapp.data.schemas.properties.DataTypeProperties;
import com.sqlapp.exceptions.FieldNotFoundException;
import com.sqlapp.jdbc.sql.JdbcHandler;
import com.sqlapp.jdbc.sql.node.SqlNode;
import com.sqlapp.util.AbstractSqlBuilder;
import com.sqlapp.util.CaseInsensitiveMap;
import com.sqlapp.util.CommonUtils;
import com.sqlapp.util.SqlBuilder;

/**
 * 標準的なDBのDialiect
 * 
 * @author satoh
 * 
 */
public class Dialect implements Serializable, Comparable<Dialect> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6651943701544804926L;

	public static final String COLUMN_SIZE = "SIZE";
	public static final String COLUMN_PRECISION = "PRECISION";
	public static final String COLUMN_SCALE = "SCALE";
	public static final String COLUMN_DEFAULT_VALUE = "DEFAULT_VALUE";

	/**
	 * システム予約スキーマ
	 */
	private String[] SYSTEM_SCHEMA = new String[] {};

	/**
	 * デフォルトのDBの製品名
	 */
	public static final String DefaultDbType = "Standard";
	/**
	 * データ型のコレクション
	 */
	private DbDataTypeCollection dbDataTypes = new DbDataTypeCollection();

	/**
	 * インデックスタイプ名のコレクション
	 */
	private Map<String, IndexType> indexTypeNameMap = new CaseInsensitiveMap<IndexType>();
	
	/**
	 * コンストラクタ
	 */
	protected Dialect(Supplier<Dialect> nextVersionDialectSupplier) {
		registerDataType();
		this.nextVersionDialectSupplier = nextVersionDialectSupplier;
	}

	/**
	 * データ型の登録
	 */
	protected void registerDataType() {
		// CHAR
		getDbDataTypes().addChar(254);
		// VARCHAR
		getDbDataTypes().addVarchar(2000);
		// LONGVARCHAR
		getDbDataTypes().addLongVarchar(2000);
		// CLOB
		getDbDataTypes().addClob("CLOB", LEN_2GB);
		// NCHAR
		getDbDataTypes().addNChar(254);
		// NVARCHAR
		getDbDataTypes().addNVarchar(2000);
		// LONGNVARCHAR
		getDbDataTypes().addLongNVarchar(2000);
		// NCLOB
		getDbDataTypes().addNClob("NCLOB", LEN_2GB);
		// Blob
		getDbDataTypes().addBlob("BLOB", LEN_2GB);
		// TINYINT
		getDbDataTypes().addTinyInt();
		// SMALLINT
		getDbDataTypes().addSmallInt();
		// INT
		getDbDataTypes().addInt();
		// BIGINT
		getDbDataTypes().addBigInt();
		// REAL
		getDbDataTypes().addReal();
		// Double
		getDbDataTypes().addDouble();
		// Date
		getDbDataTypes().addDate().setDefaultValueLiteral(
				getCurrentDateFunction());
		// Time
		getDbDataTypes().addTime().setDefaultValueLiteral(
				getCurrentTimeFunction());
		// Timestamp
		getDbDataTypes().addTimestamp().setDefaultValueLiteral(
				getCurrentTimestampFunction());
		// Decimal
		getDbDataTypes().addDecimal();
		// Numeric
		getDbDataTypes().addNumeric();
	}

	/**
	 * カラムに対応したDBのデータ型の詳細情報を取得します。型が見つからない場合は最も近い型を返します。
	 * 
	 * @param column カラム
	 * @return カラムに対応したDBのデータ型の詳細情報
	 */
	public DbDataType<?> getDbDataType(DataTypeLengthProperties<?> column) {
		if (column.getDataType()!=null&&(column.getDataType().isOther()
				|| column.getDataType().isDomain())) {
			if (column instanceof DbCommonObject){
				Schema schema = ((DbCommonObject<?>)column).getAncestor(Schema.class);
				if (schema != null) {
					Domain domain = schema.getDomains().get(
							column.getDataTypeName());
					if (domain != null) {
						return getDbDataTypes().getDbType(domain.getDataType(),
								domain.getLength());
					}
				}
			}
		}
		return getDbDataTypes().getDbType(column.getDataType(),
				column.getLength());
	}

	public boolean setDbType(int sqlType, String productDataType,
			Long lengthOrPrecision, Integer scale,
			DataTypeLengthProperties<?> column) {
		return setDbType(DataType.valueOf(sqlType), productDataType,
				lengthOrPrecision, scale,
				column);
	}
	
	public boolean setDbType(DataType dataType, String productDataType,
			Long lengthOrPrecision, Integer scale,
			DataTypeLengthProperties<?> column) {
		Set<DbDataType<?>> set = CommonUtils.set();
		return setDbType(dataType, productDataType,
				lengthOrPrecision, scale,
				column, set);
	}

	/**
	 * Columnへ値を設定します
	 * 
	 * @param dialect
	 * @param dataType
	 * @param productDataType
	 * @param lengthOrPrecision
	 */
	private boolean setDbType(DataType dataType, String productDataType,
			Long lengthOrPrecision, Integer scale, DataTypeLengthProperties<?> column, Set<DbDataType<?>> set) {
		if (dataType!=null&&(dataType.isType()||dataType.isDomain()||dataType.isOther())) {
			column.setDataType(dataType);
			SchemaUtils.setDataTypeNameInternal(productDataType, column);
			column.setLength(lengthOrPrecision);
			column.setScale(scale);
			return true;
		}
		DbDataType<?> dbDataType = getDbDataTypes().match(productDataType, lengthOrPrecision, column);
		if (set.contains(dbDataType)) {
			return false;
		}
		if (dbDataType == null) {
			column.setDataType(dataType);
		} else {
			dbDataType.parseAndSet(productDataType, column);
			set.add(dbDataType);
			DataType sarrogation=null;
			if (lengthOrPrecision==null) {
				lengthOrPrecision = column.getLength();
			}
			if (scale==null) {
				scale = column.getScale();
			}
			if (lengthOrPrecision!=null) {
				if (!dbDataType.matchLength(column)) {
					boolean bool=setDbType(column.getDataType(), productDataType, column.getLength(), column.getScale(), column, set);
					if (bool) {
						return bool;
					}
				}
				sarrogation = dbDataType.getSizeSarrogation(lengthOrPrecision);
			}
			if (sarrogation != null) {
				column.setDataType(sarrogation);
			}
		}
		dbDataType=this.getDbDataType(column);
		if (dbDataType!=null){
			set.add(dbDataType);
			if (dbDataType.isFixedLength()||dbDataType.isFixedPrecision()) {
				if (lengthOrPrecision != null) {
					column.setLength(lengthOrPrecision);
				} else {
					if (dbDataType instanceof LengthProperties) {
						column.setLength(((LengthProperties<?>)dbDataType).getDefaultLength());
					} else if (dbDataType instanceof PrecisionProperties) {
						column.setLength(((PrecisionProperties<?>)dbDataType).getDefaultPrecision());
					} else {
						column.setLength(null);
					}
				}
			} else {
				column.setLength(null);
			}
			if (dbDataType.isFixedScale()) {
				if (scale != null) {
					column.setScale(scale);
				} else {
					if (dbDataType instanceof ScaleProperties) {
						column.setScale(((ScaleProperties<?>)dbDataType).getDefaultScale());
					} else {
						column.setScale(null);
					}
				}
			} else {
				column.setScale(null);
			}
		} else{
			if (column.getDataType()==null) {
				if (dataType==null) {
					column.setDataType(DataType.OTHER);
				} else {
					column.setDataType(dataType);
				}
			}
			if (column.getDataType().isFixedSize()) {
				if (lengthOrPrecision != null) {
					column.setLength(lengthOrPrecision);
				}else {
					column.setLength(null);
				}
			}else{
				column.setLength(null);
			}
			if (column.getDataType().isFixedScale()) {
				if (scale != null) {
					column.setScale(scale);
				} else {
					column.setScale(null);
				}
			} else {
				column.setScale(null);
			}
		}
		if (column.getDataTypeName() == null) {
			if (column.getDataType()==null) {
				SchemaUtils.setDataTypeNameInternal(productDataType, column);
			} else if (column.getDataType().isOther()||column.getDataType().isDomain()||column.getDataType().isType()){
				SchemaUtils.setDataTypeNameInternal(productDataType, column);
			}
		} else {
			if (matchDataTypeName(column.getDataType(), column.getDataTypeName())){
				boolean bool=SchemaUtils.setDataTypeNameInternal(null, column);
				if (!bool){
					throw new FieldNotFoundException(SchemaProperties.DATA_TYPE_NAME.getLabel(), this);
				}
			}
		}
		return true;
	}

	public boolean setDbType(String productDataType, Long lengthOrPrecision,
			Integer scale, DataTypeLengthProperties<?> column) {
		return setDbType(null, productDataType, lengthOrPrecision,
				scale, column);
	}

	public void setDbType(String productDataType, DataTypeProperties<?> column) {
		Column temp=new Column();
		setDbType(productDataType, null,null, temp);
		column.setDataType(temp.getDataType());
		SchemaUtils.setDataTypeNameInternal(column.getDataTypeName(), temp);
	}

	/**
	 * インデックスタイプ名とインデックスタイプの設定
	 * 
	 * @param name
	 * @param indexType
	 */
	protected void setIndexTypeName(String name, IndexType indexType) {
		indexTypeNameMap.put(name, indexType);
	}

	/**
	 * DB製品名
	 */
	public String getProductName() {
		return DefaultDbType;
	}

	/**
	 * DB製品名(シンプル名)
	 */
	public String getSimpleName() {
		return DefaultDbType.toLowerCase();
	}

	/**
	 * LIMIT句のサポート
	 */
	public boolean supportsLimit() {
		return false;
	}

	/**
	 * Offset句のサポート
	 */
	public boolean supportsLimitOffset() {
		return false;
	}

	/**
	 * Identityのサポート
	 */
	public boolean supportsIdentity() {
		return false;
	}

	/**
	 * IDENTITYカラム作成時の構文
	 * 
	 * @return IDENTITYカラム作成時の構文
	 */
	public String getIdentityColumnString() {
		return null;
	}

	/**
	 * SEQUENCEのサポート
	 */
	public boolean supportsSequence() {
		return false;
	}

	/**
	 * WITHステートメントのサポート
	 */
	public boolean supportsWith() {
		return false;
	}

	/**
	 * WITHステートメント再帰のサポート
	 */
	public boolean supportsWithRecursive() {
		return false;
	}

	/**
	 * TOP句のサポート
	 */
	public boolean supportsTop() {
		return false;
	}

	/**
	 * ROWNUM句のサポート
	 */
	public boolean supportsRownum() {
		return false;
	}

	/**
	 * 関数オーバーロードのサポート
	 */
	public boolean supportsFunctionOverload() {
		return false;
	}

	/**
	 * プロシージャオーバーロードのサポート
	 */
	public boolean supportsProcedureOverload() {
		return false;
	}

	/**
	 * DOMAINのサポート
	 */
	public boolean supportsDomain() {
		return false;
	}

	/**
	 * DOMAIN制約時にカラムを示すキーワード
	 * 
	 * @return DOMAIN制約時にカラムを示すキーワード
	 */
	public String domainCheckConstraintColumnName() {
		return null;
	}

	/**
	 * IDENTITYのINSERT時の文字列表現
	 * 
	 * @return IDENTITYのINSERT時の文字列表現
	 */
	public String getIdentityInsertString() {
		return null;
	}

	/**
	 * IDENTITYのINSERT時のSELECT文
	 * 
	 * @return IDENTITYのINSERT時のSELECT文
	 */
	public String getIdentitySelectString() {
		return null;
	}

	public String getSequenceNextValString(String sequenceName) {
		return null;
	}

	/**
	 * Merge文のサポート
	 * 
	 * @return Merge文のサポート
	 */
	public boolean supportsMerge() {
		return false;
	}

	/**
	 * クォート開始文字
	 */
	public char getCloseQuote() {
		return '"';
	}

	/**
	 * クォート終了文字
	 */
	public char getOpenQuote() {
		return '"';
	}

	/**
	 * CHAR,VARCHARよりもNCHAR,NVARCHARを推奨するか SQL Server対策
	 * 
	 * @return CHAR,VARCHARよりもNCHAR,NVARCHARを推奨するか
	 */
	public boolean recommendsNTypeChar() {
		return false;
	}

	/**
	 * カラムに紐づくSEQUENCEのサポート PostgreSQLのserial4,serial8対策
	 * 
	 * @return カラムに紐づくSEQUENCEのサポート
	 */
	public boolean supportsColumnSequence() {
		return false;
	}


	/**
	 * カラムでの式をサポートするかを返します
	 * 
	 * @return カラムでの式をサポート
	 */
	public boolean supportsColumnFormula() {
		return false;
	}
	
	/**
	 * 現在日付の取得関数
	 */
	public String getCurrentDateFunction() {
		return "TO_DATE(TO_CHAR(CURRENT_TIMESTAMP, 'YYYY-MM-DD'), 'YYYY-MM-DD')";
	}

	/**
	 * 現在日時の取得関数
	 * 
	 * @return 現在日時の取得関数
	 */
	public String getCurrentDateTimeFunction() {
		return "CURRENT_DATETIME";
	}

	/**
	 * 現在時刻(Time)タイムゾーンなしの取得関数
	 * 
	 * @return 現在時刻(Time)タイムゾーンなしの取得関数
	 */
	public String getCurrentTimeFunction() {
		return "CURRENT_TIME";
	}

	/**
	 * 現在時刻(Timestamp)タイムゾーン付きの取得関数
	 * 
	 * @return 現在時刻(Timestamp)タイムゾーン付きの取得関数
	 */
	public String getCurrentTimeWithTimeZoneFunction() {
		return null;
	}

	/**
	 * 現在日時(Timestamp)の取得関数
	 * 
	 * @return 現在日時(Timestamp)の取得関数
	 */
	public String getCurrentTimestampFunction() {
		return "CURRENT_TIMESTAMP";
	}

	/**
	 * 現在日時(Timestamp)タイムゾーン付きの取得関数
	 * 
	 * @return 現在日時(Timestamp)タイムゾーン付きの取得関数
	 */
	public String getCurrentTimestampWithTimeZoneFunction() {
		return "CURRENT_TIMESTAMP";
	}

	/**
	 * 既定のスキーマ
	 * 
	 * @return 既定のスキーマ
	 */
	public String defaultSchema() {
		return null;
	}

	/**
	 * デフォルト値で関数の使用可能
	 * 
	 * @return デフォルト値で関数の使用可能
	 */
	public boolean supportsDefaultValueFunction() {
		return false;
	}

	/**
	 * DROPの制約のカスケード削除のサポート
	 */
	public boolean supportsDropCascade() {
		return false;
	}

	/**
	 * カスケード削除のサポート
	 */
	public boolean supportsCascadeDelete() {
		return false;
	}

	/**
	 * DBが入力された文字を大文字、小文字で扱う方法
	 * 
	 * @return DBが入力された文字を大文字、小文字で扱う方法
	 */
	public DefaultCase getDefaultCase() {
		return DefaultCase.UpperCase;
	}

	/**
	 * サポートしているDELETE時のカスケードのルールの判定
	 * 
	 * @param rule
	 * @return サポートしているDELETE時のカスケードのルールの判定
	 */
	public boolean supportsRuleOnDelete(CascadeRule rule) {
		if (rule == CascadeRule.None) {
			return true;
		}
		return false;
	}

	/**
	 * カスケード更新のサポート
	 */
	public boolean supportsCascadeUpdate() {
		return false;
	}

	/**
	 * DBカタログのサポート
	 */
	public boolean supportsCatalog() {
		return false;
	}

	/**
	 * DBスキーマのサポート
	 */
	public boolean supportsSchema() {
		return false;
	}

	/**
	 * インデックス名のテーブルスコープ
	 */
	public boolean supportsIndexNameTableScope() {
		return false;
	}

	/**
	 * サポートしているUPDATE時のカスケードのルールの判定
	 * 
	 * @param rule
	 */
	public boolean supportsRuleOnUpdate(CascadeRule rule) {
		if (rule == CascadeRule.None) {
			return true;
		}
		return false;
	}

	/**
	 * ON UPDATE,ON DELETEのオプションでRistrictをサポートしているか RISTRICTとNO ACTIONの違いは、
	 * RISTRICTが即時チェックで、NO ACTIONがトランザクションの最後にチェック
	 */
	public boolean supportsCascadeRistrict() {
		return false;
	}

	/**
	 * executeBatch実行時の成功時件数を 個別に返す事が出来るか?
	 */
	public boolean supportsBatchExecuteResult() {
		return true;
	}

	/**
	 * executeBatch実行時に生成されたキーを戻すことが出来るか?
	 */
	public boolean supportsBatchExecuteGeneratedKeys() {
		return false;
	}

	/**
	 * 文字列がクォートされているかの判定
	 * 
	 * @param target
	 * @return 文字列がクォートされているかの判定
	 */
	public boolean isQuoted(String target) {
		if (isEmpty(target)) {
			return false;
		}
		if (target.charAt(0) == getOpenQuote()
				&& target.charAt(target.length() - 1) == getCloseQuote()) {
			return true;
		}
		return false;
	}

	private Pattern quatePattern = Pattern.compile("[a-z0-9_$]+",
			Pattern.CASE_INSENSITIVE);

	/**
	 * クォートの必要性の判定
	 * 
	 * @param target
	 * @return クォートの必要性の判定
	 */
	public boolean needQuote(String target) {
		if (isEmpty(target)) {
			return false;
		}
		if (isQuoted(target)) {
			return false;
		}
		boolean ret = quatePattern.matcher(target).matches();
		if (ret) {
			if (getDefaultCase() == DefaultCase.LowerCase) {
				if (CommonUtils.eq(target.toLowerCase(), target)) {
					return false;
				}
			} else if (getDefaultCase() == DefaultCase.UpperCase) {
				if (CommonUtils.eq(target.toUpperCase(), target)) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * クォートします
	 * 
	 * @param target
	 * @return クォートした結果
	 */
	public String quote(String target) {
		if (isEmpty(target)) {
			return target;
		}
		if (isQuoted(target)) {
			return target;
		}
		if (!needQuote(target)){
			return target;
		}
		return doQuote(target);
	}
	
	protected String doQuote(String target){
		StringBuilder builder = new StringBuilder(target.length() + 2);
		builder.append(getOpenQuote()).append(target).append(getCloseQuote());
		return builder.toString();
	}

	/**
	 * クォートを解除します
	 * 
	 * @param target
	 * @return　クォートを解除した結果
	 */
	public String unQuote(String target) {
		if (isQuoted(target)) {
			return CommonUtils.unwrap(target, this.getCloseQuote(),
					this.getCloseQuote());
		} else {
			return target;
		}
	}

	/**
	 * このデータベースが、大文字小文字が混在する引用符なしの SQL 識別子を、 大文字小文字を区別しないで処理し、
	 * 大文字小文字混在で格納するかどうかを取得します。
	 * 
	 */
	public boolean storesMixedCaseIdentifiers() {
		return false;
	}

	/**
	 * このデータベースが、大文字小文字が混在する引用符なしの SQL 識別子を、 大文字小文字を区別しないで処理し、
	 * 小文字で格納するかどうかを取得します。
	 * 
	 */
	public boolean storesLowerCaseIdentifiers() {
		return false;
	}

	/**
	 * 入力された文字をDBの既定の文字に変換
	 * 
	 */
	public String nativeCaseString(String value) {
		if (isEmpty(value)) {
			return value;
		}
		if (!storesMixedCaseIdentifiers()) {
			if (storesLowerCaseIdentifiers()) {
				return value.toLowerCase();
			}
			return value.toUpperCase();
		}
		return value;
	}

	/**
	 * 楽観的ロックに使用するカラムの判定
	 * 
	 * @param column
	 *            判定対象のDataColumn
	 * @return <code>true</code>楽観的ロックカラム
	 */
	public boolean isOptimisticLockColumn(Column column) {
		if (column.getDataType().isNumeric()) {
			if ("LOCK_VERSION".equalsIgnoreCase(column.getName())
					|| "LockVersion".equalsIgnoreCase(column.getName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 数値型の設定
	 * 
	 * @param col
	 */
	protected void setDecimalType(Column column) {
		if (column.getScale() != 0) {
			return;
		}
		if (column.getLength() == 1) {
			column.setDataType(BIT);
		} else if (column.getLength() < 3) {
			column.setDataType(TINYINT);
		} else if (column.getLength() < 5) {
			column.setDataType(SMALLINT);
		} else if (column.getLength() < 10) {
			column.setDataType(INT);
		} else if (column.getLength() < 20) {
			column.setDataType(BIGINT);
		} else {
			column.setDataType(DECIMAL);
		}
	}

	/**
	 * カラムと値の定義からSQLでの定義を取得します。
	 * @param column カラム
	 * @param value 値
	 */
	@SuppressWarnings("unchecked")
	public String getSqlValueDefinition(Column column, Object value) {
		DbDataType<?> dbDataType = getDbDataType(column);
		if (dbDataType==null){
			column.setDataTypeName(column.getDataTypeName());
			System.out.println(column);
		}
		if (value == null) {
			if (column.isNotNull()){
				if (column.getDefaultValue()!=null){
					return column.getDefaultValue();
				}
			}
			return "NULL";
		} else {
			@SuppressWarnings("rawtypes")
			Converter converter = dbDataType.getSqlTextConverter();
			if (converter==null){
				converter = dbDataType.getConverter();
			}
			if (converter==null){
				converter = column.getConverter();
			}
			String text;
			if (column.getDataType()!=null&&column.getDataType().isBinary()&&value instanceof String){
				text = (String)value;
				return "'"+text+"'";
			} else{
				text = converter.convertString(converter.convertObject(value));
			}
			StringBuilder builder=new StringBuilder();
			if (dbDataType.getLiteralPrefix()!=null){
				builder.append(dbDataType.getLiteralPrefix());
			}
			if ("'".equals(dbDataType.getLiteralPrefix())||"N'".equalsIgnoreCase(dbDataType.getLiteralPrefix())){
				if ("'".equals(dbDataType.getLiteralSuffix())){
					builder.append(text.replace("'", "''"));
				} else{
					builder.append(text);
				}
			} else{
				builder.append(text);
			}
			if (dbDataType.getLiteralSuffix()!=null){
				builder.append(dbDataType.getLiteralSuffix());
			}
			return builder.toString();
		}
	}
	
	/**
	 * カラムと値の定義から表示用の値を取得します。
	 * @param column カラム
	 * @param value 値
	 */
	public String getValueForDisplay(Column column, Object value) {
		DbDataType<?> dbDataType = getDbDataType(column);
		if (value == null) {
			return "<NULL>";
		} else {
			@SuppressWarnings("rawtypes")
			Converter converter = dbDataType.getSqlTextConverter();
			if (converter==null){
				converter = dbDataType.getConverter();
			}
			if (converter==null){
				converter = column.getConverter();
			}
			@SuppressWarnings("unchecked")
			String text = converter.convertString(converter.convertObject(value));
			return text;
		}
	}
	
	/**
	 * システム予約しているスキーマ
	 * 
	 */
	public String[] getSystemSchema() {
		return SYSTEM_SCHEMA;
	}

	public DbDataTypeCollection getDbDataTypes() {
		return dbDataTypes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}
		return true;
	}

	/**
	 * 名称からindexタイプの取得
	 * 
	 * @param indexTypeName
	 */
	public IndexType getIndexType(String indexTypeName) {
		if (indexTypeNameMap.containsKey(indexTypeName)) {
			return indexTypeNameMap.get(indexTypeName);
		}
		return IndexType.parse(indexTypeName);
	}


	/**
	 * 指定されたインデックスタイプをサポートしているかを返します。
	 * 
	 * @param indexType インデックスタイプ
	 * @return サポートしている場合、<code>true</code>
	 */
	public boolean supportsIndexType(Table table, final Index obj, IndexType indexType) {
		return indexTypeNameMap.containsValue(indexType);
	}

	/**
	 * カタログメタデータ読み込みクラスの取得
	 * 
	 */
	public CatalogReader getCatalogReader() {
		return new JdbcCatalogReader(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getProductName().hashCode();
	}

	/**
	 * 次のバージョンのDialect
	 */
	private final Supplier<Dialect> nextVersionDialectSupplier;

	/**
	 * 次のバージョンのDialectを取得します
	 * 
	 */
	public Dialect getNextVersionDialect() {
		if (nextVersionDialectSupplier==null){
			return null;
		}
		return nextVersionDialectSupplier.get();
	}

	/**
	 * オブジェクトの名称を取得します
	 * 
	 * @param catalogName
	 * @param schemaName
	 * @param objectName
	 */
	public String getObjectFullName(final String catalogName,
			final String schemaName, final String objectName) {
		StringBuilder builder = new StringBuilder(size(catalogName)
				+ size(schemaName) + size(objectName) + 2);
		if (!isEmpty(catalogName)) {
			builder.append(catalogName);
			builder.append('.');
		}
		if (!isEmpty(schemaName)) {
			builder.append(schemaName);
			builder.append('.');
		}
		builder.append(objectName);
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	/**
	 * ミリ秒を除いたDateTimeをJDBCで扱うためのハンドラー
	 * 
	 * @author satoh
	 * 
	 */
	static class DateTimeTypeHandler extends DefaultJdbcTypeHandler {
		public DateTimeTypeHandler(java.sql.JDBCType jdbcType, Converter<?> converter) {
			super(jdbcType, converter);
		}

		/** serialVersionUID */
		private static final long serialVersionUID = -3446371652551511555L;

		@Override
		public void setObject(PreparedStatement stmt, int parameterIndex,
				Object x) throws SQLException {
			if (x == null) {
				stmt.setNull(parameterIndex, java.sql.JDBCType.TIMESTAMP.getVendorTypeNumber());
				return;
			}
			java.util.Date val = cast(this.statementConverter.convertObject(x));
			stmt.setObject(parameterIndex, truncateMilisecond(val),
					java.sql.JDBCType.TIMESTAMP);
		}
	}

	protected SqlFactoryRegistry createSqlFactoryRegistry() {
		return new SimpleSqlFactoryRegistry(this);
	}

	public SqlFactoryRegistry getSqlFactoryRegistry() {
		SqlFactoryRegistry sqlFactoryRegistry = createSqlFactoryRegistry();
		return sqlFactoryRegistry;
	}

	/**
	 * サポートされた型のセットを返します
	 * 
	 */
	public Set<Class<?>> supportedSchemaTypes() {
		return MetadataReaderUtils
				.supportedSchemaTypes(this.getCatalogReader());
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Dialect o) {
		if (this.equals(o)){
			return 0;
		}
		Set<Dialect> set=getNexts(this);
		if (set.contains(o)){
			return -1;
		} else{
			set=getNexts(o);
			if (set.contains(this)){
				return 1;
			} else{
				return 0; 
			}
		}
	}
	
	private Set<Dialect> getNexts(Dialect o){
		Set<Dialect> set=CommonUtils.linkedSet();
		while(true){
			if(o.getNextVersionDialect()==null||o==o.getNextVersionDialect()){
				break;
			} else{
				set.add(o.getNextVersionDialect());
				o=o.getNextVersionDialect();
			}
		}
		return set;
	}
	
	/**
	 * SQL標準のOffset rows onlyをサポートしているか?
	 */
	public boolean supportsStandardOffsetFetchRows(){
		return false;
	}
	
	public AbstractSqlBuilder<?> createSqlBuilder(){
		return new SqlBuilder(this);
	}
	
	public SqlSplitter createSqlSplitter(){
		return new SqlSplitter(this);
	}

	public JdbcHandler createJdbcHandler(SqlNode sqlNode){
		JdbcHandler jdbcHandler=new JdbcHandler(sqlNode);
		return jdbcHandler;
	}

	/**
	 * set a change SQL Delimiter text;
	 * @param operation
	 */
	public void setChangeAndResetSqlDelimiter(SqlOperation operation){
	}
	
	protected String getDelimiter(String sql, String[] delimiters){
		for(int i=0;i<delimiters.length;i++){
			String del=delimiters[i];
			if (!sql.contains( del)){
				return  del;
			}
		}
		int len=2;
		while(true){
			StringBuilder builder=new StringBuilder(len);
			for(int i=0;i<delimiters.length;i++){
				for(int j=0;j<len;j++){
					builder.append(delimiters[i]);
				}
				String del=builder.toString();
				if (!sql.contains( del)){
					return  del;
				}
			}
			len++;
		}
	}
	
	
	public boolean isDdlRollbackable(){
		return false;
	}

	public boolean matchDataTypeName(DataType dataType, String dataTypeName){
		if (dataType==null){
			return false;
		}
		if (dataTypeName==null){
			return false;
		}
		if (CommonUtils.eqIgnoreCase(dataType.getTypeName(), dataTypeName)){
			return true;
		}
		if (dataType==DataType.BOOLEAN&&"bool".equalsIgnoreCase(dataTypeName)){
			return true;
		}
		return false;
	}

}
