package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Controller
@RequestMapping("uploadController")
public class UploadController {


    //上传文件会自动绑定到MultipartFile中
    @RequestMapping(value="/uploadMulyi",method= RequestMethod.POST)
    @ResponseBody
    public  AjaxJson uploadMulyi(HttpServletRequest request,
                         @RequestParam("file") MultipartFile file) throws Exception {
        AjaxJson ajaxJson = new AjaxJson();
        //如果文件不为空，写入上传路径
        if(!file.isEmpty()) {
            //上传文件路径
            String path = request.getServletContext().getRealPath("/");
            String fileName = new SimpleDateFormat(
                    "yyyyMMddHHmmssSSS").format(new Date());
            String pathUrl = null;
            if (FileUtils.index!=0){
                pathUrl = FileUtils.pathUrl;
            }else {
                pathUrl = "upload/main/"+fileName+"/";
                FileUtils.pathUrl = pathUrl;
                FileUtils.fileName = fileName+".zip";
            }
            FileUtils.index++;
            File root = new File(path+pathUrl);
            //判断路径是否存在，如果不存在就创建一个
            if (!root.exists()) {
                root.mkdirs();
            }
            //上传文件名
            String filename = file.getOriginalFilename();
            File filepath = new File(path,filename);
            File fileParent = filepath.getParentFile();
            //判断路径是否存在，如果不存在就创建一个
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            //将上传文件保存到一个目标文件当中
            file.transferTo(new File(path + pathUrl + filename));
            String url = pathUrl+filename;
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("url",url);
            hashMap.put("fileName",filename);
            ImagesUtils.photo.add(hashMap);
            ajaxJson.setCode(200);
            ajaxJson.setEasyFileIndex(1);
            return ajaxJson;
        } else {
            return null;
        }

    }

    /**
     * 文件下载
     * @Description:
     * @param fileName
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/download")
    public String downloadFile(@RequestParam("fileName") String fileName,
                               HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (fileName != null) {
            String realPath = request.getServletContext().getRealPath(
                    "/");
            File file = new File(realPath + fileName);
            String name = fileName.split("/")[2];
            if (file.exists()) {
                //设置响应头，控制浏览器下载该文件
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8"));
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                OutputStream os = null;
                try {
                    fis = new FileInputStream(file);
                    os = response.getOutputStream();
                    int len = 0;
                    //循环将输入流中的内容读取到缓冲区当中
                    while((len=fis.read(buffer))>0){
                        //输出缓冲区的内容到浏览器，实现文件下载
                        os.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                            os.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    response.sendRedirect("/upload.jsp");
                }
            }
        }
        return null;
    }

    /**
     * 文件下载
     * @Description:
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/download2")
    public String downloadFile2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FileUtils.index = 0;
        FileUtils.pathUrl = "";
        String fileRoot = ImagesUtils.photo.get(0).get("url").toString();
        ImagesUtils.photo.clear();
        String path = fileRoot.substring(0,fileRoot.lastIndexOf("/"));
        String fileName = FileUtils.zipFile(request.getRealPath("/")+path,request);
        if (fileName != null) {
            File file = new File(fileName);
            String name = fileName.substring(fileName.lastIndexOf("/")+1,fileName.length());
            if (file.exists()) {
                //设置响应头，控制浏览器下载该文件
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(name, "UTF-8"));
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                OutputStream os = null;
                try {
                    fis = new FileInputStream(file);
                    os = response.getOutputStream();
                    int len = 0;
                    //循环将输入流中的内容读取到缓冲区当中
                    while((len=fis.read(buffer))>0){
                        //输出缓冲区的内容到浏览器，实现文件下载
                        os.write(buffer, 0, len);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                            os.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    response.sendRedirect("/upload.jsp");
                }
            }
        }
        return null;
    }

    //上传文件会自动绑定到MultipartFile中
    @RequestMapping(value="/upload",method= RequestMethod.POST)
    public synchronized String upload(HttpServletRequest request,
                                        @RequestParam("file") MultipartFile file) throws Exception {
        //如果文件不为空，写入上传路径
        if(!file.isEmpty()) {
            //上传文件路径
            String path = request.getServletContext().getRealPath("/");
            String pathUrl = "upload/main/";
            File root = new File(path+pathUrl);
            //判断路径是否存在，如果不存在就创建一个
            if (!root.exists()) {
                root.mkdirs();
            }
            //上传文件名
            String filename = file.getOriginalFilename();
            File filepath = new File(path,filename);
            //判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            //将上传文件保存到一个目标文件当中
            file.transferTo(new File(path + pathUrl + filename));
            request.setAttribute("url",pathUrl+filename);
            return "index";
        } else {
            return "";
        }

    }
}
