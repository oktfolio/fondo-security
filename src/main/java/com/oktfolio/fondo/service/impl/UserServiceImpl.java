package com.oktfolio.fondo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.oktfolio.fondo.common.constant.Const;
import com.oktfolio.fondo.common.result.ResultEntity;
import com.oktfolio.fondo.dao.UserMapper;
import com.oktfolio.fondo.dao.UserRoleMapper;
import com.oktfolio.fondo.model.ListUsersRequest;
import com.oktfolio.fondo.model.SysUser;
import com.oktfolio.fondo.model.SysUserRole;
import com.oktfolio.fondo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.oktfolio.fondo.common.result.UserResultCodeEnum.FAILED_ADD_USER_ROLES;
import static com.oktfolio.fondo.common.result.UserResultCodeEnum.FAILED_DELETE_USER_ROLES;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/15
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public SysUser findByUsername(String username) {

        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username)
                .andNotEqualTo("status", Const.UserStatusEnum.DELETED.getValue());
        return userMapper.selectOneByExample(example);
    }

    @Override
    public int findByUsernameIncludeDeleted(String username) {
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return userMapper.selectCountByExample(example);
    }

    @Override
    public SysUser findById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insertUser(SysUser sysUser) {
        return userMapper.insert(sysUser);
    }

    @Override
    public PageInfo<SysUser> listUser(int pageNum, int pageSize, String orderBy, boolean desc,
                                      ListUsersRequest listUsersRequest) {
        Example example = new Example(SysUser.class);
        Example.Criteria criteria = example.createCriteria();

        // 若为降序为 true，则降序排列，否则升序
        if (desc) {
            example.orderBy(orderBy).desc();
        } else {
            example.orderBy(orderBy).asc();
        }

        if (listUsersRequest.getId() != null) {
            criteria.andEqualTo("id", listUsersRequest.getId());
        }
        if (listUsersRequest.getGender() != null) {
            criteria.andEqualTo("gender", listUsersRequest.getGender());
        }
        if (listUsersRequest.getMobile() != null) {
            criteria.andLike("mobile", "%" + listUsersRequest.getMobile() + "%");
        }
        if (listUsersRequest.getRealName() != null) {
            criteria.andLike("real_name", "%" + listUsersRequest.getRealName() + "%");
        }
        if (listUsersRequest.getStatus() != null) {
            criteria.andEqualTo("status", listUsersRequest.getStatus());
        }

        return PageHelper.startPage(pageNum, pageSize)
                .doSelectPageInfo(() -> userMapper.selectByExample(example));

    }

    @Transactional
    @Override
    public ResultEntity allocRolesForUser(Integer userId, String roleIds, Integer operatorId) {

        if (deleteAllRolesByUserId(userId)) {
            String[] roles = roleIds.split(",");

            List<String> roleStrList = Lists.newArrayList(roles);
            List<Integer> roleList = new ArrayList<>();
            List<SysUserRole> sysUserRoleList = new ArrayList<>();

            roleStrList.forEach((it) -> roleList.add(Integer.valueOf(it.trim())));

            roleList.forEach((it) -> {
                SysUserRole sysUserRole = new SysUserRole(userId, it);
                sysUserRole.setCreateAt(LocalDateTime.now());
                sysUserRole.setCreateBy(operatorId);
                sysUserRoleList.add(sysUserRole);
            });

            int i = userRoleMapper.insertList(sysUserRoleList);

            if (i > 0) {
                return ResultEntity.created();
            } else {
                return ResultEntity.badRequest(FAILED_ADD_USER_ROLES);
            }
        }
        return ResultEntity.badRequest(FAILED_DELETE_USER_ROLES);
    }

    private boolean deleteAllRolesByUserId(Integer userId) {

        Example example = new Example(SysUserRole.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);

        userRoleMapper.deleteByExample(example);

        return userRoleMapper.selectCountByExample(example) > 0;
    }

    @Override
    public int deleteById(Integer userId) {

        SysUser user = new SysUser();

        user.setId(userId);
        user.setStatus(Const.UserStatusEnum.DELETED.getValue());

        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int updateUserStatus(Integer userId, Integer status) {

        SysUser user = new SysUser();

        user.setId(userId);
        user.setStatus(status);

        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int updateWholeUser(SysUser sysUser) {
        return userMapper.updateByPrimaryKeySelective(sysUser);
    }

    @Override
    public List<Integer> listRoleIdsByUserId(Integer userId) {

        Example example = new Example(SysUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);

        List<SysUserRole> sysUserRoleList = userRoleMapper.selectByExample(example);

        List<Integer> roleIdsList = new ArrayList<>();

        sysUserRoleList.forEach((it) -> roleIdsList.add(it.getRoleId()));

        return roleIdsList;
    }
}
