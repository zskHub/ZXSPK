package cn.zsk.wechat.business.controller;


import cn.zsk.core.validator.ValidatorUtils;
import cn.zsk.wechat.business.entity.HlgzhIndexBannerEntity;
import cn.zsk.wechat.business.service.HlgzhIndexBannerService;
import cn.zsk.wechat.business.utils.PageUtils;
import cn.zsk.wechat.business.utils.Query;
import cn.zsk.wechat.business.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/*
 *@author zsk
 *@Date 2018-09-20 16:58
 **/
@Controller
@RequestMapping("/hlgzhindexbanner")
public class HlgzhIndexBannerController {
    @Autowired
    private HlgzhIndexBannerService hlgzhIndexBannerService;


    /*
     * 跳转到页面
     * */
    @RequiresPermissions("hlgzhindexbanner:show")
    @RequestMapping("findHlgzhindexbannerView")
    public String findHlgzhindexbannerView(){
        System.out.println("************sdfasf*************************************************************************asfasfsafsdf*********************************************************");
        return "/hlgzhindexbanner";
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("hlgzhindexbanner:list")
    public R list(@RequestParam Map<String, Object> params){
        //查询列表数据
        Query query = new Query(params);

        List<HlgzhIndexBannerEntity> hlgzhIndexBannerList = hlgzhIndexBannerService.queryList(query);
        int total = hlgzhIndexBannerService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(hlgzhIndexBannerList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("hlgzhindexbanner:info")
    public R info(@PathVariable("id") Long id){
        HlgzhIndexBannerEntity hlgzhIndexBanner = hlgzhIndexBannerService.queryObject(id);

        return R.ok().put("hlgzhIndexBanner", hlgzhIndexBanner);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("hlgzhindexbanner:save")
    public R save(@RequestBody HlgzhIndexBannerEntity hlgzhIndexBanner){
        ValidatorUtils.validateEntity(hlgzhIndexBanner);
        hlgzhIndexBannerService.save(hlgzhIndexBanner);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("hlgzhindexbanner:update")
    public R update(@RequestBody HlgzhIndexBannerEntity hlgzhIndexBanner){
        ValidatorUtils.validateEntity(hlgzhIndexBanner);
        hlgzhIndexBannerService.update(hlgzhIndexBanner);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("hlgzhindexbanner:delete")
    public R delete(@RequestBody Long[] ids){
        hlgzhIndexBannerService.deleteBatch(ids);

        return R.ok();
    }
}
