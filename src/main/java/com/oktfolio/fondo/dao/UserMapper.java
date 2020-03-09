package com.oktfolio.fondo.dao;

import com.oktfolio.fondo.model.SysUser;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/14
 */
@Repository
public interface UserMapper extends Mapper<SysUser>, MySqlMapper<SysUser> {
}
