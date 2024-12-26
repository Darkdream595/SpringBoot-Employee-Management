package cn.example.springboot.springbootemployeemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.example.springboot.springbootemployeemanagement.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByNameIn(List<String> names);
    Role findByName(String name);
    boolean existsByName(String name);
}
