package com.oktfolio.fondo.service;

import com.github.pagehelper.PageInfo;
import com.oktfolio.fondo.common.result.ResultEntity;
import com.oktfolio.fondo.model.ListUsersRequest;
import com.oktfolio.fondo.model.SysUser;

import java.util.List;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/13
 */
public interface UserService {

    int findByUsernameIncludeDeleted(String username);

    SysUser findByUsername(String username);

    SysUser findById(Integer id);

    int insertUser(SysUser sysUser);

    PageInfo<SysUser> listUser(int pageNum, int pageSize, String orderBy, boolean desc, ListUsersRequest listUsersRequest);

    ResultEntity allocRolesForUser(Integer userId, String roleIds, Integer operatorId);

    int deleteById(Integer userId);

    int updateUserStatus(Integer userId, Integer status);

    int updateWholeUser(SysUser sysUser);

    List<Integer> listRoleIdsByUserId(Integer userId);
}
