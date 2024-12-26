package cn.example.springboot.springbootemployeemanagement.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.example.springboot.springbootemployeemanagement.entity.Attendance;
import cn.example.springboot.springbootemployeemanagement.entity.User;
import cn.example.springboot.springbootemployeemanagement.service.AttendanceService;
import cn.example.springboot.springbootemployeemanagement.service.UserService;
import cn.example.springboot.springbootemployeemanagement.vo.AttendanceVO;
import cn.example.springboot.springbootemployeemanagement.vo.JsonResult;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('HR', 'SYSTEM_ADMIN')")
    public JsonResult list() {
        List<Attendance> attendances = attendanceService.findAll();
        List<AttendanceVO> attendanceVOs = attendances.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return JsonResult.success(attendanceVOs);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('NORMAL_EMPLOYEE', 'HR', 'SYSTEM_ADMIN')")
    public JsonResult getMyAttendance() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getByUsername(username).orElse(null);
        if (user == null) {
            return JsonResult.fail("用户不存在");
        }
        List<Attendance> attendances = attendanceService.findByUserId(user.getId());
        List<AttendanceVO> attendanceVOs = attendances.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return JsonResult.success(attendanceVOs);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('HR', 'SYSTEM_ADMIN')")
    public JsonResult getUserAttendance(@PathVariable Long userId) {
        List<Attendance> attendances = attendanceService.findByUserId(userId);
        List<AttendanceVO> attendanceVOs = attendances.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        return JsonResult.success(attendanceVOs);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('HR') or hasRole('SYSTEM_ADMIN')")
    public JsonResult add(@RequestBody AttendanceVO attendanceVO) {
        try {
            Attendance attendance = new Attendance();
            attendance.setUserId(attendanceVO.getUserId());
            
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate attendanceDate = LocalDate.parse(attendanceVO.getAttendanceDate(), dateFormatter);
            attendance.setAttendanceDate(attendanceDate);
            
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalTime checkIn = LocalTime.parse(attendanceVO.getCheckIn(), timeFormatter);
            LocalTime checkOut = LocalTime.parse(attendanceVO.getCheckOut(), timeFormatter);
            attendance.setCheckIn(checkIn);
            attendance.setCheckOut(checkOut);
            
            attendance.setStatus(attendanceVO.getStatus());
            attendance.setGmtCreate(Instant.now());
            attendance.setGmtModified(Instant.now());
            
            attendanceService.save(attendance);
            return JsonResult.success("添加成功");
        } catch (Exception e) {
            return JsonResult.fail("添加失败：" + e.getMessage());
        }
    }

    private AttendanceVO convertToVO(Attendance attendance) {
        AttendanceVO vo = new AttendanceVO();
        vo.setId(attendance.getId());
        vo.setUserId(attendance.getUserId());
        User user = userService.getById(attendance.getUserId());
        vo.setUsername(user != null ? user.getUsername() : null);
        vo.setAttendanceDate(attendance.getAttendanceDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        vo.setCheckIn(attendance.getCheckIn() != null ? attendance.getCheckIn().format(DateTimeFormatter.ISO_LOCAL_TIME) : null);
        vo.setCheckOut(attendance.getCheckOut() != null ? attendance.getCheckOut().format(DateTimeFormatter.ISO_LOCAL_TIME) : null);
        vo.setStatus(attendance.getStatus());
        vo.setGmtCreate(attendance.getGmtCreate());
        vo.setGmtModified(attendance.getGmtModified());
        return vo;
    }
} 