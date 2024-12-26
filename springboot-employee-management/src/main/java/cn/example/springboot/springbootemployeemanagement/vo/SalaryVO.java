package cn.example.springboot.springbootemployeemanagement.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SalaryVO {
    private Long id;
    private Long userId;
    private String username;
    private String salaryMonth;
    private BigDecimal baseSalary;
    private BigDecimal bonus;
    private BigDecimal totalSalary;
} 