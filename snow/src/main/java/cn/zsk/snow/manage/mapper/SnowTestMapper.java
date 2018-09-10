package cn.zsk.snow.manage.mapper;

import cn.zsk.snow.manage.entity.SnowTestEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:zsk
 * @CreateTime:2018/9/6 17:58
 */
@Mapper
public interface SnowTestMapper {

    List<SnowTestEntity> findUserList();
}
