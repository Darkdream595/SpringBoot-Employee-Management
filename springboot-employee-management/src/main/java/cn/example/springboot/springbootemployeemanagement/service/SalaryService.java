package cn.example.springboot.springbootemployeemanagement.service;

import java.util.List;

import cn.example.springboot.springbootemployeemanagement.entity.Salary;

public interface SalaryService {
    List<Salary> findByUserId(Long userId);
    List<Salary> findAll();
    Salary save(Salary salary);
} 