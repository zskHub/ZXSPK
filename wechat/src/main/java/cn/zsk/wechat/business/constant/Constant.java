package cn.zsk.wechat.business.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量
 * 
 * @author gongbiao
 * @email gongbiao@mippoint.com
 * @date 2016年11月15日 下午1:23:52
 */
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;

	/*
	* 图片上传位置
	* */
    //public static final String fileDirectory="/opt/hlgzh/uploads";
    public static final String fileDirectory="d:/hlgzh/uploads";

    public static final String HOST_URL="http://fwh.bhgmall.com.cn/";

    /*
     *定义url用于wangEditor上传图片时拼接图片地址
     */
    public static final String IMGURL = "http://192.168.199.45:8081/hlgzh/";
    //public static final String IMGURL = "http://fwh.bhgmall.com.cn/hlgzh/";


	/**
	 * 菜单类型
	 * 
	 * @author gongbiao
	 * @email gongbiao@mippoint.com
	 * @date 2016年11月15日 下午1:24:29
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        private MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    /**
     * 定时任务状态
     * 
     * @author gongbiao
     * @email gongbiao@mippoint.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        private ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        private CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 异常信息统一头信息<br>
     * 非常遗憾的通知您,程序发生了异常
     */
    public static final String Exception_Head = "出错了，请联系管理员";
    /**
     * 缓存键值
     */
    public static final Map<Class<?>, String> cacheKeyMap = new HashMap<>();
    /**
     * 保存文件所在路径的key，eg.FILE_MD5:1243jkalsjflkwaejklgjawe
     */
    public static final String FILE_MD5_KEY = "FILE_MD5:";
    /**
     * 保存上传文件的状态
     */
    public static final String FILE_UPLOAD_STATUS = "FILE_UPLOAD_STATUS";

}
