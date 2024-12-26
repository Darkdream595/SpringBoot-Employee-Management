package cn.example.springboot.springbootemployeemanagement.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.example.springboot.springbootemployeemanagement.entity.Attendance;
import cn.example.springboot.springbootemployeemanagement.repository.AttendanceRepository;
import cn.example.springboot.springbootemployeemanagement.service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public List<Attendance> findAll() {
        return attendanceRepository.findAllByOrderByAttendanceDateDesc();
    }

    @Override
    public List<Attendance> findByUserId(Long userId) {
        return attendanceRepository.findByUserId(userId);
    }

    @Override
    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }
} 