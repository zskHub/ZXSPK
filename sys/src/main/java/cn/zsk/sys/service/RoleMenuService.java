package cn.zsk.sys.service;


import cn.zsk.core.base.BaseService;
import cn.zsk.sys.entity.SysRoleMenu;

import java.util.List;

/**
 * @author zsk
 * @date 2017/12/28.
 *
 */
public interface RoleMenuService extends BaseService<SysRoleMenu,String> {

    List<SysRoleMenu> selectByCondition(SysRoleMenu sysRoleMenu);

    int  selectCountByCondition(SysRoleMenu sysRoleMenu);

    int deleteByPrimaryKey(SysRoleMenu sysRoleMenu);
}
