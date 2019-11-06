package com.oktfolio.fondo.dao;

import com.oktfolio.fondo.model.SysResource;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author Oktfolio oktfolio@gmail.com
 * @date 2019/09/14
 */
@Repository
public interface ResourceMapper extends Mapper<SysResource>, MySqlMapper<SysResource> {
}
