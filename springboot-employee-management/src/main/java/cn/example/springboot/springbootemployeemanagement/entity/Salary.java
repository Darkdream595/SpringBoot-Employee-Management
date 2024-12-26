package cn.example.springboot.springbootemployeemanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "salary")
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "salary_month", nullable = false)
    private String salaryMonth;

    @Column(name = "base_salary", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseSalary;

    @Column(name = "bonus", precision = 10, scale = 2)
    private BigDecimal bonus;

    @Column(name = "total_salary", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalSalary;

    @Column(name = "gmt_create", nullable = false)
    private Instant gmtCreate;

    @Column(name = "gmt_modified", nullable = false)
    private Instant gmtModified;
}