package com.branch.util.services;

import static java.lang.String.format;
import static java.util.Arrays.copyOfRange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VCAP_SERVICES {
	private static final String vcapServicesString = System.getenv("VCAP_SERVICES");

	public static String get(String... path) {
		try {
			return getFrom(vcapServicesString, path);
		} catch (JSONException e) {
			return null;
		}
	}

	public static String getOrException(String... path) throws JSONException {
		return getFrom(vcapServicesString, path);
	}

	public static String getFrom(JSONObject vcapServicesJSON, String... path) throws JSONException {
		if (vcapServicesJSON == null || path == null)
			return null;
		return getValue(vcapServicesJSON, path);
	}

	public static String getFrom(String vcapServicesString, String... path) throws JSONException {
		if (vcapServicesString == null || path == null)
			return null;
		return getValue(new JSONObject(vcapServicesString), path);
	}

	private static String getValue(JSONObject obj, String... path) throws JSONException {
		if (path == null || path.length == 0)
			return obj == null ? null : obj.toString();
		else {
			String name = path[0];
			Object val = obj.opt(name);
			if (val != null)
				return getObjectValue(val, path);
			else
				throw new JSONException(format("Unable to find object child with name %s remaining path: %s", path[0],
						join(" ", copyOfRange(path, 1, path.length))));
		}
	}

	private static String getValue(JSONArray arr, String... path) throws JSONException {
		if (path == null || path.length == 0)
			return arr == null ? null : arr.toString();
		else {
			int idx = parseUnsignedInteger(path[0]);
			if (idx > -1) {
				Object val = arr.opt(idx);
				return getObjectValue(val, path);
			} else
				throw new JSONException(format("Unable to find array element with index %s remaining path: %s", path[0],
						join(" ", copyOfRange(path, 1, path.length))));
		}
	}

	private static String getObjectValue(Object val, String... path) throws JSONException {
		if (val instanceof JSONArray) {
			JSONArray arr = (JSONArray) val;
			if (path.length > 0)
				return getValue(arr, copyOfRange(path, 1, path.length));
			else
				return arr.toString();
		} else if (val instanceof JSONObject) {
			JSONObject obj = (JSONObject) val;
			if (path.length > 0)
				return getValue(obj, copyOfRange(path, 1, path.length));
			else
				return obj.toString();
		} else if (val instanceof String)
			return (String) val;
		else
			throw new JSONException(format("Unable to find children of %s remaining path: %s", val,
					join(" ", copyOfRange(path, 1, path.length))));
	}

	private static int parseUnsignedInteger(String s) {
		try {
			int i = Integer.parseInt(s);
			return i < 0 ? -1 : i;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private static String join(String delim, String... sa) {
		StringBuilder sb = new StringBuilder();
		for (String s : sa) {
			sb.append(s);
			sb.append(delim);
		}
		return sb.substring(0, sb.length() - delim.length());
	}

}
