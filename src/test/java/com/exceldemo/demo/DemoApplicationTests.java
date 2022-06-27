package com.exceldemo.demo;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSONObject;
import com.exceldemo.Contants;
import com.exceldemo.entity.OrderDetail;
import com.exceldemo.service.ExcelService;
import com.exceldemo.service.OrderService;
import com.exceldemo.util.AMapUtil;
import com.exceldemo.util.BaiduUtil;
import com.exceldemo.util.FileUtil;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private ExcelService excelService;
    @Autowired
    private OrderService orderService;

    public static String regex = "(?<city>[^市]+市)?(?<area>.*?区)(?<street>[^路]+路)?(?<nong>[^弄]+弄|[^号楼]+号楼|[^号]+号)?(?<hao>[^号楼]+号楼)?(?<roomnumber>.*)";

//    @Test
    public void testExcel4() {
        Map<String, List<OrderDetail>> keyOrderDetails = new HashMap<>();
        List<OrderDetail> details;
        try {
            details = excelService.parseExcel("./11.xlsx", 0);
            System.out.println("-----" + details.size());
            for (OrderDetail detail : details) {
                try {
                    Matcher m = Pattern.compile(regex).matcher(detail.getRightAddress());
                    Map<String, String> map = new HashMap<>(16);
                    String city = "";
                    String area = "";
                    String street = "";
                    String nong = "";
                    String hao = "";
                    String roomnumber = "";
                    while (m.find()) {
                        city = m.group("city");
                        map.put("city", city == null ? "" : city.trim());
                        area = m.group("area");
                        map.put("area", area == null ? "" : area.trim());
                        street = m.group("street");
                        map.put("street", street == null ? "" : street.trim());
                        nong = m.group("nong");
                        map.put("nong", nong == null ? "" : nong.trim());
                        hao = m.group("hao");
                        map.put("hao", hao == null ? "" : hao.trim());
                        roomnumber = m.group("roomnumber");
                        map.put("roomnumber", roomnumber == null ? "" : roomnumber.trim());
                    }
                    detail.setKey(map.get("city") + map.get("area") + map.get("street") + map.get("nong"));// +
                                                                                                           // map.get("hao")
//                  System.out.println(detail.getRightAddress() + "\t" + map.get("area") + "\t" + map.get("street")
//                          + "\t" + map.get("nong") + "\t" + map.get("hao") + "\t" + map.get("roomnumber") + "\t"
//                          + detail.getKey());
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            List<OrderDetail> newDetails = new ArrayList<>();
            for (Entry<String, List<OrderDetail>> entry : keyOrderDetails.entrySet()) {
                System.out.println(entry.getKey() + "\t" + entry.getValue().size());
                try {
                    Map<String, String> result = BaiduUtil.getPoint(entry.getKey());
                    if (result == null || result.isEmpty()) {
                        System.out.println(entry.getKey() + "\t BaiduUtil.getPoint null");
                        break;
                    }
                    for (OrderDetail detail : entry.getValue()) {
                        detail.setLng(result.get("lng"));
                        detail.setLat(result.get("lat"));
                        System.out.println(JSONObject.toJSONString(detail));
                        newDetails.add(detail);
                    }
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            FileUtil.saveFile(JSONObject.toJSONString(newDetails), "./baidu_details.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void testExcel3() {
        Map<String, List<OrderDetail>> keyOrderDetails = new HashMap<>();
        List<OrderDetail> details;
        try {
            details = excelService.parseExcel("./11.xlsx", 0);
            System.out.println("-----" + details.size());
            for (OrderDetail detail : details) {
                try {
                    Matcher m = Pattern.compile(regex).matcher(detail.getRightAddress());
                    Map<String, String> map = new HashMap<>(16);
                    String city = "";
                    String area = "";
                    String street = "";
                    String nong = "";
                    String hao = "";
                    String roomnumber = "";
                    while (m.find()) {
                        city = m.group("city");
                        map.put("city", city == null ? "" : city.trim());
                        area = m.group("area");
                        map.put("area", area == null ? "" : area.trim());
                        street = m.group("street");
                        map.put("street", street == null ? "" : street.trim());
                        nong = m.group("nong");
                        map.put("nong", nong == null ? "" : nong.trim());
                        hao = m.group("hao");
                        map.put("hao", hao == null ? "" : hao.trim());
                        roomnumber = m.group("roomnumber");
                        map.put("roomnumber", roomnumber == null ? "" : roomnumber.trim());
                    }
                    detail.setKey(map.get("city") + map.get("area") + map.get("street") + map.get("nong"));// +
                                                                                                           // map.get("hao")
//                  System.out.println(detail.getRightAddress() + "\t" + map.get("area") + "\t" + map.get("street")
//                          + "\t" + map.get("nong") + "\t" + map.get("hao") + "\t" + map.get("roomnumber") + "\t"
//                          + detail.getKey());
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (Entry<String, List<OrderDetail>> entry : keyOrderDetails.entrySet()) {
                System.out.println(entry.getKey() + "\t" + entry.getValue().size());
                try {
                    Map<String, String> result = AMapUtil.getGeocoderLatitude(entry.getKey());
                    if (result == null || result.isEmpty()) {
                        System.out.println(entry.getKey() + "\t getGeocoderLatitude null");
                        return;
                    }
                    for (OrderDetail detail : entry.getValue()) {
                        detail.setLng(result.get("lng"));
                        detail.setLat(result.get("lat"));
                    }

                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            FileUtil.saveFile(JSONObject.toJSONString(details), "./details.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Test
    public void testExcel2() {
        Map<String, List<OrderDetail>> keyOrderDetails = new HashMap<>();
        List<OrderDetail> details;
        try {
            details = excelService.parseExcel("./11.xlsx", 0);
            System.out.println("-----" + details.size());
            for (OrderDetail detail : details) {
                try {
                    Matcher m = Pattern.compile(regex).matcher(detail.getRightAddress());
                    Map<String, String> map = new HashMap<>(16);
                    String city = "";
                    String area = "";
                    String street = "";
                    String nong = "";
                    String hao = "";
                    String roomnumber = "";
                    while (m.find()) {
                        city = m.group("city");
                        map.put("city", city == null ? "" : city.trim());
                        area = m.group("area");
                        map.put("area", area == null ? "" : area.trim());
                        street = m.group("street");
                        map.put("street", street == null ? "" : street.trim());
                        nong = m.group("nong");
                        map.put("nong", nong == null ? "" : nong.trim());
                        hao = m.group("hao");
                        map.put("hao", hao == null ? "" : hao.trim());
                        roomnumber = m.group("roomnumber");
                        map.put("roomnumber", roomnumber == null ? "" : roomnumber.trim());
                    }
                    detail.setKey(map.get("city") + map.get("area") + map.get("street") + map.get("nong"));// +
                                                                                                           // map.get("hao")
//					System.out.println(detail.getRightAddress() + "\t" + map.get("area") + "\t" + map.get("street")
//							+ "\t" + map.get("nong") + "\t" + map.get("hao") + "\t" + map.get("roomnumber") + "\t"
//							+ detail.getKey());
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//			FileUtil.saveFile(JSONObject.toJSONString(keyOrderDetails), "./result1.txt");
            for (Entry<String, List<OrderDetail>> entry : keyOrderDetails.entrySet()) {
                System.out.println(entry.getKey() + "\t" + entry.getValue().size());
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @Test
    public void testExcel1() {
        List<OrderDetail> details;
        try {
            details = excelService.parseExcel("/Users/ruochenxing/Desktop/11.xlsx", 0);
            System.out.println("-----" + details.size());
            for (OrderDetail detail : details) {
                try {
                    Map<String, String> result = AMapUtil.getGeocoderLatitude(detail.getRightAddress());
                    detail.setLng(result.get("lng"));
                    detail.setLat(result.get("lat"));
                    System.out.println(
                            detail.getRightAddress() + "\t lng = " + detail.getLng() + "\t lat = " + detail.getLat());
                    Thread.sleep(500);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @Test
    public void testLaglat() {
        String address = "上海市普陀区祁顺路1388弄17-301";
        Map<String, String> result = AMapUtil.getGeocoderLatitude(address);
        System.out.println(JSONObject.toJSONString(result));
    }

//    @Test
    public void testExcel() {
        List<OrderDetail> details;
        try {
            details = excelService.parseExcel("/Users/winter.zou/Documents/eclipse/workspace/demo/data.xlsx", 0);
            System.out.println("-----" + details.size());
//            for (OrderDetail detail : details) {
//                System.out.println(detail.getRightAddress());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @Test
    public void testCache() {
        Map<String, String> cacheLngLat = orderService.readCache("./details.txt");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : cacheLngLat.entrySet()) {
            sb.append(entry.getKey() + Contants.KEY_LANLAT_SPLIT + entry.getValue() + "\r\n");
        }
        FileUtil.saveFile(sb.toString(), Contants.KEY_LNGLAT_CACHE_FILE);
    }

//    @Test
    public void testCache1() throws IOException {
        Map<String, String> result = orderService.readLngLatCache(Contants.KEY_LNGLAT_CACHE_FILE);
        System.out.println(JSONObject.toJSONString(result));
    }

//    @Test
    public void testAppend() throws IOException {
        String str = "上海市静安区宝通路239弄" + Contants.KEY_LANLAT_SPLIT + "121.478755,31.256700";
        FileUtil.appendFile(str, Contants.KEY_LNGLAT_CACHE_FILE);
    }

    /**
     * 如果已打成jar包，则返回jar包所在目录 如果未打成jar，则返回target所在目录
     */
    @Test
    public String getRootPath() throws UnsupportedEncodingException {
        // 项目的编译文件的根目录
        String path = URLDecoder.decode(this.getClass().getResource("/").getPath(),
                String.valueOf(StandardCharsets.UTF_8));
        if (path.startsWith("file:")) {
            int i = path.indexOf(".jar!");
            path = path.substring(0, i);
            path = path.replaceFirst("file:", "");
        }
        // 项目所在的目录
        String result = new File(path).getParentFile().getAbsolutePath();
        return result;
    }

}
