package com.springtools.excel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeDemo {

    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @ExcelProperty(value = "性别", index = 1)
    private String gender;

    @ExcelProperty(value = "部门", index = 2)
    private String department;

    @ExcelProperty(value = "职位", index = 3)
    private String position;

    @ExcelProperty(value = "学历", index = 4)
    private String education;

    @ExcelProperty(value = "手机号", index = 5)
    private String phone;

    @ExcelProperty(value = "邮箱", index = 6)
    private String email;

    @ExcelProperty(value = "入职日期", index = 7)
    private LocalDate hireDate;

    @ExcelProperty(value = "工资", index = 8)
    private BigDecimal salary;
}
