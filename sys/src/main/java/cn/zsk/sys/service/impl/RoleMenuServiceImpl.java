package cn.zsk.sys.service.impl;


import cn.zsk.core.base.BaseMapper;
import cn.zsk.core.base.impl.BaseServiceImpl;
import cn.zsk.sys.entity.SysRoleMenu;
import cn.zsk.sys.mapper.SysRoleMenuMapper;
import cn.zsk.sys.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zsk
 * @date 2017/12/28.
 *
 */
@Service
public class RoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenu,String> implements
        RoleMenuService {
    @Autowired
    private SysRoleMenuMapper roleMenuMapper;
    @Override
    public BaseMapper<SysRoleMenu, String> getMappser() {
        return roleMenuMapper;
    }

    @Override
    public List<SysRoleMenu> selectByCondition(SysRoleMenu sysRoleMenu) {
        return roleMenuMapper.selectByCondition(sysRoleMenu);
    }

    @Override
    public int selectCountByCondition(SysRoleMenu sysRoleMenu) {
        return roleMenuMapper.selectCountByCondition(sysRoleMenu);
    }

    @Override
    public int deleteByPrimaryKey(SysRoleMenu sysRoleMenu) {
        return roleMenuMapper.deleteByPrimaryKey(sysRoleMenu);
    }
}
