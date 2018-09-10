package cn.zsk.snow.manage.controller;

import cn.zsk.snow.manage.entity.SnowTestEntity;
import cn.zsk.snow.manage.service.SnowTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:zsk
 * @CreateTime:2018/8/14 16:31
 */
@Controller
@RequestMapping("/snowTest")
public class SnowTestController {
    @Autowired
    private SnowTestService snowTestService;


    @RequestMapping("findTestView")
    public String findTestView(){
        return "/test";
    }

    @ResponseBody
    @RequestMapping("findUserList")
    public String findUserList(){
        List<SnowTestEntity> snowTestEntityList = snowTestService.findUserList();
        return snowTestEntityList.toString();
    }
}
