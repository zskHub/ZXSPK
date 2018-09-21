package cn.zsk.wechat.business.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;



/**
 * 
 * 
 * @author gongbiao
 * @email gongbiao@mippoint.com
 * @date 2018-07-11 11:07:35
 */
public class HlgzhIndexBannerEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//ID
	private Long id;
	//图片
	@NotNull(message="图片不能为空")
	private String url;
	//标题
	@NotNull(message = "标题不能为空")
	private String title;
	//内容
	@NotNull(message = "内容不能为空")
	private String content;


	/**
	 * 设置：ID
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：ID
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：图片
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：图片
	 */
	public String getUrl() {
		return url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
