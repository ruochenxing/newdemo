package com.exceldemo;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static String regex = "(?<city>[^市]+市)?(?<area>.*?区)(?<street>[^路]+路)?(?<nong>[^弄]+弄|[^号楼]+号楼|[^号]+号)?(?<hao>[^号楼]+号楼)?(?<roomnumber>.*)";

    public static String generateFilePath(String fileName) {
        return Contants.BASE_DIR + fileName;
    }

    public static String parseRightAddress(String area, String address) {
        try {
            return "上海市" + area + address.split("【|】|\\[|\\]")[2];
        } catch (Exception e) {
            return address;
        }
    }

    public static String parseKey(String rightAddress) {
        Matcher m = Pattern.compile(regex).matcher(rightAddress);
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
        return map.get("city") + map.get("area") + map.get("street") + map.get("nong"); // + map.get("hao")
    }
}
