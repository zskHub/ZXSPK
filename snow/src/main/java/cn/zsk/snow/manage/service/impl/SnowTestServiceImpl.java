package cn.zsk.snow.manage.service.impl;

import cn.zsk.snow.manage.entity.SnowTestEntity;
import cn.zsk.snow.manage.mapper.SnowTestMapper;
import cn.zsk.snow.manage.service.SnowTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:zsk
 * @CreateTime:2018-09-10 16:18
 */
@Service
public class SnowTestServiceImpl implements SnowTestService {
    @Autowired
    private SnowTestMapper snowTestMapper;

    @Override
    public List<SnowTestEntity> findUserList() {
        return snowTestMapper.findUserList();
    }
}
