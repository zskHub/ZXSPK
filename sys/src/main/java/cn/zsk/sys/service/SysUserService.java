package cn.zsk.sys.service;


import cn.zsk.core.base.BaseService;
import cn.zsk.core.util.Checkbox;
import cn.zsk.core.util.JsonUtil;
import cn.zsk.sys.entity.SysRoleUser;
import cn.zsk.sys.entity.SysUser;

import java.util.List;

/**
 * @author zsk
 * @date 2017/12/4.
 *
 */
public interface SysUserService extends BaseService<SysUser,String> {

  SysUser login(String username);


  SysUser selectByPrimaryKey(String id);

  /**
   * 分页查询
   * @param
   * @return
   */
  @Override
  List<SysUser> selectListByPage(SysUser sysUser);

  int count();

  /**
   * 新增
   * @param user
   * @return
   */
  int add(SysUser user);

  /**
   * 删除
   * @param id
   * @return
   */
  JsonUtil delById(String id, boolean flag);

  int checkUser(String username);


  @Override
  int updateByPrimaryKey(SysUser sysUser);

  List<SysRoleUser> selectByCondition(SysRoleUser sysRoleUser);

  List<Checkbox> getUserRoleByJson(String id);

  /**
   * 更新密码
   * @param user
   * @return
   */
  int rePass(SysUser user);


  List<SysUser> getUserByRoleId(String roleId,int page,int limit);

  int countUserByRoleId(String roleId,int page,int limit);
}
