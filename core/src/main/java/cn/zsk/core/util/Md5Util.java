package cn.zsk.core.util;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author zsk
 * @date 2017/12/7.
 *
 * 采用md5加密 确保数据安全性
 */
public class Md5Util {
  public static String getMD5(String msg,String salt){
    return new Md5Hash(msg,salt,1024).toString();
  }
}
