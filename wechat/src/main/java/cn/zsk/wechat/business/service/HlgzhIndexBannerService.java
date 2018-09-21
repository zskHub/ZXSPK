package cn.zsk.wechat.business.service;


import cn.zsk.wechat.business.entity.HlgzhIndexBannerEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author gongbiao
 * @email gongbiao@mippoint.com
 * @date 2018-07-11 11:07:35
 */
public interface HlgzhIndexBannerService {
	
	HlgzhIndexBannerEntity queryObject(Long id);
	
	List<HlgzhIndexBannerEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(HlgzhIndexBannerEntity hlgzhIndexBanner);
	
	void update(HlgzhIndexBannerEntity hlgzhIndexBanner);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
