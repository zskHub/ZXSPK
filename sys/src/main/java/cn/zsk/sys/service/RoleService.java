package cn.zsk.sys.service;


import cn.zsk.core.base.BaseService;
import cn.zsk.sys.entity.SysRole;

import java.util.List;

/**
 * @author zsk
 * @date 2017/12/19.
 *
 */
public interface RoleService extends BaseService<SysRole,String> {


  int deleteByPrimaryKey(String id);

  @Override
  int insert(SysRole record);

  @Override
  int insertSelective(SysRole record);


  SysRole selectByPrimaryKey(String id);

  @Override
  int updateByPrimaryKeySelective(SysRole record);

  @Override
  int updateByPrimaryKey(SysRole record);

  @Override
  List<SysRole> selectListByPage(SysRole sysRole);
}
