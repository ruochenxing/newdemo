package com.exceldemo.web;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.exceldemo.Contants;
import com.exceldemo.Parser;
import com.exceldemo.entity.OrderDetail;
import com.exceldemo.service.ExcelService;
import com.exceldemo.service.OrderService;
import com.exceldemo.util.FileUtil;

@Controller
public class UploadController {

    @Autowired
    private ExcelService excelService;
    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/upload")
    @ResponseBody
    public String uploadExcel(HttpServletRequest request) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("filename");
        if (file.isEmpty()) {
            return "文件不能为空";
        }
        String now = format.format(new Date());
        InputStream inputStream = file.getInputStream();
        List<OrderDetail> list = new ArrayList<>();
        try {
            list = excelService.getBankListByExcel(inputStream, file.getOriginalFilename(), 0);
        } catch (Exception e) {
            System.out.println("getBankListByExcel\t " + e.getMessage());
        }
        inputStream.close();
        try {
            FileUtil.saveFile(JSONObject.toJSONString(list), Parser.generateFilePath("details-origins" + now + ".txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("end save origin data details-origins.txt");
        orderService.fillKey(list);
        System.out.println("begin fill lng lat");
        orderService.fillLngLat(list, false);
        System.out.println("begin save details.txt");
        try {
            FileUtil.saveFile(JSONObject.toJSONString(list), Parser.generateFilePath("details" + now + ".txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Contants.DETAILS_MAP_CACHE.put(Parser.generateFilePath("details" + now + ".txt"), list);
        return Parser.generateFilePath("details" + now + ".txt");
    }

}
