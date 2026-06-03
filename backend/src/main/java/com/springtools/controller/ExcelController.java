package com.springtools.controller;

import com.alibaba.excel.EasyExcel;
import com.springtools.excel.handler.DropdownWriteHandler;
import com.springtools.excel.listener.ExcelListener;
import com.springtools.excel.template.EmployeeDemo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/excel")
public class ExcelController {

    /**
     * 下载导入模板（带下拉选项）
     */
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        setResponseHeader(response, "员工信息导入模板.xlsx");

        Map<Integer, List<String>> dropdownMap = buildDropdownMap();

        EasyExcel.write(response.getOutputStream(), EmployeeDemo.class)
                .registerWriteHandler(new DropdownWriteHandler(dropdownMap))
                .sheet("员工信息")
                .doWrite(Collections.emptyList());
    }

    /**
     * 导出数据（带下拉选项，返回示例数据）
     */
    @GetMapping("/export")
    public void exportData(HttpServletResponse response) throws IOException {
        setResponseHeader(response, "员工信息导出.xlsx");

        Map<Integer, List<String>> dropdownMap = buildDropdownMap();
        List<EmployeeDemo> dataList = buildSampleData();

        EasyExcel.write(response.getOutputStream(), EmployeeDemo.class)
                .registerWriteHandler(new DropdownWriteHandler(dropdownMap))
                .sheet("员工信息")
                .doWrite(dataList);
    }

    /**
     * 导入 Excel 文件
     */
    @PostMapping("/import")
    public Map<String, Object> importExcel(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            ExcelListener<EmployeeDemo> listener = new ExcelListener<>();
            EasyExcel.read(file.getInputStream(), EmployeeDemo.class, listener).sheet().doRead();

            result.put("code", 200);
            result.put("message", "导入成功");

            Map<String, Object> data = new HashMap<>();
            data.put("total", listener.getTotal());
            data.put("success", listener.getSuccess());
            data.put("fail", listener.getFail());
            data.put("failRows", listener.getFailRows());
            data.put("list", listener.getDataList());
            result.put("data", data);
        } catch (IOException e) {
            log.error("导入 Excel 失败", e);
            result.put("code", 500);
            result.put("message", "导入失败: " + e.getMessage());
        }
        return result;
    }

    private Map<Integer, List<String>> buildDropdownMap() {
        Map<Integer, List<String>> dropdownMap = new HashMap<>();
        dropdownMap.put(1, Arrays.asList("男", "女"));
        dropdownMap.put(2, Arrays.asList("技术部", "产品部", "市场部", "人事部", "财务部"));
        dropdownMap.put(3, Arrays.asList("初级工程师", "中级工程师", "高级工程师", "经理", "总监"));
        dropdownMap.put(4, Arrays.asList("大专", "本科", "硕士", "博士"));
        return dropdownMap;
    }

    private List<EmployeeDemo> buildSampleData() {
        List<EmployeeDemo> list = new ArrayList<>();

        EmployeeDemo emp1 = new EmployeeDemo();
        emp1.setName("张三");
        emp1.setGender("男");
        emp1.setDepartment("技术部");
        emp1.setPosition("高级工程师");
        emp1.setEducation("本科");
        emp1.setPhone("13800138001");
        emp1.setEmail("zhangsan@example.com");
        emp1.setHireDate(LocalDate.of(2023, 3, 15));
        emp1.setSalary(new BigDecimal("15000.00"));
        list.add(emp1);

        EmployeeDemo emp2 = new EmployeeDemo();
        emp2.setName("李四");
        emp2.setGender("女");
        emp2.setDepartment("产品部");
        emp2.setPosition("经理");
        emp2.setEducation("硕士");
        emp2.setPhone("13800138002");
        emp2.setEmail("lisi@example.com");
        emp2.setHireDate(LocalDate.of(2022, 7, 1));
        emp2.setSalary(new BigDecimal("20000.00"));
        list.add(emp2);

        EmployeeDemo emp3 = new EmployeeDemo();
        emp3.setName("王五");
        emp3.setGender("男");
        emp3.setDepartment("市场部");
        emp3.setPosition("中级工程师");
        emp3.setEducation("本科");
        emp3.setPhone("13800138003");
        emp3.setEmail("wangwu@example.com");
        emp3.setHireDate(LocalDate.of(2024, 1, 10));
        emp3.setSalary(new BigDecimal("12000.00"));
        list.add(emp3);

        return list;
    }

    private void setResponseHeader(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }
}
