package com.exceldemo.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class AMapUtil {
    public static Map<String, String> getGeocoderLatitude(String address) {
        String getUrl = "http://restapi.amap.com/v3/geocode/geo?key=389880a06e3f893ea46036f030c94700&s=rsv3&city=35"
                + "&address=" + address;
        Map<String, String> map = null;
        try {
            URL url = new URL(getUrl); // 把字符串转换为URL请求地址
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
            connection.connect();// 连接会话
            // 获取输入流
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {// 循环读取流
                sb.append(line);
            }
            br.close();// 关闭流
            connection.disconnect();// 断开连接
            String str = sb.toString();
            if (!ObjectUtils.isEmpty(str)) {
                JSONObject json = JSONObject.parseObject(str);
                if (json.getString("status").equalsIgnoreCase("1")) {
                    JSONArray geocodes = json.getJSONArray("geocodes");
                    String location = geocodes.getJSONObject(0).getString("location");
                    String lng = location.split(",")[0];
                    String lat = location.split(",")[1];
                    map = new HashMap<String, String>();
                    map.put("lng", lng);
                    map.put("lat", lat);
                    return map;
                }
            }
            System.out.println("Get==" + sb.toString() + "\t " + address);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("失败!");
        }
        return map;
    }

//    public static final String GET_URL = "http://restapi.amap.com/v3/geocode/geo?key=389880a06e3f893ea46036f030c94700&s=rsv3&city=35"
//            + "&address=山东青岛李沧区李村社区";
//    public static final String POST_URL = "http://restapi.amap.com/v3/geocode/geo?key=389880a06e3f893ea46036f030c94700&s=rsv3&city=35"
//            + "&address=山东青岛李沧区李村社区";

//
//	/**
//	 * 接口调用 GET
//	 */
//	public static void httpURLConectionGET() {
//		try {
//			URL url = new URL(GET_URL); // 把字符串转换为URL请求地址
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 打开连接
//			connection.connect();// 连接会话
//			// 获取输入流
//			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//			String line;
//			StringBuilder sb = new StringBuilder();
//			while ((line = br.readLine()) != null) {// 循环读取流
//				sb.append(line);
//			}
//			br.close();// 关闭流
//			connection.disconnect();// 断开连接
//			System.out.println("Get==" + sb.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("失败!");
//		}
//	}
//
//	/**
//	 * 
//	 * 接口调用 POST
//	 * 
//	 */
//	public static void httpURLConnectionPOST() {
//		try {
//			URL url = new URL(POST_URL);
//			// 将url 以 open方法返回的urlConnection 连接强转为HttpURLConnection连接 (标识一个url所引用的远程对象连接)
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();// 此时cnnection只是为一个连接对象,待连接中
//			// 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
//			connection.setDoOutput(true);
//			// 设置连接输入流为true
//			connection.setDoInput(true);
//			// 设置请求方式为post
//			connection.setRequestMethod("POST");
//			// post请求缓存设为false
//			connection.setUseCaches(false);
//			// 设置该HttpURLConnection实例是否自动执行重定向
//			connection.setInstanceFollowRedirects(true);
//			// 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
//			// application/x-javascript text/xml->xml数据
//			// application/x-javascript->json对象
//			// application/x-www-form-urlencoded->表单数据
//			// 这是重点^_^
//			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//			// 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
//			connection.connect();
//			// 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
//			DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
//			String parm = "storeId=" + URLEncoder.encode("32", "utf-8"); // URLEncoder.encode()方法 为字符串进行编码
//			// 将参数输出到连接
//			dataout.writeBytes(parm);
//			// 输出完成后刷新并关闭流
//			dataout.flush();
//			dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)
//			System.out.println(connection.getResponseCode());
//			// 连接发起请求,处理服务器响应 (从连接获取到输入流并包装为bufferedReader)
//			BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//			String line;
//			StringBuilder sb = new StringBuilder(); // 用来存储响应数据
//			// 循环读取流,若不到结尾处
//			while ((line = bf.readLine()) != null) {
//				sb.append(bf.readLine());
//			}
//			bf.close(); // 重要且易忽略步骤 (关闭流,切记!)
//			connection.disconnect(); // 销毁连接
//			System.out.println("Post==" + sb.toString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public static void main(String[] args) {
//		httpURLConectionGET();
//		httpURLConnectionPOST();
//	}

}