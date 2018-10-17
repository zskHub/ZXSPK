/**
 * Copyright 2018 lenos
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package len.actlistener;

import cn.zsk.core.util.SpringUtil;
import len.entity.ActAssignee;
import len.service.ActAssigneeService;
import len.service.impl.ActAssigneeServiceImpl;
import len.util.AssigneeType;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

/**
 * @author zsk
 * @date 2018/1/20.
 *
 * <p>
 * 流程监听器 动态注入节点办理人
 */
public class ActNodeListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        //KEY
        String nodeId = delegateTask.getTaskDefinitionKey();
        ActAssigneeService actAssigneeService = SpringUtil.getBean(ActAssigneeServiceImpl.class);
        List<ActAssignee> assigneeList = actAssigneeService.selectListByPage(new ActAssignee(nodeId));
        for (ActAssignee assignee : assigneeList) {
            switch (assignee.getAssigneeType()) {
                case AssigneeType.GROUP_TYPE:
                    delegateTask.addCandidateGroup(assignee.getRoleId());
                    break;
                case AssigneeType.USER_TYPE:
                    delegateTask.addCandidateUser(assignee.getAssignee());
                    break;
                    default:
            }
        }
    }
}
