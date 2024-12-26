package cn.example.springboot.springbootemployeemanagement.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.example.springboot.springbootemployeemanagement.entity.Salary;
import cn.example.springboot.springbootemployeemanagement.repository.SalaryRepository;
import cn.example.springboot.springbootemployeemanagement.service.SalaryService;

@Service
public class SalaryServiceImpl implements SalaryService {
    @Autowired
    private SalaryRepository salaryRepository;

    @Override
    public List<Salary> findAll() {
        return salaryRepository.findAllByOrderBySalaryMonthDesc();
    }

    @Override
    public List<Salary> findByUserId(Long userId) {
        return salaryRepository.findByUserId(userId);
    }

    @Override
    public Salary save(Salary salary) {
        return salaryRepository.save(salary);
    }
} 