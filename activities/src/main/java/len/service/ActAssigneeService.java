package len.service;


import cn.zsk.core.base.BaseService;
import len.entity.ActAssignee;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.List;

/**
 * @author zsk
 * @date 2018/1/23.
 *
 */
public interface ActAssigneeService extends BaseService<ActAssignee,String> {
  int deleteByNodeId(String nodeId);

  List<ActivityImpl> getActivityList(String deploymentId);

  List<ActivityImpl> selectAllActivity(List<ActivityImpl> activities);

}
