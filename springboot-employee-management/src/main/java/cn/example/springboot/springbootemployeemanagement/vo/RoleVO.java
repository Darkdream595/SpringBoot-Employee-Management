package cn.example.springboot.springbootemployeemanagement.vo;

import java.time.Instant;
import java.util.List;

import lombok.Data;

@Data
public class RoleVO {
    private Long id;
    private String name;
    private List<String> permissions;
    private Instant gmtCreate;
    private Instant gmtModified;
} 