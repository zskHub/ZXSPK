package cn.zsk.core.util;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author zsk
 * @date 2017/12/7.
 *
 * 采用md5加密 确保数据安全性
 */
public class Md5Util {
  public static String getMD5(String msg,String salt,int length){
    return new Md5Hash(msg,salt,length).toString();
  }
}
