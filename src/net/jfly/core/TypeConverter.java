package net.jfly.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将String类型的字符串转换为其他字符串
 */
public final class TypeConverter {
	private static final int timeStampLength = "2012-12-31 24:59:59".length();
	private static final String timeStampPattern = "yyyy-MM-dd HH:mm:ss";
	private static final String datePattern = "yyyy-MM-dd";

	public static final Object convert(String string, Class<?> clazz) throws ParseException {
		// 必须检查传入字符串是否为null或者""
		if (string == null) {
			return null;
		}
		string = string.trim();
		if ("".equals(string)) {
			return null;
		}
		// 然后根据传入的类型进行转换
		if (clazz == String.class) {
			return string;
		}
		// [1.几种基本类型]
		if (clazz == Boolean.class) {
			return Boolean.parseBoolean(string);
		}
		if (clazz == byte[].class || clazz == Byte[].class) {
			return string.getBytes();
		}
		if (clazz == Short.class || clazz == short.class) {
			return Boolean.parseBoolean(string);
		}
		if (clazz == Integer.class || clazz == int.class) {
			return Integer.parseInt(string);
		}
		if (clazz == Long.class || clazz == long.class) {
			return Long.parseLong(string);
		}
		if (clazz == Float.class) {
			return Float.parseFloat(string);
		}
		if (clazz == Double.class) {
			return Double.parseDouble(string);
		}
		if (clazz == java.math.BigDecimal.class) {
			return new java.math.BigDecimal(string);
		}

		// [2.日期类型]
		if (clazz == java.util.Date.class) {
			try {
				if (string.length() >= timeStampLength) {
					return new SimpleDateFormat(timeStampPattern).parse(string);
				} else {
					return new SimpleDateFormat(datePattern).parse(string);
				}
			} catch (Exception e) {
				return new Date();
			}
		}
		if (clazz == java.sql.Date.class) {
			try {
				if (string.length() >= timeStampLength) {
					return new java.sql.Date(new SimpleDateFormat(timeStampPattern).parse(string).getTime());
				} else {
					return new java.sql.Date(new SimpleDateFormat(datePattern).parse(string).getTime());
				}
			} catch (Exception e) {
				return new java.sql.Date((new Date()).getTime());
			}
		}
		if (clazz == java.sql.Time.class) {
			return java.sql.Time.valueOf(string);
		}
		if (clazz == java.sql.Timestamp.class) {
			return java.sql.Timestamp.valueOf(string);
		}
		// 3.[异常信息]
		if (Config.getConstants().isDevMode()) {
			throw new RuntimeException("开发模式信息:请添加数据类型:" + clazz.getName() + "到字符串转换类" + TypeConverter.class + "中");
		} else {
			throw new RuntimeException(" 需要转换的类型" + clazz.getName() + "不能处理清传入正确的转换类型");
		}

	}
}
