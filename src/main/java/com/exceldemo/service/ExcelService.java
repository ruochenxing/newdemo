package com.exceldemo.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.exceldemo.entity.OrderDetail;

@Service
public class ExcelService {

    public void writeExcel(HttpServletResponse response, List<OrderDetail> details) throws IOException {
        // 创建Excel文件薄
        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            // 创建工作表sheeet
            HSSFSheet sheet = workbook.createSheet();
            // 创建第一行
            HSSFRow row = sheet.createRow(0);
            String[] title = { "商品", "商品数量(件)", "收货人", "手机", "区", "详细地址", "分机号", "团ID" };
            HSSFCell cell_title = null;
            for (int i = 0; i < title.length; i++) {
                cell_title = row.createCell(i);
                cell_title.setCellValue(title[i]);
            }
            int i = 0;
            // 追加数据
            for (OrderDetail detail : details) {
                HSSFRow nextrow = sheet.createRow(++i);
                HSSFCell cell_value = nextrow.createCell(0);
                cell_value.setCellValue(detail.getName());

                cell_value = nextrow.createCell(1);
                cell_value.setCellValue(detail.getNum());

                cell_value = nextrow.createCell(2);
                cell_value.setCellValue(detail.getPerson());

                cell_value = nextrow.createCell(3);
                cell_value.setCellValue(detail.getPhone());

                cell_value = nextrow.createCell(4);
                cell_value.setCellValue(detail.getArea());

                cell_value = nextrow.createCell(5);
                cell_value.setCellValue(detail.getAddress());

                cell_value = nextrow.createCell(6);
                cell_value.setCellValue(detail.getSubNumber());

                cell_value = nextrow.createCell(7);
                cell_value.setCellValue(detail.getTuanId());
            }
            // 创建一个文件
            try {
//                File file = new File("./export.xls");
//                file.createNewFile();
//                FileOutputStream stream = new FileOutputStream(file);
//                stream.close();
                response.setContentType("application/octet-stream");
                // 默认Excel名称
                response.setHeader("Content-Disposition", "attachment;fileName=" + "export.xls");
                response.flushBuffer();
                workbook.write(response.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public List<OrderDetail> parseExcel(String filePath, int sheetNum) throws Exception {
        List<OrderDetail> li = new ArrayList<>();
        // 创建Excel工作薄
        Workbook work = WorkbookFactory.create(new File(filePath));
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
//		Cell cell = null;
        sheet = work.getSheetAt(sheetNum);
        if (sheet == null) {
            return li;
        }
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            if (row == null || row.getFirstCellNum() == j) {
                continue;
            }
//				for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
//					cell = row.getCell(y);
//					li.add(cell);
//				}
            try {
                OrderDetail detail = new OrderDetail();
                detail.setName(row.getCell(0).getStringCellValue());
                detail.setNum(row.getCell(1).getNumericCellValue());
                detail.setPerson(row.getCell(2).getStringCellValue());
                detail.setPhone(row.getCell(3).getStringCellValue());
                detail.setArea(row.getCell(4).getStringCellValue());
                detail.setAddress(row.getCell(5).getStringCellValue());
                detail.setSubNumber(row.getCell(6).getStringCellValue());
                detail.setTuanId(row.getCell(7).getStringCellValue());
                li.add(detail);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        work.close();
        return li;
    }

    /**
     * 处理上传的文件
     *
     * @param in
     * @param fileName
     * @return
     * @throws Exception
     */
    public List<OrderDetail> getBankListByExcel(InputStream in, String fileName, int sheetNum) throws Exception {
        List<OrderDetail> li = new ArrayList<>();
        // 创建Excel工作薄
        Workbook work = this.getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
//		Cell cell = null;
        sheet = work.getSheetAt(sheetNum);
        if (sheet == null) {
            return li;
        }
        for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
            row = sheet.getRow(j);
            if (row == null || row.getFirstCellNum() == j) {
                continue;
            }
            List<String> errorFields = new ArrayList<>();
            OrderDetail detail = new OrderDetail();
            try {
                detail.setName(row.getCell(0).getStringCellValue());
            } catch (Exception e) {
                errorFields.add("商品");
            }
            try {
                detail.setNum(row.getCell(1).getNumericCellValue());
            } catch (Exception e) {
                errorFields.add("商品数量");
            }
            try {
                detail.setPerson(row.getCell(2).getStringCellValue());
            } catch (Exception e) {
                errorFields.add("收货人");
            }
            try {
                detail.setPhone(row.getCell(3).getStringCellValue());
            } catch (Exception e) {
                errorFields.add("手机");
            }
            try {
                detail.setArea(row.getCell(4).getStringCellValue());
            } catch (Exception e) {
                errorFields.add("区");
            }
            try {
                detail.setAddress(row.getCell(5).getStringCellValue());
            } catch (Exception e) {
                errorFields.add("详细地址");
            }
            try {
                detail.setSubNumber(row.getCell(6).getStringCellValue());
            } catch (Exception e) {
                errorFields.add("分机号");
            }
            try {
                detail.setTuanId(row.getCell(7).getStringCellValue());
            } catch (Exception e) {
                errorFields.add("团ID");
            }
            li.add(detail);
            if (!errorFields.isEmpty()) {
                System.out.println("================parse excel error, " + (j + 1) + ", fields= "
                        + JSONObject.toJSONString(errorFields));
            }
            if (errorFields.size() > 7) {
                break;
            }
        }
        work.close();
        System.out.println("parse excel count = " + li.size());
        return li;
    }

    /**
     * 判断文件格式
     *
     * @param inStr
     * @param fileName
     * @return
     * @throws Exception
     */
    public Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook workbook = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (".xls".equals(fileType)) {
            workbook = new HSSFWorkbook(inStr);
        } else if (".xlsx".equals(fileType)) {
            workbook = new XSSFWorkbook(inStr);
        } else {
            throw new Exception("请上传excel文件！");
        }
        return workbook;
    }

}
