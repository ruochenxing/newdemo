package com.exceldemo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.exceldemo.entity.OrderDetail;

public class Contants {

//    public static final String BASE_DIR = "D:\\邹冬\\order\\";

    public static final String BASE_DIR = "./";

    public static final String KEY_LNGLAT_CACHE_FILE = BASE_DIR + "key_lnglat_cache.txt";

    public static final String KEY_LANLAT_SPLIT = "#####";

    public static Map<String, List<OrderDetail>> DETAILS_MAP_CACHE = new ConcurrentHashMap<>();
}
