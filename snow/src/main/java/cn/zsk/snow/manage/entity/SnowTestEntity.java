package cn.zsk.snow.manage.entity;

import cn.zsk.core.validator.group.AddGroup;
import cn.zsk.core.validator.group.UpdateGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

/**
 * @author:zsk
 * @CreateTime:2018/9/6 18:07
 */
@Table(name = "snow_test")
@Data
@ToString
@EqualsAndHashCode
public class SnowTestEntity {
    @Id
    @GeneratedValue(generator = "JDBC")
    private String id;

    /**
     * 描述任务
     */
    @NotEmpty(message = "测试使用，不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "name")
    private String name;

}
