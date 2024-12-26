package cn.example.springboot.springbootemployeemanagement.vo;

import java.time.Instant;
import java.util.List;

import lombok.Data;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String password;
    private List<String> roles;
    private Instant gmtCreate;
    private Instant gmtModified;
}