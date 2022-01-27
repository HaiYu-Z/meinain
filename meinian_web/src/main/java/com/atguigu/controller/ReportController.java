package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.MemberService;
import com.atguigu.service.ReportService;
import com.atguigu.service.SetmealService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author HaiYu
 * @date 2022/1/25 20:18
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    MemberService memberService;
    @Reference
    SetmealService setmealService;
    @Reference
    ReportService reportService;

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        try {
            List<String> setmealNames = new ArrayList<>();
            List<Map> setmealCount = setmealService.getSetmealReport();

            for (Map map : setmealCount) {
                setmealNames.add((String) map.get("name"));
            }

            Map map = new HashMap();
            map.put("setmealNames", setmealNames);
            map.put("setmealCount", setmealCount);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {
        try {
            List<String> months = new ArrayList<>();
            List<Integer> memberCount = null;

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -12);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH, 1);
                String month = sdf.format(calendar.getTime());
                months.add(month);
            }

            memberCount = memberService.findMemberCountByMonth(months);

            Map map = new HashMap<>();
            map.put("months", months);
            map.put("memberCount", memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }


    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream out = null;
        Workbook workbook = null;
        try {
            // 1.拿数据
            Map result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            // 2.获取模板文件
            String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            // 3.构建工作簿
            workbook = new XSSFWorkbook(new File(filePath));
            Sheet sheet = workbook.getSheetAt(0);

            // 4.写数据
            // 日期
            sheet.getRow(1).getCell(1).setCellValue(reportDate);

            // 会员数据统计
            Row row = sheet.getRow(3);
            row.getCell(1).setCellValue(todayNewMember);
            row.getCell(3).setCellValue(totalMember);
            row = sheet.getRow(4);
            row.getCell(1).setCellValue(thisWeekNewMember);
            row.getCell(3).setCellValue(thisMonthNewMember);

            // 预约出游数据统计
            row = sheet.getRow(6);
            row.getCell(1).setCellValue(todayOrderNumber);
            row.getCell(3).setCellValue(todayVisitsNumber);
            row = sheet.getRow(7);
            row.getCell(1).setCellValue(thisWeekOrderNumber);
            row.getCell(3).setCellValue(thisWeekVisitsNumber);
            row = sheet.getRow(8);
            row.getCell(1).setCellValue(thisMonthOrderNumber);
            row.getCell(3).setCellValue(thisMonthVisitsNumber);

            // 热门套餐
            int rowNum = 11;
            for(Map map : hotSetmeal){
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                // 套餐名称
                row.getCell(0).setCellValue(name);
                // 预约数量
                row.getCell(1).setCellValue(setmeal_count);
                // 占比
                row.getCell(2).setCellValue(proportion.doubleValue());
            }

            // 5.输出文件
            //通过输出流进行文件下载
            out = response.getOutputStream();
            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)
            response.setHeader("content-Disposition", "attachment;filename=" + "运营数据统计_" + reportDate + ".xlsx");
            workbook.write(out);

        } catch (Exception e) {
            e.printStackTrace();
            // 跳转错误页面
            try {
                request.getRequestDispatcher("/error/downloadError.html").forward(request,response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            // 6.关闭
            try {
                assert out != null;
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
