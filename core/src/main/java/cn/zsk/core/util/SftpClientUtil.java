package cn.zsk.core.util;

import com.jcraft.jsch.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;


/**
 * @author:zsk
 * @CreateTime:2018-12-05 15:48
 */
public class SftpClientUtil {
    private static final Logger logger=LoggerFactory.getLogger(SftpClientUtil.class);

    ChannelSftp sftp = null;

    private String host = "";

    private int port = 0;

    private String userName = "";

    private String passWord = "";

    /**
     * 调用方式：
     * SftpClientUtil sftpclient = new SftpClientUtil( host, port, userName, passWord);
     * sftpclient.connect();
     * sftpclient.download(fieDirPath,backup.getBackname(),strFileName);
     */
    //获取ftp链接
    public SftpClientUtil(String host, int port, String userName, String passWord) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.passWord = passWord;
    }

    //获取ftp链接
    public Map connect() throws Exception {
        Map<String,Object> map = new HashMap<>();
        if (StringUtils.isBlank(this.host)) {
            logger.debug("host为空");
            map.put("code","-1");
            map.put("msg","host为空");
            return map;
        }
        if (StringUtils.isBlank(this.passWord)) {
            logger.debug("passWord为空");
            map.put("code","-1");
            map.put("msg","passWord为空");
            return map;
        }
        if (StringUtils.isBlank(this.userName)) {
            logger.debug("userName为空");
            map.put("code","-1");
            map.put("msg","userName为空");
            return map;
        }
        if (this.port == 0) {
            logger.debug("port为空");
            map.put("code","-1");
            map.put("msg","port为空");
            return map;
        }
        JSch jsch = new JSch();
        Session sshSession = jsch.getSession(this.userName, this.host, this.port);
        logger.debug(SftpClientUtil.class + "::::::::Session created.");

        sshSession.setPassword(this.passWord);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "::::::::no");
        sshSession.setConfig(sshConfig);
        sshSession.connect(20000);
        logger.debug(SftpClientUtil.class + ":::::::: Session connected.");

        logger.debug(SftpClientUtil.class + ":::::::: Opening Channel.");
        Channel channel = sshSession.openChannel("sftp");
        channel.connect();
        this.sftp = ((ChannelSftp) channel);
        logger.debug(SftpClientUtil.class + ":::::::: Connected to " + this.host + ".");
        map.put("code",0);
        return map;
    }

    //断开ftp链接
    public void disconnect() throws Exception {
        if (this.sftp != null) {
            if (this.sftp.isConnected()) {
                this.sftp.disconnect();
                logger.debug(SftpClientUtil.class + ":::::::: sftp is closed already");
            } else if (this.sftp.isClosed()) {
                logger.debug(SftpClientUtil.class + ":::::::: sftp is closed already");
            }
        }
    }


    /*
    * 上传文件
    * */
    public Map upload(String directory,String originDirectory,String originFileName,String upFileNewName,Boolean isEditName) throws FileNotFoundException {
        InputStream is = new FileInputStream(new File(originDirectory+originFileName));
        Map<String,Object> map = new HashMap<>();
        if (!check(originFileName)){
            map.put("code","-1");
            map.put("msg","传入参数不完全");
            return map;
        }
        if(isEditName){
            if(StringUtils.isBlank(upFileNewName)){
                map.put("code","-1");
                map.put("msg","已开启更名，请输入新文件名");
                return map;
            }
        }
        try {
            /*
            * 默认是上传的原文件名
            * */
            String endFileName = originFileName;
            if(isEditName){
                endFileName = upFileNewName;
            }

            try {
                Vector ls = this.sftp.ls(directory);
                if (null == ls){
                    this.sftp.mkdir(directory);
                }

            }
            catch (SftpException sftp) {
                this.sftp.mkdir(directory);
            }
            this.sftp.cd(directory);
            this.sftp.put(is, endFileName);
            map.put("code","0");
            map.put("msg","success");
            return map;
        } catch (Exception e) {
            logger.error("上传文件失败", e);
            map.put("code","-2");
            map.put("msg","上传文件失败");
            return map;
        }
    }

    public void uploadByDirectory(String directory)
            throws Exception {
        String uploadFile = "";
        List uploadFileList = listFiles(directory);
        Iterator it = uploadFileList.iterator();

        while (it.hasNext()) {
            uploadFile = ((String) it.next()).toString();
            upload(directory, uploadFile);
        }
    }

    public void upload(String directory, String uploadFile)
            throws Exception {
        this.sftp.cd(directory);
        File file = new File(uploadFile);
        this.sftp.put(new FileInputStream(file), file.getName());
    }

    /*
    * 将服务器中某个文件拷贝到另一个文件夹中
    * */
    public void download(String directory, String downloadFile, String saveDirectory)
            throws Exception {
        String saveFile = saveDirectory + "//" + downloadFile;
        this.sftp.cd(directory);
        File file = new File(saveFile);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        this.sftp.get(downloadFile, fileOutputStream);
        fileOutputStream.close();
        this.sftp.disconnect();
    }

    /*
    * 下载文件
    *
    * */
    public Map downloadFile(String directoryUrl, String downFileName,String downDirectory) {
        Map<String,Object> map = new HashMap<>();
        if (!check(downFileName)) {
            map.put("code","-1");
            map.put("msg","传入参数不完全");
            return map;
        }
        try {
            this.sftp.cd(directoryUrl);
            InputStream is = this.sftp.get(downFileName);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(downDirectory);
                byte[] b = new byte[10240];
                while (is.read(b, 0, 10240) != -1) {
                    fos.write(b, 0, 10240);
                }
                map.put("code","0");
                map.put("msg","success");
                fos.flush();
                fos.close();
            }catch (Exception e){

            }finally {
                if(fos != null){
                    fos.flush();
                    fos.close();
                }
            }

            return map;
        } catch (Exception e) {
            logger.error("下载文件失败", e);
            map.put("code","-2");
            map.put("msg","操作失败，出现异常");
            return map;
        }

    }


    private byte[] getBytes(InputStream in) {
        byte[] buffer = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = in.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            in.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
        }
        return buffer;
    }

    /*
    * 批量将一个文件夹下所有文件拷贝到另一个文件夹中
    * */
    public void downloadByDirectory(String directory, String saveDirectory)
            throws Exception {
        String downloadFile = "";
        List downloadFileList = listFiles(directory);
        Iterator it = downloadFileList.iterator();

        while (it.hasNext()) {
            downloadFile = ((String) it.next()).toString();
            if (downloadFile.toString().indexOf(".") < 0) {
                continue;
            }
            download(directory, downloadFile, saveDirectory);
        }
    }

    /*
    * 删除某个目录下的某个文件
    * */
    public void delete(String directory, String deleteFile)
            throws Exception {
        this.sftp.cd(directory);
        Vector content = this.sftp.ls(deleteFile);
        if (content != null){
            this.sftp.rm(deleteFile);
        }
    }

    public List<String> listFiles(String directory) throws Exception {
        List fileNameList = new ArrayList();

        Vector fileList = this.sftp.ls(directory);
        Iterator it = fileList.iterator();

        while (it.hasNext()) {
            String fileName = ((ChannelSftp.LsEntry) it.next()).getFilename();
            if ((".".equals(fileName)) || ("..".equals(fileName))) {
                continue;
            }
            fileNameList.add(fileName);
        }

        return fileNameList;
    }

    /*
    * 服务器文件重命名
    * */
    public void rename(String directory, String oldFileNm, String newFileNm)
            throws Exception {
        this.sftp.cd(directory);
        this.sftp.rename(oldFileNm, newFileNm);
    }
    /*
    * cd到服务器相关目录
    * */
    public void cd(String directory) throws Exception {
        this.sftp.cd(directory);
    }

    /*
    * 获取目录下文件流
    * */
    public InputStream get(String directory) throws Exception {
        InputStream streatm = this.sftp.get(directory);
        return streatm;
    }

    /*
    * 生成新的文件名方法
    * */
    private String getNewFileName(String fileName) {
        Long timeStamp = Long.valueOf(System.currentTimeMillis());
        return timeStamp + "_" + fileName;
    }
    /*
    * 检查文件名是否合法
    * */
    private boolean check(String fileName) {
        if (StringUtils.isBlank(this.host)) {
            return false;
        }
        if (StringUtils.isBlank(this.passWord)) {
            return false;
        }
        if (StringUtils.isBlank(this.userName)) {
            return false;
        }
        if (this.port == 0) {
            return false;
        }

        return !StringUtils.isBlank(fileName);
    }

    //测试方法
    public static void main(String[] args) throws Exception {

        /*
        * ip、端口号、用户名、密码
        *
        * */
        SftpClientUtil sftpClientUtil = new SftpClientUtil("192.144.151.141", 22, "test", "test");
        try {
            Map<String,Object> map = sftpClientUtil.connect();
            if(Integer.valueOf(map.get("code").toString()) == 0){
                /*
                 * 测试删除
                 * */
//                sftpClientUtil.delete("/home/cp_beijing/test/", "1544068764188_测试123.docx");

                /*
                 * 测试上传
                 * 最后的参数是判断上传文件是否改名字，false：不改变，true：改变。
                 * 为true时，必须输入新改的文件名字
                 * */
                Map<String,Object> upMap = sftpClientUtil.upload("/home/cp_beijing/test/", "E:\\downTest\\","测试ing.docx","测试上传新名字.docx",true);
                System.out.println(upMap);
                if("0".equals(upMap.get("code"))){
                    System.out.println("上传成功了");
                }

                /*
                 * 测试下载
                 * */
    //          Map downloadMap = sftpClientUtil.downloadFile("/home/cp_beijing/test/", "1544068893389_测试123.docx","E:\\downTest\\测试下载.docx");
    //          System.out.println(downloadMap);


            }else {
                logger.debug("连接ftp失败，失败原因："+map.get("msg").toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            sftpClientUtil.disconnect();

        }
    }
}