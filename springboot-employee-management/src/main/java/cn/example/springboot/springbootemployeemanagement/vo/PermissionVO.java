package cn.example.springboot.springbootemployeemanagement.vo;

import java.time.Instant;

import lombok.Data;

@Data
public class PermissionVO {
    private Long id;
    private String name;
    private Instant gmtCreate;
    private Instant gmtModified;
} 