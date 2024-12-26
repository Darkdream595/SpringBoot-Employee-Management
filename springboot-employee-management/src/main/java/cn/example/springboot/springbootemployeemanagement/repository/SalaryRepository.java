package cn.example.springboot.springbootemployeemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.example.springboot.springbootemployeemanagement.entity.Salary;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    List<Salary> findByUserId(Long userId);
    List<Salary> findAllByOrderBySalaryMonthDesc();
}