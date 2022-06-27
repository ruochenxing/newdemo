package com.exceldemo.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exceldemo.Contants;
import com.exceldemo.entity.OrderDetail;
import com.exceldemo.service.ExcelService;
import com.exceldemo.service.OrderService;
import com.exceldemo.util.FileUtil;

@Controller
public class DataController {

    @Autowired
    private ExcelService excelService;
    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String data = request.getParameter("data");
        if (ObjectUtils.isEmpty(data)) {
            return;
        }
        List<OrderDetail> list = new ArrayList<>();
        JSONArray arr = JSONObject.parseArray(data);
        for (int i = 0; i < arr.size(); i++) {
            OrderDetail detail = arr.getObject(i, OrderDetail.class);
            list.add(detail);
        }
        excelService.writeExcel(response, list);
    }

    @RequestMapping("/getData1")
    @ResponseBody
    public String getData1(String filename, int currentPage) {
        JSONObject result = new JSONObject();
        result.put("filename", filename);
        result.put("currentPage", currentPage);
        return JSONObject.toJSONString(result);
    }
    
    @RequestMapping("/getData")
    @ResponseBody
    public String getData(String filename, int currentPage) {
        if (ObjectUtils.isEmpty(filename) || filename.contains("error") || filename.equalsIgnoreCase("details.txt")) {
            return "";
        }
        System.out.println("filename = " + filename);
        System.out.println("currentPage = " + currentPage);
        List<OrderDetail> cache = Contants.DETAILS_MAP_CACHE.get(filename);
        List<Map<String, Object>> data = new ArrayList<>();
        if (cache != null && !cache.isEmpty()) {
            try {
                orderService.fillLngLat(cache, false);
            } catch (IOException e1) {
                e1.printStackTrace();
                return e1.getMessage();
            }
            Map<String, List<OrderDetail>> keyOrderDetails = new HashMap<>();
            for (int i = 0; i < cache.size(); i++) {
                OrderDetail detail = cache.get(i);
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
            for (Entry<String, List<OrderDetail>> entry : keyOrderDetails.entrySet()) {
                Map<String, Object> map = new HashMap<>();
                try {
                    map.put("key", entry.getKey().split("区")[1]);
                } catch (Exception e) {
                    System.out.println(entry.getKey() + " parse error");
                    continue;
                }
                map.put("list", entry.getValue());
                map.put("size", entry.getValue().size());
                data.add(map);
            }
        } else {
            String jsonTxt = FileUtil.getFileText(filename, true);
            JSONArray arr = JSONObject.parseArray(jsonTxt);
            List<OrderDetail> list = new ArrayList<>();
            for (int i = 0; i < arr.size(); i++) {
                list.add(arr.getObject(i, OrderDetail.class));
            }
            try {
                orderService.fillLngLat(list, false);
            } catch (IOException e1) {
                e1.printStackTrace();
                return e1.getMessage();
            }
            Map<String, List<OrderDetail>> keyOrderDetails = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                OrderDetail detail = list.get(i);
                if (keyOrderDetails.containsKey(detail.getKey())) {
                    List<OrderDetail> tmp = keyOrderDetails.get(detail.getKey());
                    if (tmp == null) {
                        tmp = new ArrayList<>();
                    }
                    tmp.add(detail);
                } else {
                    List<OrderDetail> tmp = new ArrayList<>();
                    tmp.add(detail);
                    keyOrderDetails.put(detail.getKey(), tmp);
                }
            }
            for (Entry<String, List<OrderDetail>> entry : keyOrderDetails.entrySet()) {
                Map<String, Object> map = new HashMap<>();
                try {
                    map.put("key", entry.getKey().split("区")[1]);
                } catch (Exception e) {
                    System.out.println(entry.getKey() + " parse error");
                    continue;
                }
                map.put("list", entry.getValue());
                map.put("size", entry.getValue().size());
                data.add(map);
            }
        }
        int pageSize = 250;
        int maxPage = (data.size() / pageSize) + 1;
        if (currentPage > maxPage) {
            currentPage = maxPage;
        }
        int start = (currentPage - 1) * pageSize;
        int end = currentPage * pageSize;
        if (start > data.size()) {
            start = 0;
        }
        if (end > data.size()) {
            end = data.size();
        }
//        List<Map<String, Object>> tmp = data.subList(start, end);
        return JSONObject.toJSONString(data);
    }
}