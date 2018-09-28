package cn.zsk.snow.manage.controller;

import cn.zsk.snow.manage.entity.SnowTestEntity;
import cn.zsk.snow.manage.service.SnowTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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


    public static void main(String [] args){

        Map<String,Integer> map = new IdentityHashMap();
        map.put(new String("a"),1);
        map.put(new String("a"),2);
        map.put(new String("a"),3);
        map.put(new String("b"),1);
        map.put(new String("c"),6);

        //存放字母
        ArrayList<String> word = new ArrayList<>();
        //存放数字
        ArrayList<Integer> num = new ArrayList<>();
        //统计相同key有多少个
        Map<String,Integer> map1 = new HashMap<>();
        System.out.println(map.containsKey("a"));


        for(Map.Entry<String,Integer> entry : map.entrySet()) {
            if(!(num.contains(entry.getValue()))){
                num.add(entry.getValue());
                if(!(word.contains(entry.getKey()))){
                    word.add(entry.getKey());
                }
            }

        }
        System.out.println(word.toString());







    }

}
