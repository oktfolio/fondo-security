package com.oktfolio.fondo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.oktfolio.fondo.common.constant.Const;
import com.oktfolio.fondo.common.result.ResultEntity;
import com.oktfolio.fondo.model.AllocRoleRequest;
import com.oktfolio.fondo.model.ListUsersRequest;
import com.oktfolio.fondo.model.SysUser;
import com.oktfolio.fondo.security.JWTUserDetails;
import com.oktfolio.fondo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.oktfolio.fondo.common.result.UserResultCodeEnum.*;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/13
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody SysUser user) {

        int foundUsers = userService.findByUsernameIncludeDeleted(user.getUsername());

        if (foundUsers == 0) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(encryptedPassword);

            user.setStatus(Const.UserStatusEnum.DISABLED.getValue());

            int res = userService.insertUser(user);

            if (res == 1) {
                return new ResponseEntity(HttpStatus.CREATED);
            } else {
                ResultEntity result = ResultEntity.badRequest(FAILED_CREATE_USER);
                return new ResponseEntity<>(result, result.getStatus());
            }
        } else {
            ResultEntity result = ResultEntity.badRequest(USERNAME_EXISTS);
            return new ResponseEntity<>(result, result.getStatus());
        }
    }

    @JsonView(SysUser.WithoutPasswordView.class)
    @GetMapping("/users")
    public ResponseEntity listUsers(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "order", defaultValue = "id") String orderBy,
                                    @RequestParam(value = "desc", defaultValue = "false") boolean desc,
                                    @ModelAttribute ListUsersRequest listUsersRequest) {

        return ResponseEntity.ok(userService.listUser(pageNum, pageSize, orderBy, desc, listUsersRequest));
    }

    @JsonView(SysUser.WithoutPasswordView.class)
    @GetMapping("/users/{userId:\\d+$}")
    public ResponseEntity getUserById(@PathVariable("userId") Integer userId) {
        SysUser sysUser = userService.findById(userId);

        if (sysUser != null) {
            return ResponseEntity.ok(sysUser);
        }

        ResultEntity result = ResultEntity.notFound(USER_NOT_EXIST);
        return new ResponseEntity<>(result, result.getStatus());
    }

    @PutMapping("/users/{userId:\\d+$}")
    public ResponseEntity updateWholeUser(@PathVariable("userId") Integer userId,
                                          @RequestBody SysUser sysUser) {

        SysUser foundUser = userService.findById(userId);
        if (foundUser != null) {
            int res = userService.updateWholeUser(sysUser);
            if (res == 1) {
                return ResponseEntity.ok().build();
            } else {
                return new ResponseEntity<>(ResultEntity.builder()
                        .code(FAILED_DELETE_USER.value())
                        .message(FAILED_DELETE_USER.message())
                        .timestamp(LocalDateTime.now())
                        .build(), HttpStatus.BAD_REQUEST);
            }
        }
        ResultEntity result = ResultEntity.notFound(USER_NOT_EXIST);
        return new ResponseEntity<>(result, result.getStatus());
    }


    /**
     * 根据 Id 删除用户
     *
     * @param userId
     * @return
     */
    @DeleteMapping("/users/{userId:\\d+$}")
    public ResponseEntity deleteUser(@PathVariable("userId") Integer userId) {

        SysUser foundUser = userService.findById(userId);
        if (foundUser != null) {
            int res = userService.deleteById(userId);
            if (res == 1) {
                return ResponseEntity.ok().build();
            } else {
                return new ResponseEntity<>(ResultEntity.builder()
                        .code(FAILED_DELETE_USER.value())
                        .message(FAILED_DELETE_USER.message())
                        .timestamp(LocalDateTime.now())
                        .build(), HttpStatus.BAD_REQUEST);
            }
        }
        ResultEntity result = ResultEntity.notFound(USER_NOT_EXIST);
        return new ResponseEntity<>(result, result.getStatus());
    }

    /**
     * 修改用户状态
     *
     * @param userId
     * @param status
     * @return
     */
    @PatchMapping("/users/{userId:\\d+$}/status/{status:\\d+$}")
    public ResponseEntity updateUserStatus(@PathVariable("userId") Integer userId,
                                           @PathVariable("status") Integer status) {
        SysUser foundUser = userService.findById(userId);
        if (foundUser != null) {
            int res = userService.updateUserStatus(userId, status);
            if (res == 1) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            ResultEntity result = ResultEntity.notFound(USER_NOT_EXIST);
            return new ResponseEntity<>(result, result.getStatus());
        }
    }


    /**
     * 给用户分配角色
     *
     * @param userId
     * @param allocRoleRequest
     * @return
     */
    @PostMapping("/users/{userId:\\d+$}/roles")
    public ResponseEntity allocRolesForUser(@PathVariable("userId") Integer userId,
                                            @RequestBody AllocRoleRequest allocRoleRequest,
                                            @Autowired Authentication authentication) {

        JWTUserDetails operator = (JWTUserDetails) authentication.getPrincipal();
        Integer operatorId = operator.getId();

        ResultEntity result = userService.allocRolesForUser(userId, allocRoleRequest.getRoleIds(), operatorId);
        return new ResponseEntity<>(result, result.getStatus());
    }
}
