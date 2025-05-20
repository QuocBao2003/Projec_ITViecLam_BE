package com.example.demo.config;

import com.example.demo.domain.Permission;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import com.example.demo.util.SecurityUtil;
import com.example.demo.util.error.IdInvalidException;
import com.example.demo.util.error.PermissionExceeption;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

//Request -> Secutiry->Interceptor -> Controller ->Service
public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;
    @Override
    @Transactional
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler)
            throws PermissionExceeption {
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        System.out.println(">>> RUN preHandle");
        System.out.println(">>> path= " + path);
        System.out.println(">>> httpMethod= " + httpMethod);
        System.out.println(">>> requestURI= " + requestURI);
//        check  permission để được di tiếp sang controller
        String email = SecurityUtil.getCurrentUserLogin().isEmpty() == true ?SecurityUtil.getCurrentUserLogin().get() : "";
        if(email != null && !email.isEmpty()){
            User user = this.userService.handleGetUserByUserName(email);
            if(user!=null){
                Role role = user.getRole();
                if(role!=null){
                    List<Permission> permissions=role.getPermissions();

                    boolean isAllow = permissions.stream().anyMatch(item ->item.getApiPath().equals(path)
                            && item.getMethod().equals(httpMethod));
                    if(isAllow==false){
                        throw  new PermissionExceeption("Bạn không có quyền truy cập enpoint này");
                    }
                    }else{
                        throw  new PermissionExceeption("Bạn không có quyền truy cập enpoint này");
                }
            }

        }
        return true;
    }
}
