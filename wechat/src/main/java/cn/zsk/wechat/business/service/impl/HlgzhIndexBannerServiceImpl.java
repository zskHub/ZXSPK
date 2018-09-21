package cn.zsk.wechat.business.service.impl;


import cn.zsk.wechat.business.entity.HlgzhIndexBannerEntity;
import cn.zsk.wechat.business.mapper.HlgzhIndexBannerDao;
import cn.zsk.wechat.business.service.HlgzhIndexBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;




@Service("hlgzhMdBannerService")
public class HlgzhIndexBannerServiceImpl implements HlgzhIndexBannerService {
	@Autowired
	private HlgzhIndexBannerDao hlgzhIndexBannerDao;
	
	@Override
	public HlgzhIndexBannerEntity queryObject(Long id){
		return hlgzhIndexBannerDao.queryObject(id);
	}
	
	@Override
	public List<HlgzhIndexBannerEntity> queryList(Map<String, Object> map){
		return hlgzhIndexBannerDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return hlgzhIndexBannerDao.queryTotal(map);
	}
	
	@Override
	public void save(HlgzhIndexBannerEntity hlgzhMdBanner){
		hlgzhIndexBannerDao.save(hlgzhMdBanner);
	}
	
	@Override
	public void update(HlgzhIndexBannerEntity hlgzhMdBanner){
		hlgzhIndexBannerDao.update(hlgzhMdBanner);
	}
	
	@Override
	public void delete(Long id){
		hlgzhIndexBannerDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		hlgzhIndexBannerDao.deleteBatch(ids);
	}
	
}
