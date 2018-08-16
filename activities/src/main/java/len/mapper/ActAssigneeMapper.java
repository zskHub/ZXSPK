package len.mapper;


import cn.zsk.core.base.BaseMapper;
import len.entity.ActAssignee;

public interface ActAssigneeMapper extends BaseMapper<ActAssignee,String> {
    int deleteByNodeId(String nodeId);
}