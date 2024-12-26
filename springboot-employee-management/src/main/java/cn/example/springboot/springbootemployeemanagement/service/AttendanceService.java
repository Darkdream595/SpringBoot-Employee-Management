package cn.example.springboot.springbootemployeemanagement.service;

import java.util.List;

import cn.example.springboot.springbootemployeemanagement.entity.Attendance;

public interface AttendanceService {
    List<Attendance> findByUserId(Long userId);
    List<Attendance> findAll();
    Attendance save(Attendance attendance);
} 