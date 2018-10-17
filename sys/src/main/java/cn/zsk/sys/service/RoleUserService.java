package cn.zsk.sys.service;


import cn.zsk.core.base.BaseService;
import cn.zsk.sys.entity.SysRoleUser;

import java.util.List;

/**
 * @author zsk
 * @date 2017/12/21.
 *
 */
public interface RoleUserService  extends BaseService<SysRoleUser,String> {

  int deleteByPrimaryKey(SysRoleUser sysRoleUser);

  @Override
  int insert(SysRoleUser sysRoleUser);

  int selectCountByCondition(SysRoleUser sysRoleUser);

  List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser);
}
