/**
 * Copyright (c) 2011-2012, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.jfly.plugin.ActiveRecord;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@SuppressWarnings("rawtypes")
public abstract class Model<M extends Model> implements Serializable {
	private static Object[] Null_Object_Array = new Object[0];

	private static final long serialVersionUID = -4890964905769110400L;
	private Map<String, Object> attributeMap = new HashMap<String, Object>();

	private Set<String> modifiedAttributeFlagSet;

	private static final ModelAndTableInfoMapping modelAndTableInfoMapping = ModelAndTableInfoMapping.getInstance();

	private Set<String> getModifiedAttributeFlagSet() {
		if (modifiedAttributeFlagSet == null) {
			modifiedAttributeFlagSet = new HashSet<String>();
		}
		return modifiedAttributeFlagSet;
	}

	/**
	 * Set attribute to model.
	 */
	public Model<M> set(String attr, Object value) {
		if (modelAndTableInfoMapping.getTableInfo(getClass()).containsColumnName(attr)) {
			attributeMap.put(attr, value);
			getModifiedAttributeFlagSet().add(attr);
		} else {
			throw new ActiveRecordException("The attribute name is not exists: " + attr);
		}
		return this;
	}

	/**
	 * Put key value pair to the model when the key is not attribute of the
	 * model.
	 */
	public Model<M> put(String key, Object value) {
		attributeMap.put(key, value);
		return this;
	}

	/**
	 * Get attribute of any mysql type
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String attr) {
		return (T) attributeMap.get(attr);
	}

	/**
	 * Get attribute of any mysql type. Returns defaultValue if null.
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String attr, Object defaultValue) {
		Object result = attributeMap.get(attr);
		return (T) (result != null ? result : defaultValue);
	}

	/**
	 * Get attribute of mysql type: varchar, char, enum, set, text, tinytext,
	 * mediumtext, longtext
	 */
	public String getStr(String attr) {
		return (String) attributeMap.get(attr);
	}

	/**
	 * Get attribute of mysql type: int, integer, tinyint(n) n > 1, smallint,
	 * mediumint
	 */
	public Integer getInt(String attr) {
		return (Integer) attributeMap.get(attr);
	}

	/**
	 * Get attribute of mysql type: bigint
	 */
	public Long getLong(String attr) {
		return (Long) attributeMap.get(attr);
	}

	/**
	 * Get attribute of mysql type: date, year
	 */
	public java.sql.Date getDate(String attr) {
		return (java.sql.Date) attributeMap.get(attr);
	}

	/**
	 * Get attribute of mysql type: time
	 */
	public java.sql.Time getTime(String attr) {
		return (java.sql.Time) attributeMap.get(attr);
	}

	/**
	 * Get attribute of mysql type: timestamp, datetime
	 */
	public java.sql.Timestamp getTimestamp(String attr) {
		return (java.sql.Timestamp) attributeMap.get(attr);
	}

	/**
	 * Get attribute of mysql type: real, double
	 */
	public Double getDouble(String attr) {
		return (Double) attributeMap.get(attr);
	}

	/**
	 * Get attribute of mysql type: float
	 */
	public Float getFloat(String attr) {
		return (Float) attributeMap.get(attr);
	}

	/**
	 * Get attribute of mysql type: bit, tinyint(1)
	 */
	public Boolean getBoolean(String attr) {
		return (Boolean) attributeMap.get(attr);
	}

	/**
	 * Get attribute of mysql type: decimal, numeric
	 */
	public java.math.BigDecimal getBigDecimal(String attr) {
		return (java.math.BigDecimal) attributeMap.get(attr);
	}

	/**
	 * Get attribute of mysql type: binary, varbinary, tinyblob, blob,
	 * mediumblob, longblob
	 */
	public byte[] getBytes(String attr) {
		return (byte[]) attributeMap.get(attr);
	}

	/**
	 * Get attribute of any type that extends from Number
	 */
	public Number getNumber(String attr) {
		return (Number) attributeMap.get(attr);
	}

	@SuppressWarnings("unchecked")
	public Page<M> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
		if (pageNumber < 1 || pageSize < 1)
			throw new ActiveRecordException("pageNumber and pageSize must be more than 0");

		if (DbKit.getDialect().isTakeOverModelPaginate())
			return DbKit.getDialect().takeOverModelPaginate(getClass(), pageNumber, pageSize, select, sqlExceptSelect, paras);

		Connection conn = null;
		try {
			conn = DbKit.getConnection();
			long totalRow = 0;
			int totalPage = 0;
			List result = Db.query(conn, "select count(*) " + DbKit.replaceFormatSqlOrderBy(sqlExceptSelect), paras);
			int size = result.size();
			if (size == 1)
				totalRow = ((Number) result.get(0)).longValue(); // totalRow =
																	// (Long)result.get(0);
			else if (size > 1)
				totalRow = result.size();
			else
				return new Page<M>(new ArrayList<M>(0), pageNumber, pageSize, 0, 0); // totalRow
																						// =
																						// 0;

			totalPage = (int) (totalRow / pageSize);
			if (totalRow % pageSize != 0) {
				totalPage++;
			}

			StringBuilder sql = new StringBuilder();
			DbKit.getDialect().forPaginate(sql, pageNumber, pageSize, select, sqlExceptSelect);
			List<M> list = find(conn, sql.toString(), paras);
			return new Page<M>(list, pageNumber, pageSize, totalPage, (int) totalRow);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.close(conn);
		}
	}

	/**
	 * @see #paginate(int, int, String, String, Object...)
	 */
	public Page<M> paginate(int pageNumber, int pageSize, String select, String sqlExceptSelect) {
		return paginate(pageNumber, pageSize, select, sqlExceptSelect, Null_Object_Array);
	}

	/**
	 * Return attribute Map.
	 * <p>
	 * Danger! The update method will ignore the attribute if you change it
	 * directly. You must use set method to change attribute that update method
	 * can handle it.
	 */
	Map<String, Object> getAttributeMap() {
		return attributeMap;
	}

	/**
	 * Return attribute Set.
	 */
	public Set<Entry<String, Object>> getAttrsEntrySet() {
		return attributeMap.entrySet();
	}

	/**
	 * Save model.
	 */
	public boolean save() {
		TableInfo tableInfo = modelAndTableInfoMapping.getTableInfo(getClass());

		StringBuilder sql = new StringBuilder();
		List<Object> paras = new ArrayList<Object>();
		DbKit.getDialect().forModelSave(tableInfo, attributeMap, sql, paras);
		Connection conn = null;
		PreparedStatement pst = null;
		int result = 0;
		try {
			conn = DbKit.getConnection();
			boolean isSupportAutoIncrementKey = DbKit.getDialect().isSupportAutoIncrementKey();
			if (isSupportAutoIncrementKey)
				pst = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
			else
				pst = conn.prepareStatement(sql.toString());
			for (int i = 0, size = paras.size(); i < size; i++) {
				pst.setObject(i + 1, paras.get(i));
			}
			result = pst.executeUpdate();
			if (isSupportAutoIncrementKey)
				getGeneratedKey(pst, tableInfo);
			getModifiedAttributeFlagSet().clear();
			return result >= 1;
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.close(pst, conn);
		}
	}

	/**
	 * Get id after save method.
	 */
	private void getGeneratedKey(PreparedStatement pst, TableInfo tableInfo) throws SQLException {
		String pKey = tableInfo.getPrimaryKey();
		if (get(pKey) == null) {
			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				Class colType = tableInfo.getColumnType(pKey);
				if (colType == Integer.class || colType == int.class)
					set(pKey, rs.getInt(1));
				else if (colType == Long.class || colType == long.class)
					set(pKey, rs.getLong(1));
				else
					set(pKey, rs.getObject(1));
				rs.close();
			}
		}
	}

	/**
	 * Delete model.
	 */
	public boolean delete() {
		TableInfo tInfo = modelAndTableInfoMapping.getTableInfo(getClass());
		String pKey = tInfo.getPrimaryKey();
		Object id = attributeMap.get(pKey);
		if (id == null)
			throw new ActiveRecordException("You can't delete model whitout id.");
		return deleteById(tInfo, id);
	}

	/**
	 * Delete model by id.
	 * 
	 * @param id
	 *            the id value of the model
	 * @return true if delete succeed otherwise false
	 */
	public boolean deleteById(Object id) {
		if (id == null)
			throw new IllegalArgumentException("id can not be null");
		TableInfo tInfo = modelAndTableInfoMapping.getTableInfo(getClass());
		return deleteById(tInfo, id);
	}

	private boolean deleteById(TableInfo tInfo, Object id) {
		String sql = DbKit.getDialect().forModelDeleteById(tInfo);
		return Db.update(sql, id) >= 1;
	}

	/**
	 * Update model.
	 */
	public boolean update() {
		if (getModifiedAttributeFlagSet().isEmpty())
			return false;

		TableInfo tableInfo = modelAndTableInfoMapping.getTableInfo(getClass());
		String pKey = tableInfo.getPrimaryKey();
		Object id = attributeMap.get(pKey);
		if (id == null)
			throw new ActiveRecordException("You can't update model whitout Primary Key.");

		StringBuilder sql = new StringBuilder();
		List<Object> paras = new ArrayList<Object>();
		DbKit.getDialect().forModelUpdate(tableInfo, attributeMap, getModifiedAttributeFlagSet(), pKey, id, sql, paras);

		if (paras.size() <= 1) {
			return false;
		}

		Connection conn = null;
		try {
			conn = DbKit.getConnection();
			int result = Db.update(conn, sql.toString(), paras.toArray());
			if (result >= 1) {
				getModifiedAttributeFlagSet().clear();
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.close(conn);
		}
	}

	/**
	 * Find model.
	 */
	private List<M> find(Connection conn, String sql, Object... paras) throws Exception {
		Class<? extends Model> modelClass = getClass();
		if (DbKit.devMode)
			checkTableName(modelClass, sql);

		PreparedStatement pst = conn.prepareStatement(sql);
		for (int i = 0; i < paras.length; i++) {
			pst.setObject(i + 1, paras[i]);
		}

		ResultSet rs = pst.executeQuery();
		List<M> result = ModelBuilder.build(rs, modelClass);
		DbKit.closeYe(rs, pst);

		return result;
	}

	/**
	 * Find model.
	 * 
	 * @param sql
	 *            an SQL statement that may contain one or more '?' IN parameter
	 *            placeholders
	 * @param paras
	 *            the parameters of sql
	 * @return the list of Model
	 */
	public List<M> find(String sql, Object... paras) {
		Connection conn = null;
		try {
			conn = DbKit.getConnection();
			return find(conn, sql, paras);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.close(conn);
		}
	}

	/**
	 * Check the table name. The table name must in sql.
	 */
	private void checkTableName(Class<? extends Model> modelClass, String sql) {
		TableInfo tableInfo = modelAndTableInfoMapping.getTableInfo(modelClass);
		if (!sql.toLowerCase().contains(tableInfo.getTableName().toLowerCase()))
			throw new ActiveRecordException("The table name: " + tableInfo.getTableName() + " not in your sql.");
	}

	/**
	 * @see #find(String, Object...)
	 */
	public List<M> find(String sql) {
		return find(sql, Null_Object_Array);
	}

	/**
	 * Find first model. I recommend add "limit 1" in your sql.
	 * 
	 * @param sql
	 *            an SQL statement that may contain one or more '?' IN parameter
	 *            placeholders
	 * @param paras
	 *            the parameters of sql
	 * @return Model
	 */
	public M findFirst(String sql, Object... paras) {
		List<M> result = find(sql, paras);
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 * @see #findFirst(String, Object...)
	 * @param sql
	 *            an SQL statement
	 */
	public M findFirst(String sql) {
		List<M> result = find(sql, Null_Object_Array);
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 * Find model by id.
	 * 
	 * @param id
	 *            the id value of the model
	 */
	public M findById(Object id) {
		return findById(id, "*");
	}

	/**
	 * Find model by id. Fetch the specific columns only. Example: User user =
	 * User.dao.findById(15, "name, age");
	 * 
	 * @param id
	 *            the id value of the model
	 * @param columns
	 *            the specific columns separate with comma character ==> ","
	 */
	public M findById(Object id, String columns) {
		TableInfo tInfo = modelAndTableInfoMapping.getTableInfo(getClass());
		String sql = DbKit.getDialect().forModelFindById(tInfo, columns);
		List<M> result = find(sql, id);
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 * Set attributes with other model.
	 * 
	 * @param model
	 *            the Model
	 * @return this Model
	 */
	@SuppressWarnings("unchecked")
	public Model<M> setAttrs(M model) {
		return setAttrs(model.getAttributeMap());
	}

	/**
	 * Set attributes with Map.
	 * 
	 * @param model
	 *            the Model
	 * @return this Model
	 */
	public Model<M> setAttrs(Map<String, Object> attrs) {
		for (Entry<String, Object> e : attrs.entrySet()) {
			set(e.getKey(), e.getValue());
		}
		return this;
	}

	/**
	 * Remove attribute of this model.
	 * 
	 * @param attr
	 *            the attribute name of the model
	 * @return this model
	 */
	public Model<M> remove(String attr) {
		attributeMap.remove(attr);
		getModifiedAttributeFlagSet().remove(attr);
		return this;
	}

	/**
	 * Remove attributes of this model.
	 * 
	 * @param attrs
	 *            the attribute names of the model
	 * @return this model
	 */
	public Model<M> remove(String... attrs) {
		if (attrs != null)
			for (String a : attrs) {
				this.attributeMap.remove(a);
				this.getModifiedAttributeFlagSet().remove(a);
			}
		return this;
	}

	/**
	 * Remove attributes if it is null.
	 * 
	 * @return this model
	 */
	public Model removeNullValueAttrs() {
		for (Iterator<Entry<String, Object>> it = attributeMap.entrySet().iterator(); it.hasNext();) {
			Entry<String, Object> e = it.next();
			if (e.getValue() == null) {
				it.remove();
				getModifiedAttributeFlagSet().remove(e.getKey());
			}
		}
		return this;
	}

	/**
	 * Keep attributes of this model and remove other attributes.
	 * 
	 * @param attrs
	 *            the attribute names of the model
	 * @return this model
	 */
	public Model<M> keep(String... attrs) {
		if (attrs != null && attrs.length > 0) {
			Map<String, Object> newAttrs = new HashMap<String, Object>(attrs.length);
			Set<String> newModifyFlag = new HashSet<String>();
			for (String a : attrs) {
				if (this.attributeMap.containsKey(a)) // prevent put null value
														// to the
					// newColumns
					newAttrs.put(a, this.attributeMap.get(a));
				if (this.getModifiedAttributeFlagSet().contains(a))
					newModifyFlag.add(a);
			}
			this.attributeMap = newAttrs;
			this.modifiedAttributeFlagSet = newModifyFlag;
		} else {
			this.attributeMap.clear();
			this.getModifiedAttributeFlagSet().clear();
		}
		return this;
	}

	/**
	 * Keep attribute of this model and remove other attributes.
	 * 
	 * @param attributeMap
	 *            the attribute names of the model
	 * @return this model
	 */
	public Model<M> keep(String attr) {
		if (attributeMap.containsKey(attr)) {
			Object keepIt = attributeMap.get(attr);
			boolean keepFlag = getModifiedAttributeFlagSet().contains(attr);
			attributeMap.clear();
			getModifiedAttributeFlagSet().clear();
			attributeMap.put(attr, keepIt);
			if (keepFlag)
				getModifiedAttributeFlagSet().add(attr);
		} else {
			attributeMap.clear();
			getModifiedAttributeFlagSet().clear();
		}
		return this;
	}

	/**
	 * Remove all attributes of this model.
	 * 
	 * @return this model
	 */
	public Model<M> clear() {
		attributeMap.clear();
		getModifiedAttributeFlagSet().clear();
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append(" {");
		boolean flag = false;
		for (Entry<String, Object> e : attributeMap.entrySet()) {
			if (flag == false)
				flag = true;
			else
				sb.append(", ");

			Object value = e.getValue();
			if (value != null)
				value = value.toString();
			sb.append(e.getKey()).append(":").append(value);
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Model))
			return false;
		if (o == this)
			return true;
		return this.attributeMap.equals(((Model) o).attributeMap);
	}

	@Override
	public int hashCode() {
		return (attributeMap == null ? 0 : attributeMap.hashCode()) ^ (getModifiedAttributeFlagSet() == null ? 0 : getModifiedAttributeFlagSet().hashCode());
	}

	/**
	 * Find model by cache.
	 * 
	 * @see #find(String, Object...)
	 * @param cacheName
	 *            the cache name
	 * @param key
	 *            the key used to get date from cache
	 * @return the list of Model
	 */
	public List<M> findByCache(String cacheName, Object key, String sql, Object... paras) {
		ICache cache = DbKit.getCache();
		List<M> result = cache.get(cacheName, key);
		if (result == null) {
			result = find(sql, paras);
			cache.put(cacheName, key, result);
		}
		return result;
	}

	/**
	 * @see #findByCache(String, Object, String, Object...)
	 */
	public List<M> findByCache(String cacheName, Object key, String sql) {
		return findByCache(cacheName, key, sql, Null_Object_Array);
	}

	/**
	 * Paginate by cache.
	 * 
	 * @see #paginate(int, int, String, String, Object...)
	 * @param cacheName
	 *            the cache name
	 * @param key
	 *            the key used to get date from cache
	 * @return Page
	 */
	public Page<M> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
		ICache cache = DbKit.getCache();
		Page<M> result = cache.get(cacheName, key);
		if (result == null) {
			result = paginate(pageNumber, pageSize, select, sqlExceptSelect, paras);
			cache.put(cacheName, key, result);
		}
		return result;
	}

	/**
	 * @see #paginateByCache(String, Object, int, int, String, String,
	 *      Object...)
	 */
	public Page<M> paginateByCache(String cacheName, Object key, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
		return paginateByCache(cacheName, key, pageNumber, pageSize, select, sqlExceptSelect, Null_Object_Array);
	}
}
