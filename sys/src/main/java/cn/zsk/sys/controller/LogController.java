package cn.zsk.sys.controller;

import cn.zsk.core.base.BaseController;
import cn.zsk.core.exception.MyException;
import cn.zsk.core.util.JsonUtil;
import cn.zsk.core.util.ReType;
import cn.zsk.sys.entity.SysLog;
import cn.zsk.sys.mapper.SysLogMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author zsk
 * @date 2017/12/29.
 *
 * <p>
 * 日志监控
 */
@Controller
@RequestMapping(value = "/log")
@Slf4j
public class LogController extends BaseController {
    @Autowired
    private SysLogMapper logMapper;

    @GetMapping(value = "showLog")
    public String showMenu(Model model) {
        return "/system/log/logList";
    }

    /**
     * 日志监控
     *
     * @param sysLog
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "showLogList")
    @ResponseBody
    public ReType showLog(SysLog sysLog, String page, String limit) {
        List<SysLog> tList = null;
        Page<SysLog> tPage = PageHelper.startPage(Integer.valueOf(page), Integer.valueOf(limit));
        try {
            tList = logMapper.selectListByPage(sysLog);
        } catch (MyException e) {
            log.error("class:LogController ->method:showLog->message:" + e.getMessage());
            e.printStackTrace();
        }
        return new ReType(tPage.getTotal(), tList);
    }

    /**
     * 删除log
     *
     * @param
     * @return
     */
    @PostMapping(value = "del")
    @ResponseBody
    public JsonUtil del(String[] ids) {
        JsonUtil j = new JsonUtil();
        String msg = "删除成功";
        try {
            for (String id : ids) {
                logMapper.deleteByPrimaryKey(Integer.valueOf(id));
            }
        } catch (MyException e) {
            msg = "删除失败";
            log.error(msg + e.getMessage());
        }
        j.setMsg(msg);
        return j;
    }


}
