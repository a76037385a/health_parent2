package com.app.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.app01.entity.Result;
import com.app01.msg.MessageConstant;
import com.app01.service.ReportService;
import com.app01.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    ReportService reportService;

    @GetMapping("/countUserByMonth")
    public Result countUserByMonth() {

        Map<String, List> map = new HashMap<>();
        List<String> monthList = new ArrayList<>();
        List<Integer> countMonthList = new ArrayList<>();


        //monthList //月份表
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);

        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            monthList.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()).toString());
        }
        map.put("months",monthList);
        
        //countMonthList //用户统计结果表
        for (String s : monthList) {
            String month = s + "-31";
            //countDao
            int monthCount =  reportService.countUserByMonth(s);
            countMonthList.add(monthCount);
        }
        map.put("count",countMonthList);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    @GetMapping("/getSetmealReport")
    public Result getSetmealReport() {

        Map<String, List> map = new HashMap<>();
        List<Map> countByPackageName = reportService.countByPackageName();

        //packgeNames  ["套餐1","套餐2","套餐3"]
        List<String> names = new ArrayList<>();
        for (Map map1 : countByPackageName) {
            String name = (String) map1.get("name");
            names.add(name);
        }
        map.put("names",names);
        //packgeCount  [ {"name":"套餐1","value":10}, {"name":"套餐1","value":10}]
        map.put("values",countByPackageName);

        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);

    }

    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            return reportService.getBusinessReportData();
        }catch (Exception e){
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @GetMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            //获得文件路径
            String reapPath = request.getSession().getServletContext().getRealPath("template") +File.separator +"report_template.xlsx";



            Map<String , Object> map = (Map<String, Object>) reportService.getBusinessReportData().getData();

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) map.get("reportDate");
            Integer todayNewMember = (Integer) map.get("todayNewMember");
            Integer totalMember = (Integer) map.get("totalMember");
            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");

            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(reapPath)));
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            System.out.println(hotSetmeal.toString());
            for (Map map1 : hotSetmeal) {

                String name = (String) map1.get("name");
                Long total = (Long) map1.get("total");
                String percent = (String) map1.get("percent");

                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(total);
                row.getCell(6).setCellValue(percent);
            }

            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            xssfWorkbook.write(out);

            out.flush();
            out.close();
            xssfWorkbook.close();

            return null;
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }


}
