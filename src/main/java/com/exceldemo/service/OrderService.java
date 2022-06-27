package com.exceldemo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exceldemo.Contants;
import com.exceldemo.Parser;
import com.exceldemo.entity.OrderDetail;
import com.exceldemo.util.AMapUtil;
import com.exceldemo.util.FileUtil;

@Service
public class OrderService {

    public void fillKey(List<OrderDetail> details) {
        for (OrderDetail detail : details) {
            try {

                detail.setKey(Parser.parseKey(detail.getRightAddress()));
//              System.out.println(detail.getRightAddress() + "\t" + map.get("area") + "\t" + map.get("street")
//                      + "\t" + map.get("nong") + "\t" + map.get("hao") + "\t" + map.get("roomnumber") + "\t"
//                      + detail.getKey());
            } catch (Exception e) {
                System.out.println("fillKey error " + JSONObject.toJSONString(detail));
                continue;
            }
        }
    }

    public void fillLngLat(List<OrderDetail> details, boolean useCache) throws IOException {
        Map<String, List<OrderDetail>> keyOrderDetails = new HashMap<>();
        for (OrderDetail detail : details) {
            if (keyOrderDetails.containsKey(detail.getKey())) {
                List<OrderDetail> list = keyOrderDetails.get(detail.getKey());
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(detail);
            } else {
                List<OrderDetail> list = new ArrayList<>();
                list.add(detail);
                keyOrderDetails.put(detail.getKey(), list);
            }
        }
        int count = 0;
        Map<String, String> cacheLngLat = readLngLatCache(Contants.KEY_LNGLAT_CACHE_FILE);
        for (Entry<String, List<OrderDetail>> entry : keyOrderDetails.entrySet()) {
            try {
                if (StringUtils.isEmpty(entry.getKey())) {
                    System.out.println("key is null," + JSONObject.toJSONString(entry.getValue()));
                    continue;
                }
                String result = cacheLngLat.get(entry.getKey());
                if (result != null && !result.isEmpty()) {
                    for (OrderDetail detail : entry.getValue()) {
                        detail.setLng(result.split(",")[0]);
                        detail.setLat(result.split(",")[1]);
                    }
                    continue;
                } else {
                    Map<String, String> map = AMapUtil.getGeocoderLatitude(entry.getKey());
                    count++;
                    if (map == null || map.isEmpty()) {
                        System.out.println(entry.getKey() + "\t getGeocoderLatitude null");
                        continue;
                    }
                    count++;
                    for (OrderDetail detail : entry.getValue()) {
                        detail.setLng(map.get("lng"));
                        detail.setLat(map.get("lat"));
                    }
                    Thread.sleep(100);

                    try {
                        String content = entry.getKey() + Contants.KEY_LANLAT_SPLIT + map.get("lng") + ","
                                + map.get("lat");
                        FileUtil.appendFile(content, Contants.KEY_LNGLAT_CACHE_FILE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("update key_lnglat_cache file , " + count);
        }
        List<OrderDetail> newDetails = details.stream()
                .filter(s -> (!StringUtils.isEmpty(s.getLng()) && !StringUtils.isEmpty(s.getLat())))
                .collect(Collectors.toList());
        System.out.println("oldDetails size = " + details.size());
        System.out.println("newDetails size = " + newDetails.size());
    }

    public Map<String, String> readCache(String fileName) {
        Map<String, String> result = new HashMap<>();
        String jsonTxt = FileUtil.getFileText(fileName, true);
        JSONArray arr = JSONObject.parseArray(jsonTxt);
        for (int i = 0; i < arr.size(); i++) {
            OrderDetail detail = arr.getObject(i, OrderDetail.class);
            result.put(detail.getKey(), detail.getLng() + "," + detail.getLat());
        }
        return result;
    }

    public Map<String, String> readLngLatCache(String fileName) throws IOException {
        List<String> contents = FileUtil.readFileLines(fileName);
        Map<String, String> result = new HashMap<>();
        for (String content : contents) {
            String[] arr = content.split(Contants.KEY_LANLAT_SPLIT);
            result.put(arr[0].trim(), arr[1].trim());
        }
        return result;
    }

}
