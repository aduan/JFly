package net.jfly.core;

import java.text.ParseException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ������
 */
public abstract class Controller {
	// ������Ӧ
	private HttpServletRequest request;
	private HttpServletResponse response;

	void init(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	// 1.request �����������
	// ---------------------------------------Attribute---------------------------------------

	public Controller setAttribute(String name, Object value) {
		request.setAttribute(name, value);
		return this;
	}

	public Controller setAttributeMap(Map<String, Object> attributeMap) {
		for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());
		}
		return this;
	}

	public Controller removeAttribute(String name) {
		request.removeAttribute(name);
		return this;
	}

	@SuppressWarnings("unchecked")
	public Enumeration<String> getAttributeNames() {
		return request.getAttributeNames();
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name) {
		return (T) request.getAttribute(name);
	}

	public String getAttributeForString(String name) {
		return (String) request.getAttribute(name);
	}

	public Integer getAttributeForInt(String name) {
		return (Integer) request.getAttribute(name);
	}

	// ---------------------------------------Parameter---------------------------------------
	@SuppressWarnings("unchecked")
	public Map<String, String[]> getParameterMap() {
		return request.getParameterMap();
	}

	@SuppressWarnings("unchecked")
	public Enumeration<String> getParameterNames() {
		return request.getParameterNames();
	}

	public String[] getParameterValues(String name) {
		return request.getParameterValues(name);
	}

	public String getParameter(String name) {
		return request.getParameter(name);
	}

	public String getParameter(String name, String defaultValue) {
		String result = getParameter(name);
		return result != null ? result : defaultValue;
	}

	// request����ȡ�����ݲ��ҿ��Խ���ת���ض����������ͣ�Boolean,Byte,Short,Integer,Long,Float,Double
	// ~1:ת��ΪBoolean
	public Boolean getParameterToBoolean(String name) {
		String result = getParameter(name);
		if (result != null) {
			result = result.trim().toLowerCase();
			return Boolean.parseBoolean(result);
		}
		return null;
	}

	public Boolean getParameterToBoolean(String name, Boolean defaultValue) {
		Boolean result = getParameterToBoolean(name);
		return result != null ? result : defaultValue;
	}

	public Boolean[] getParameterValuesToBoolean(String name) {
		String[] values = request.getParameterValues(name);
		if (values == null) {
			return null;
		}
		Boolean[] result = new Boolean[values.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Boolean.parseBoolean(values[i]);
		}
		return result;
	}

	// ~2:ת��ΪByte[]
	public byte[] getParameterToByteArray(String name) {
		String result = getParameter(name);
		return result != null ? result.getBytes() : null;
	}

	public byte[] getParameterToByteArray(String name, byte[] defaultValue) {
		String result = getParameter(name);
		return result != null ? result.getBytes() : defaultValue;
	}

	public byte[][] getParameterValuesToByteArray(String name) {
		String[] values = request.getParameterValues(name);
		if (values == null) {
			return null;
		}
		byte[][] result = new byte[values.length][];
		for (int i = 0; i < result.length; i++) {
			result[i] = values[i].getBytes();
		}
		return result;
	}

	// ~3:ת��ΪShort
	public Short getParameterToShort(String name) {
		String result = getParameter(name);
		return result != null ? Short.parseShort(result) : null;
	}

	public Short getParameterToShort(String name, Short defaultValue) {
		String result = getParameter(name);
		return result != null ? Short.parseShort(result) : defaultValue;
	}

	public Short[] getParameterValuesToShort(String name) {
		String[] values = request.getParameterValues(name);
		if (values == null) {
			return null;
		}
		Short[] result = new Short[values.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Short.parseShort(values[i]);
		}
		return result;
	}

	// ~4:ת��ΪInteger
	public Integer getParameterToInt(String name) {
		String result = getParameter(name);
		return result != null ? Integer.parseInt(result) : null;
	}

	public Integer getParameterToInt(String name, Integer defaultValue) {
		String result = getParameter(name);
		return result != null ? Integer.parseInt(result) : defaultValue;
	}

	public Integer[] getParameterValuesToInt(String name) {
		String[] values = request.getParameterValues(name);
		if (values == null) {
			return null;
		}
		Integer[] result = new Integer[values.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(values[i]);
		}
		return result;
	}

	// ~5:ת��ΪLong
	public Long getParameterToLong(String name) {
		String result = getParameter(name);
		return result != null ? Long.parseLong(result) : null;
	}

	public Long getParameterToLong(String name, Long defaultValue) {
		String result = getParameter(name);
		return result != null ? Long.parseLong(result) : defaultValue;
	}

	public Long[] getParameterValuesToLong(String name) {
		String[] values = request.getParameterValues(name);
		if (values == null) {
			return null;
		}
		Long[] result = new Long[values.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Long.parseLong(values[i]);
		}
		return result;
	}

	// ~6:ת��ΪFloat
	public Float getParameterToFloat(String name) {
		String result = getParameter(name);
		return result != null ? Float.parseFloat(result) : null;
	}

	public Float getParameterToFloat(String name, Float defaultValue) {
		String result = getParameter(name);
		return result != null ? Float.parseFloat(result) : defaultValue;
	}

	public Float[] getParameterValuesToFloat(String name) {
		String[] values = request.getParameterValues(name);
		if (values == null) {
			return null;
		}
		Float[] result = new Float[values.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Float.parseFloat(values[i]);
		}
		return result;
	}

	// ~7:ת��ΪDouble
	public Double getParameterToDouble(String name) {
		String result = getParameter(name);
		return result != null ? Double.parseDouble(result) : null;
	}

	public Double getParameterToDouble(String name, Float defaultValue) {
		String result = getParameter(name);
		return result != null ? Double.parseDouble(result) : defaultValue;
	}

	public Double[] getParameterValuesToDouble(String name) {
		String[] values = request.getParameterValues(name);
		if (values == null) {
			return null;
		}
		Double[] result = new Double[values.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = Double.parseDouble(values[i]);
		}
		return result;
	}

	// ��request ParameterMap��������ݱ���

	@SuppressWarnings("unchecked")
	public Controller keepParameter() {
		Map<String, String[]> map = request.getParameterMap();
		for (Entry<String, String[]> entry : map.entrySet()) {
			String[] values = entry.getValue();
			if (values.length == 1) {
				request.setAttribute(entry.getKey(), values[0]);
			} else {
				request.setAttribute(entry.getKey(), values);
			}
		}
		return this;
	}

	public Controller keepParameter(String... names) {
		for (String name : names) {
			String[] values = request.getParameterValues(name);
			if (values != null) {
				if (values.length == 1) {
					request.setAttribute(name, values[0]);
				} else {
					request.setAttribute(name, values);
				}
			}
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public Controller keepParameter(String name, Class type) {
		String[] values = request.getParameterValues(name);
		if (values != null) {
			if (values.length == 1)
				try {
					request.setAttribute(name, TypeConverter.convert(values[0], type));
				} catch (ParseException e) {
				}
			else {
				request.setAttribute(name, values);
			}
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public Controller keepParameter(Class type, String... names) {
		if (type == String.class) {
			return keepParameter(names);
		}
		if (names != null) {
			for (String name : names) {
				keepParameter(name, type);
			}
		}
		return this;
	}

	public boolean parameterIsBlank(String name) {
		String value = request.getParameter(name);
		return value == null || value.trim().length() == 0;
	}

	public boolean isParamExists(String paramName) {
		return request.getParameterMap().containsKey(paramName);
	}

	// 2.session����
	public HttpSession getSession() {
		return request.getSession();
	}

	public HttpSession getSession(boolean create) {
		return request.getSession(create);
	}

	@SuppressWarnings("unchecked")
	public <T> T getSessionAttribute(String key) {
		// ˵������session����key��ȡ��ʱ��,���뱣֤sessionһ������,���������ָ�����
		HttpSession session = request.getSession(false);
		return session != null ? (T) session.getAttribute(key) : null;
	}

	public Controller setSessionAttribute(String key, Object value) {
		// java����Ĭ��request.getSession()���������session�򴴽�session
		request.getSession().setAttribute(key, value);
		return this;
	}

	public Controller removeSessionAttribute(String key) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(key);
		}
		return this;
	}

	// 2.cookie����
	public Cookie getCookie(String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	public String getCookieValue(String name, String defaultValue) {
		Cookie cookie = getCookie(name);
		return cookie != null ? cookie.getValue() : defaultValue;
	}

	public String getCookieValue(String name) {
		return getCookieValue(name, null);
	}

	public Cookie[] getCookies() {
		return request.getCookies();
	}

	public Controller addCookie(Cookie cookie) {
		response.addCookie(cookie);
		return this;
	}

	/**
	 * ����maxAgeInSeconds��-1: ��������ر������ر�cookie. 0:�������cookie. n>0:cookie��Ч���ʱ��
	 */
	public Controller addCookie(String name, String value, int maxAgeInSeconds, String path) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setPath(path);
		response.addCookie(cookie);
		return this;
	}

	public Controller addCookie(String name, String value, int maxAgeInSeconds) {
		addCookie(name, value, maxAgeInSeconds, "/");
		return this;
	}

	public Controller addCookie(String name, String value, int maxAgeInSeconds, String path, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setDomain(domain);
		cookie.setPath(path);
		response.addCookie(cookie);
		return this;
	}

	public Controller removeCookie(String name, String path) {
		addCookie(name, null, 0, path);
		return this;
	}

	public Controller removeCookie(String name) {
		addCookie(name, null, 0, "/");
		return this;
	}

}