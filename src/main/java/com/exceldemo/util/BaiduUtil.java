package com.exceldemo.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

//mvn exec:java -Dexec.mainClass="com.echarts.BaiduUtil"
public class BaiduUtil {

    public static void main(String[] args) {
        getPoint("上海市徐汇区龙瑞路128弄");
    }

    public static Map<String, String> getPoint(String address) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            java.io.InputStream l_urlStream;
            java.net.URL l_url = new java.net.URL(
                    "http://api.map.baidu.com/geocoder/v2/?address=" + address.replaceAll(" ", "")
                            + "&output=json&ak=4ea360dba9ba4afa0b90cbe9ee9483a7&callback=showLocation");
            java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url.openConnection();
            l_connection.connect();
            l_urlStream = l_connection.getInputStream();
            java.io.BufferedReader l_reader = new java.io.BufferedReader(new java.io.InputStreamReader(l_urlStream));
            String str = l_reader.readLine();

            JSONObject json = JSONObject.parseObject(str.replace("showLocation&&showLocation(", "").replace(")", ""));
            if (json.getIntValue("status") == 0) {
                JSONObject result = json.getJSONObject("result");
                JSONObject location = result.getJSONObject("location");
                String lng = location.getString("lng");
                String lat = location.getString("lat");
                map.put("lng", lng);
                map.put("lat", lat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}