package com.controller;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    public static int index = 0;
    public static String pathUrl  = "";
    public static String fileName  = "";

    public static String zipFile(String sourcePath, HttpServletRequest request){
        /**创建一个临时压缩文件，
         * 我们会把文件流全部注入到这个文件中
         * 这里的文件你可以自定义是.rar还是.zip*/
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        //String path = "c:/guidang.zip";//这种写法是错误的，之前理解的有问题，因为是在本机测试，
        //windows系统存在c盘，而我们项目的服务器是Linux的，致使识别不了。可以将临时文件保存到一个临时路径文件夹tempPath下
        String fileName = new SimpleDateFormat(
                "yyyyMMddHHmmssSSS").format(new Date())+".zip";
        String path = request.getRealPath("/")+"upload/"+fileName;
        File zipFile = null;
        try {
            zipFile = new File(path);
            if (!zipFile.exists()){
                zipFile.createNewFile();
            }
            //创建文件输出流
            fos = new FileOutputStream(zipFile);
            /**打包的方法我们会用到ZipOutputStream这样一个输出流,
             * 所以这里我们把输出流转换一下*/
            zos = new ZipOutputStream(fos);
            writeZip(new File(sourcePath), "", zos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally{
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
//                log.error("创建ZIP文件失败",e);
            }
            //删除临时文件
            if (zipFile.exists()) {
//                zipFile.delete();
            }
        }
        return path;
    }

    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if(file.exists()){
            if(file.isDirectory()){//处理文件夹
                parentPath+=file.getName()+File.separator;
                File [] files=file.listFiles();
                for(File f:files){
                    writeZip(f, parentPath, zos);
                }
            }else{

                FileInputStream fis=null;
                try {
                    fis=new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte [] content=new byte[2048];
                    int len;
                    while((len=fis.read(content))!=-1){
                        zos.write(content,0,len);
                        zos.flush();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    //关闭流
                    try {
                        if(null != fis) fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

//    public void downLoad(String filePath,HttpServletRequest request){
//
//        HttpServletResponse response = this.getResponse();
//
//        BufferedInputStream bis=null;
//        BufferedOutputStream  bos=null;
//        try{
//            String filename=filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
//            response.setContentType(returnFileType(filename.substring(filename.lastIndexOf(".")).toUpperCase()));
//            response.setCharacterEncoding("gbk");
//            response.setHeader("Content-Disposition","filename="+new String(filename.getBytes("gbk"),"iso8859-1"));
//            bis =new BufferedInputStream(new FileInputStream(filePath));
//            bos=new BufferedOutputStream(response.getOutputStream());
//            byte[] buff = new byte[2048];
//            int bytesread;
//            while(-1 != (bytesread = bis.read(buff, 0, buff.length))) {
//                bos.write(buff,0,bytesread);
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally {
//            if (bis != null)
//                try {
//                    bis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            if (bos != null)
//                try {
//                    bos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//        }
//    }
}
