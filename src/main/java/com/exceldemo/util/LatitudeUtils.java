package com.exceldemo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSONObject;

public class LatitudeUtils {

	public static final String KEY_1 = "7d9fbeb43e975cd1e9477a7e5d5e192a";

	/**
	 * 返回输入地址的经纬度坐标 key lng(经度),lat(纬度)
	 * 
	 * @throws Exception
	 */
	public static Map<String, String> getGeocoderLatitude(String address) throws Exception {
		BufferedReader in = null;
		try {
			// 将地址转换成utf-8的16进制
			address = URLEncoder.encode(address, "UTF-8");
			URL tirc = new URL("http://api.map.baidu.com/geocoder?address=" + address + "&output=json&key=" + KEY_1);

			in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
			String res;
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			String str = sb.toString();
			System.out.println(str);
			Map<String, String> map = null;
			if (!ObjectUtils.isEmpty(str)) {
				JSONObject json = JSONObject.parseObject(str);
				if (json.getString("status").equalsIgnoreCase("OK")) {
					JSONObject result = json.getJSONObject("result");
					JSONObject location = result.getJSONObject("location");
					String lng = location.getString("lng");
					String lat = location.getString("lat");
					map = new HashMap<String, String>();
					map.put("lng", lng);
					map.put("lat", lat);
					return map;
				}
			}
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String args[]) {
		try {
			Map<String, String> json = LatitudeUtils.getGeocoderLatitude("浦东区张杨路1725号");
			System.out.println(json);
			System.out.println("lng : " + json.get("lng"));
			System.out.println("lat : " + json.get("lat"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
