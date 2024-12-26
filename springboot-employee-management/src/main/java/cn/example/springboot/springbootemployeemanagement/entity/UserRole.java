package cn.example.springboot.springbootemployeemanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "role_id", nullable = false)
    private Long roleId;
    
    @Column(name = "gmt_modified")
    private Instant gmtModified;
    
    @Column(name = "gmt_create")
    private Instant gmtCreate;
}
