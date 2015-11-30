/*
 * Copyright (c) 2015 Appfire Technologies, Inc.
 * All rights reserved.
 *
 * This software is licensed under the provisions of the "Bob Swift Atlassian Add-ons EULA"
 * (https://bobswift.atlassian.net/wiki/x/WoDXBQ) as well as under the provisions of
 * the "Standard EULA" from the "Atlassian Marketplace Terms of Use" as a "Marketplace Product"
 * (http://www.atlassian.com/licensing/marketplace/termsofuse).
 *
 * See the LICENSE file for more details.
 */

import java.util.Map;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class Sample {

    public static void main(String[] a) {
        String data = "{\"total\":{\"sum\":7147,\"monthlies\":[ {\"month\":\"2013-07\",\"sum\":712},{\"month\":\"2013-08\",\"sum\":578} ],\"weeklies\":[ {\"week\":\"2013-W14\",\"sum\":186},{\"week\":\"2013-W17\",\"sum\":135} ]}}";
        JsonPath jsonPath = JsonPath.compile("$['total']['monthlies']");

        // JSONObject rawJsonObject = getJson(data, jsonPath);
        // System.out.println(rawJsonObject);
        // List<String> keyList = new ArrayList<String>(rawJsonObject.keySet());
        // for (String key : keyList) { // get a row for each key from the json object, columns based on field path selection
        // Object value = rawJsonObject.get(key);
        // JSONObject j = new JSONObject((Map) value);
        // System.out.println("key:" + key + ", value :" + value);
        // }
        //
        // jsonPath = JsonPath.compile("bbb{}");
        // String jsonStr = "{\"bbb{}\":\"ccc {}\"}";
        // // getJson(jsonStr, jsonPath);
        // System.out.println(jsonPath.read(jsonStr));
        //
        // String[] aStr = {"xx", "yy"};
        // String s = "";
        // for (String str1 : aStr) {
        // s = s + str1 + ",";
        // }
        // System.out.println(s);

        JSONArray array = getJsonArray(data, jsonPath);
        if (array != null) {
            for (Object element : array) {
                System.out.println(getJson(element));
            }
        }
        System.out.println(getString(jsonPath, false, false));
    }

    static public JSONObject getJson(final String data, final JsonPath jsonPath) {
        if (!data.isEmpty()) {
            try {
                Object jsonData = jsonPath.read(data);
                return getJson(jsonData);  // may be null
            } catch (NullPointerException exception) {
                exception.printStackTrace();
                return null;
            } catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }
        return null;
    }

    static public JSONArray getJsonArray(final String data, final JsonPath jsonPath) {
        if (!data.isEmpty()) {
            try {
                Object jsonData = jsonPath.read(data);
                return getJsonArray(jsonData);  // may be null
            } catch (NullPointerException exception) {
                // ignore an NPE that indicates a json path problem parsing the json for rare json data situations. This way user gets the rest of the data. It
                // is unlikely the user can do anything about it.
                // log.debug("Ignore NPE parsing data, likely data is not a json array. Data: " + data, exception);
            } catch (PathNotFoundException exception) {
                exception.printStackTrace();
                // throw new MacroExecutionException(getErrorMessage(exception));
            } catch (Exception exception) {
                exception.printStackTrace();
                // throw new MacroExecutionException(getErrorMessage(exception));
            }
        }
        return null;
    }

    static public JSONArray getJsonArray(final Object object) {
        return object instanceof JSONArray ? (JSONArray) object : null;
    }

    static public String deleteWhiteSpace() {
        return "bbb{}";
    }

    static protected String getString(final JsonPath path) {
        String pathString = StringUtils.replaceOnce(StringUtils.replaceOnce(path.getPath(), "$['", ""), "@.", "");
        // jsonpath 2.0.0 returns paths as $['total']['weeklies'], hence the below 2 lines added to remove [' and ]'
        pathString = StringUtils.replace(pathString, "['", ".");
        pathString = StringUtils.replace(pathString, "']", "");
        return pathString; // no regex
    }

    /**
     * Get the string value with modifications dictated by capitalize and stripAllQualifiers.
     * 
     * @param path
     * @param capitalize
     * @param stripAllQualifiers
     * @return modified string value of the paht
     */
    static protected String getString(final JsonPath path, boolean capitalize, boolean stripQualifiers) {
        // always remove at least the leading qualifiers
        String pathString = stripQualifiers ? StringUtils.substring(path.getPath(), path.getPath().lastIndexOf("[") + 1, path.getPath().lastIndexOf("]"))
                : getString(path);
        pathString = pathString.replace("'", "");
        return capitalize ? WordUtils.capitalize(pathString) : pathString;
    }

    static public JSONObject getJson(final Object object) {
        try {
            JSONObject json = new JSONObject(((Map) object));
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
