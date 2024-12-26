package cn.example.springboot.springbootemployeemanagement.vo;

import java.time.Instant;

import lombok.Data;

@Data
public class AttendanceVO {
    private Long id;
    private Long userId;
    private String username;
    private String attendanceDate;
    private String checkIn;
    private String checkOut;
    private String status;
    private Instant gmtCreate;
    private Instant gmtModified;
} 