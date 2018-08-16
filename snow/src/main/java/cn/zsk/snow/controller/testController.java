package cn.zsk.snow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author:zsk
 * @CreateTime:2018/8/14 16:31
 */
@Controller
@RequestMapping("/test")
public class testController {


    @RequestMapping("findTestView")
    public String findTestView(){
        return "/test";
    }
}
