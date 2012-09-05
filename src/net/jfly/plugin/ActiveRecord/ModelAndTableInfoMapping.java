package net.jfly.plugin.ActiveRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * 保存模型和表之间的映射关系
 */
public class ModelAndTableInfoMapping {

	private static final Map<Class<? extends Model<?>>, TableInfo> modelAndTableInfoMap = new HashMap<Class<? extends Model<?>>, TableInfo>();

	private static ModelAndTableInfoMapping instance = new ModelAndTableInfoMapping();

	private ModelAndTableInfoMapping() {
	}

	public static ModelAndTableInfoMapping getInstance() {
		return instance;
	}

	public void addModelAndTableInfoMapping(Class<? extends Model<?>> modelClass, TableInfo tableInfo) {
		modelAndTableInfoMap.put(modelClass, tableInfo);
	}

	@SuppressWarnings("rawtypes")
	public TableInfo getTableInfo(Class<? extends Model> modelClass) {
		TableInfo result = modelAndTableInfoMap.get(modelClass);
		if (result == null) {
			throw new RuntimeException("ModelAndTableInfoMap 中不存在 " + modelClass.getName() + " 与之对应的关系映射");
		}
		return result;
	}

}
