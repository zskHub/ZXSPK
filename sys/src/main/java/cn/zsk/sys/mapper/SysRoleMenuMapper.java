package cn.zsk.sys.mapper;


import cn.zsk.core.base.BaseMapper;
import cn.zsk.sys.entity.SysRoleMenu;

import java.util.List;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu, String> {
    List<SysRoleMenu> selectByCondition(SysRoleMenu sysRoleMenu);

    int selectCountByCondition(SysRoleMenu sysRoleMenu);
}