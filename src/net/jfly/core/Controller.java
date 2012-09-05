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
 * 控制器
 */
// request里面取出数据并且可以进行转换特定的数据类型：Boolean,Byte,Short,Integer,Long,Float,Double
public abstract class Controller {
	private static final String[] Null_Url_Param_Array = new String[0];
	private static final String Url_Param_Separator = Config.getConstants().getUrlParamSeparator();

	// 请求响应
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String urlParam;
	private String[] urlParamArray;

	void init(HttpServletRequest request, HttpServletResponse response, String urlParam) {
		this.request = request;
		this.response = response;
		// 将分隔符一起放入urlParam中
		this.urlParam = urlParam;
	}

	public void setUrlPara(String urlParam) {
		this.urlParam = urlParam;
		this.urlParamArray = null;
	}

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

	// 1.request 请求参数处理
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

	// request里面取出数据并且可以进行转换特定的数据类型：Boolean,Byte,Short,Integer,Long,Float,Double
	// ~1:转换为Boolean
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

	// ~2:转换为Byte[]
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

	// ~3:转换为Short
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

	// ~4:转换为Integer
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

	// ~5:转换为Long
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

	// ~6:转换为Float
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

	// ~7:转换为Double
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

	// 将request ParameterMap里面的数据保留

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

	@SuppressWarnings({ "rawtypes" })
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

	@SuppressWarnings("rawtypes")
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

	// ~存在与否和为空判断
	public boolean parameterIsBlank(String name) {
		String value = request.getParameter(name);
		return value == null || value.trim().length() == 0;
	}

	public boolean isParamExists(String paramName) {
		return request.getParameterMap().containsKey(paramName);
	}

	// 2.session处理
	public HttpSession getSession() {
		return request.getSession();
	}

	public HttpSession getSession(boolean create) {
		return request.getSession(create);
	}

	@SuppressWarnings("unchecked")
	public <T> T getSessionAttribute(String key) {
		// 说明：从session依据key读取的时候,必须保证session一定存在,否则产生空指针错误
		HttpSession session = request.getSession(false);
		return session != null ? (T) session.getAttribute(key) : null;
	}

	public Controller setSessionAttribute(String key, Object value) {
		// java机制默认request.getSession()如果不存在session则创建session
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

	// 3.cookie处理
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
	 * 参数maxAgeInSeconds：-1: 当浏览器关闭立即关闭cookie. 0:马上清除cookie. n>0:cookie有效存活时间
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

	// 4:Url参数处理
	// ---------------------------------------urlParam---------------------------------------
	// :说明：传递过的做原始数据,为了安全着想不能设置默认值，不能让没有参数的请求进行业务处理
	public String getUrlParam() {
		if ("".equals(urlParam)) {
			// 这样做只用检查是否为null,而不用检查是否为""
			urlParam = null;
		}
		return urlParam;
	}

	public Boolean getUrlParamToBoolean() {
		String result = getUrlParam();
		return result != null ? Boolean.parseBoolean(result) : null;
	}

	public byte[] getUrlParamToByteArray() {
		String result = getUrlParam();
		return result != null ? result.getBytes() : null;
	}

	public Short getUrlParamToShort() {
		String result = getUrlParam();
		return result != null ? Short.parseShort(result) : null;
	}

	public Integer getUrlParamToInt() {
		String result = getUrlParam();
		return result != null ? Integer.parseInt(result) : null;
	}

	public Long getUrlParamToLong() {
		String result = getUrlParam();
		return result != null ? Long.parseLong(result) : null;
	}

	public Float getUrlParamToFloat() {
		String result = getUrlParam();
		return result != null ? Float.parseFloat(result) : null;
	}

	public Double getUrlParamToDouble() {
		String result = getUrlParam();
		return result != null ? Double.parseDouble(result) : null;
	}

	// ---------------------------------------urlParamArray---------------------------------------
	public String getUrlParam(int index) {
		if (index < 0) {
			return getUrlParam();
		}

		if (urlParamArray == null) {
			if (urlParam == null || "".equals(urlParam)) {
				urlParamArray = Null_Url_Param_Array;
			} else {
				urlParamArray = urlParam.split(Url_Param_Separator);
			}
		}
		return urlParamArray.length > index ? urlParamArray[index] : null;
	}

	public String getUrlParam(int index, String defaultValue) {
		String result = getUrlParam(index);
		return result != null && !"".equals(result) ? result : defaultValue;
	}

	public Boolean getUrlParamToBoolean(int index) {
		String result = getUrlParam(index);
		return result != null ? Boolean.parseBoolean(result) : null;
	}

	public Boolean getUrlParamToBoolean(int index, Boolean defaultValue) {
		String result = getUrlParam(index);
		return result != null ? Boolean.parseBoolean(result) : defaultValue;
	}

	public byte[] getUrlParamToByteArray(int index) {
		String result = getUrlParam(index);
		return result != null ? result.getBytes() : null;
	}

	public byte[] getUrlParamToByteArray(int index, Integer defaultValue) {
		String result = getUrlParam(index);
		return (byte[]) (result != null ? result.getBytes() : defaultValue);
	}

	public Short getUrlParamToShort(int index) {
		String result = getUrlParam(index);
		return result != null ? Short.parseShort(result) : null;
	}

	public Short getUrlParamToShort(int index, Short defaultValue) {
		String result = getUrlParam(index);
		return result != null ? Short.parseShort(result) : defaultValue;
	}

	public Integer getUrlParamToInt(int index) {
		String result = getUrlParam(index);
		return result != null ? Integer.parseInt(result) : null;
	}

	public Integer getUrlParamToInt(int index, Integer defaultValue) {
		String result = getUrlParam(index);
		return result != null ? Integer.parseInt(result) : defaultValue;
	}

	public Long getUrlParamToLong(int index) {
		String result = getUrlParam(index);
		return result != null ? Long.parseLong(result) : null;
	}

	public Long getUrlParamToLong(int index, Long defaultValue) {
		String result = getUrlParam(index);
		return result != null ? Long.parseLong(result) : defaultValue;
	}

	public Float getUrlParamToFloat(int index) {
		String result = getUrlParam(index);
		return result != null ? Float.parseFloat(result) : null;
	}

	public Float getUrlParamToFloat(int index, Float defaultValue) {
		String result = getUrlParam(index);
		return result != null ? Float.parseFloat(result) : defaultValue;
	}

	public Double getUrlParamToDouble(int index) {
		String result = getUrlParam(index);
		return result != null ? Double.parseDouble(result) : null;
	}

	public Double getUrlParamToDouble(int index, Double defaultValue) {
		String result = getUrlParam(index);
		return result != null ? Double.parseDouble(result) : defaultValue;
	}

	// ~存在与否和为空判断
	public boolean urlParamIsBlank(int index) {
		String value = getUrlParam(index);
		return value == null || value.trim().length() == 0;
	}

	public boolean urlParamIsExists(int index) {
		return getUrlParam(index) != null;
	}

}