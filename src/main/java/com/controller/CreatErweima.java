package com.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.qs.AjaxJson;
import com.qs.MatrixToImageWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Controller
@RequestMapping("wx")
public class CreatErweima {

    @RequestMapping("center")
    public String center(HttpServletRequest request) throws Exception{
        return "center";
    }

    @RequestMapping("upload")
    public String upload(HttpServletRequest request) throws Exception{
        ImagesUtils.photo.clear();
        return "uploadmuil";
    }

    /**
     * 生成二维码
     *
     * @throws WriterException
     * @throws IOException
     */
    @RequestMapping("testEncode")
    @ResponseBody
    public AjaxJson testEncode(HttpServletRequest request, @RequestParam("qrCode") String qrCode) throws Exception {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setMsg(creatEncode(request,qrCode));
        return ajaxJson;
    }

    /**
     * 生成二维码
     *
     * @throws WriterException
     * @throws IOException
     */
    @RequestMapping("testEncodeList")
    @ResponseBody
    public AjaxJson testEncodeList(HttpServletRequest request,@RequestParam("url") String url,
                                   @RequestParam("size") Integer size,
                                   @RequestParam("name") String name,
                                   @RequestParam("befor") String befor,
                                   @RequestParam("after") String after) throws Exception {
        AjaxJson ajaxJson = new AjaxJson();
        int i = Integer.parseInt(befor);
        int j = Integer.parseInt(after);
        List<String> list = new ArrayList<>();
        for (int b = i;b<=j;b++){
            String fileName = creatEncode(request,String.valueOf(b));
            list.add(fileName);
        }
        String fileName = pressImage2(size,url,request,list,name);
        ajaxJson.setMsg(fileName);
        return ajaxJson;
    }

    /**
     * 生成二维码
     *
     * @throws WriterException
     * @throws IOException
     */
    @RequestMapping("createEncodeList")
    @ResponseBody
    public AjaxJson createEncodeList(HttpServletRequest request,
                                   @RequestParam("name") String name) throws Exception {
        AjaxJson ajaxJson = new AjaxJson();
        List<HashMap<String,Object>> photos = ImagesUtils.photo;
        for (HashMap<String,Object> hashMap : photos){
            String val = hashMap.get("fileName").toString();
            String url = hashMap.get("url").toString();
            int i = Integer.parseInt(val.split("\\.")[0].split("-")[0]);
            int j = Integer.parseInt(val.split("\\.")[0].split("-")[1]);
            List<String> list = new ArrayList<>();
            for (int b = i;b<=j;b++){
//                System.out.println(b);
                String fileName = creatEncode(request,String.valueOf(b));
                list.add(fileName);
//                System.out.println(fileName);
            }
            String fileName = pressImage2(url,request,list,name);
            ajaxJson.setMsg("生成二维码成功-请按步骤继续操作");
        }
        return ajaxJson;
    }

    /**
     * 生成二维码
     *
     * @throws WriterException
     * @throws IOException
     */
    @RequestMapping("createEncodeList2")
    @ResponseBody
    public AjaxJson createEncodeList2(HttpServletRequest request,
                                     @RequestParam("name") String name) throws Exception {
        AjaxJson ajaxJson = new AjaxJson();
        String baseDocPath = "upload/image/";
        File file = new File(request.getRealPath("/")+baseDocPath);
        List<File> fileList = new ArrayList<File>();
        File[] files = file.listFiles();// 获取目录下的所有文件或文件夹
        if (files == null) {// 如果目录为空，直接退出
            ajaxJson.setMsg("没有图片");
            return ajaxJson;
        }
        // 遍历，目录下的所有文件
        for (File f : files) {
            if (f.isFile()) {
                fileList.add(f);
            }
        }
        List<LinkedHashMap<String,Object>> photos = new ArrayList<>();
        String pathUrl = "upload/image/";
        for (File f1 : fileList) {
            System.out.println(f1.getName());
            String url = pathUrl+f1.getName();
            LinkedHashMap<String,Object> hashMap = new LinkedHashMap<>();
            hashMap.put("url",url);
            hashMap.put("fileName",f1.getName());
            photos.add(hashMap);
        }

        for (LinkedHashMap<String,Object> hashMap : photos){
            String val = hashMap.get("fileName").toString();
            String url = hashMap.get("url").toString();
            int i = Integer.parseInt(val.split("\\.")[0].split("-")[0]);
            int j = Integer.parseInt(val.split("\\.")[0].split("-")[1]);
            List<String> list = new ArrayList<>();
            for (int b = i;b<=j;b++){
                String fileName = creatEncode(request,String.valueOf(b));
                list.add(fileName);
                System.out.println(fileName);
            }
            String fileName = pressImage2(url,request,list,name);
        }
        ajaxJson.setMsg("生成成功，请下载upload/image目录，再把需要生成的二维码放在image目录下，删除erweima下的所有文件，刷新页面重新生成！");
        return ajaxJson;
    }

    public synchronized String creatEncode(HttpServletRequest request, String qrCode) throws WriterException, IOException{
        Date date=new Date();
//        String fileName = new SimpleDateFormat(
//                "yyyyMMddHHmmssSSS").format(date)+".png";
        String baseDocPath = "upload/erweima/";
        String fileName = qrCode+".png";

        File ff = new File(request.getRealPath("/")+baseDocPath+fileName);
        if(!ff.exists()) {
            ff.mkdir();
        }
        qrCode = "http://www.gdranqi.com/qr/"+qrCode;
        int width = 80; // 图像宽度
        int height = 80; // 图像高度
        String format = "png";// 图像类型
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1); //设置二维码空白边框的大小 1-4，1是最小 4是默认的国标
        BitMatrix bitMatrix = new MultiFormatWriter().encode(qrCode,
                BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵
        bitMatrix = deleteWhite(bitMatrix);
        MatrixToImageWriter.writeToFile(bitMatrix, format, ff);// 输出图像
//        System.out.println("输出成功.");
        return baseDocPath+fileName;
    }

    private BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    /**
     * 二维码生成             //x = 735 + 868 = 1603 + 868 = 2471 + 868 = 3339 + 868 = 4207
     /                      /x = 148 + 175 = 323
     /                      /y = 475 + 510 = 985
     /                      /y = 94 + 103 = 985
     /                      /375   77
     */
    public String generateImg(List<String> names, HttpServletRequest request) {
        //生成520证书
        String path = "upload/";
        Date date=new Date();
        String fileName = new SimpleDateFormat(
                "yyyyMMddHHmmssSSS").format(date)+".jpg";
        File file = new File(request.getRealPath("/")+path+fileName);
        ImagesUtils.copyFile(new File(request.getRealPath("/")+"images/1.jpg"), file);
        boolean flag = false;
        int  j = 0;
        for (int i = 0; i<names.size(); i++){
//            synchronized (this) {
                try {
                    if (flag) {
                        file = new File(request.getRealPath("/") + path+fileName);
                    }
                    String qrcodeImg = request.getRealPath("/") + names.get(i);
                    int x = 148;
                    int y = 94;
                    if (i % 5 == 0) {
                        x = 148;
                        y = 94+103*j;
                        j++;
                    } else {
                        x = 148 + 175 * (i % 5);
                        y = 94+103*(j-1);
                    }
                    System.out.println(j+":j");
                    System.out.println(x+"---"+y);
                    ImagesUtils.pressImage(file, new File(qrcodeImg), x, y, 77, (float) 1.0);
                    System.out.println("true");
                    flag = true;
                } catch (Exception e) {
                    System.out.println("false");
                    e.printStackTrace();
                }
//            }
        }
        return path+fileName;
    }

    /**
     * 添加图片水印
     *
     */
    public static String pressImage(HttpServletRequest request,List<String> names,String company) {
        try {
            //生成520证书
            String path = "upload/";
            Date date=new Date();
            String fileName = new SimpleDateFormat(
                    "yyyyMMddHHmmssSSS").format(date)+".jpg";
            File file = new File(request.getRealPath("/")+path+fileName);
            ImagesUtils.copyFile(new File(request.getRealPath("/")+"images/1.jpg"), file);

            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);

            int j = 0;
            for (int i = 0; i < names.size(); i++) {
                int clearX = 35;
                int clearY = 172;
                int recX = 77;
                int recY = 172;
                int textX = 80;
                int textY = 183;
                int x = 150;
                int y = 95;
                if (i % 5 == 0) {
                    x = x;
                    y = y+103*j-j/2;

                    clearX = clearX;
                    clearY = clearY + 102 * j+j/2;

                    recX = recX;
                    recY = recY + 102 * j+j/2;

                    textX = textX;
                    textY = textY + 102 * j+j/2;
                    j++;
                } else {
                    x = x + 175 * (i % 5);
                    y = y+103*(j-1)-j/2;

                    clearX = clearX + 175 * (i % 5);
                    clearY = clearY + 102 * (j - 1)+j/2;

                    recX = recX + 176 * (i % 5);
                    recY = recY + 102 * (j - 1)+j/2;

                    textX = textX + 176 * (i % 5);
                    textY = textY + 102 * (j - 1)+j/2;
                }

                if (i%5 == 4){
                    //去除水印
                    g.setColor(Color.white);
                    g.fillRect(910, clearY, 20, 20);  //35+175 = 210 +175=385
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
                }
                //去除水印
                g.setColor(Color.white);
                g.fillRect(clearX, clearY, 82, 20);  //35+175 = 210 +175=385
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                int width_l = 10 * ImagesUtils.getLength(company) + 6;
                /* 画圆角矩形 */
                g.setColor(Color.black);
                RoundRectangle2D rect = new RoundRectangle2D.Double(recX, recY, width_l, 16,
                        10, 10);
                g.draw(rect);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                //水印字
                g.setFont(new Font("宋体", Font.PLAIN, 10));
                g.setColor(Color.black);
                // 80 + 183
                g.drawString(company, textX+2, textY+2);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                //水印二维码
                String qrcodeImg = request.getRealPath("/") + names.get(i);
                Image waterImage = ImageIO.read(new File(qrcodeImg));    // 水印文件
                int width_1 = 77;
                int height_1 = 77;
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                int widthDiff = width - width_1;
                int heightDiff = height - height_1;
                if(x < 0){
                    x = widthDiff / 2;
                }else if(x > widthDiff){
                    x = widthDiff;
                }
                if(y < 0){
                    y = heightDiff / 2;
                }else if(y > heightDiff){
                    y = heightDiff;
                }
                g.drawImage(waterImage, x, y, width_1, height_1, null); // 水印文件结束

            }
            g.dispose();
            ImageIO.write(bufferedImage, "jpg", file);
            return path+fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加图片水印
     *
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg
     * @param waterImg  水印图片路径，如：C://myPictrue//logo.png
     * @param x         水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y         水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha     透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     */
//    public final static void pressImage(File waterImg, int x, int y,int twidth, float alpha) {
    public static String pressImage2(Integer textSize,String url,HttpServletRequest request,List<String> names,String company) {
        try {
            //生成520证书
//            String path = "upload/";
//            Date date=new Date();
//            String fileName = new SimpleDateFormat(
//                    "yyyyMMddHHmmssSSS").format(date)+".jpg";
            File file = new File(request.getRealPath("/")+url);
//            ImagesUtils.copyFile(new File(request.getRealPath("/")+"images/2.jpg"), file);

            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);

            int j = 0;
            for (int i = 0; i < names.size(); i++) {
                int clearX = 168;
                int clearY = 850;
                int recX = 400;
                int recY = 864;
                int textX = 400;
                int textY = 910;
                int x = 760;
                int y = 475;
                if (i % 5 == 0) {
                    x = x;
                    y = y+510*j -j/2;

                    clearX = clearX;
                    clearY = clearY + 510 * j-j;

                    recX = recX;
                    recY = recY + 510 * j-j;

                    textX = textX;
                    textY = textY + 510 * j-j;
                    j++;
                } else {
                    x = x + 868 * (i % 5);
                    y = y+510*(j-1)-j/2;

                    clearX = clearX + 870 * (i % 5);
                    clearY = clearY + 510 * (j - 1)-j;

                    recX = recX + 865 * (i % 5);
                    recY = recY + 510 * (j - 1)-j;

                    textX = textX + 865 * (i % 5);
                    textY = textY + 510 * (j - 1)-j;
                }

                if (i%5 == 4){
                    //去除水印
                    g.setColor(Color.white);
                    g.fillRect(4515, clearY, 100, 100);  //35+175 = 210 +175=385
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
                }
                //去除水印
                g.setColor(Color.white);
                g.fillRect(clearX, clearY, 406, 96);  //35+175 = 210 +175=385
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                int width_l = 46 * ImagesUtils.getLength(company) + 25;
                /* 画圆角矩形 */
                g.setColor(Color.black);
                RoundRectangle2D rect = new RoundRectangle2D.Double(recX, recY, width_l, 75,
                        20, 20);
                g.setStroke(new BasicStroke(6.0f));//关键,设置画笔的宽度.  越大,边框越粗
                g.draw(rect);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                //水印字
                g.setFont(new Font("黑体", Font.BOLD, 46));
                g.setColor(Color.black);
                // 80 + 183
                g.drawString(company, textX+15, textY+5);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                //水印二维码
                String qrcodeImg = request.getRealPath("/") + names.get(i);
                Image waterImage = ImageIO.read(new File(qrcodeImg));    // 水印文件
                int width_1 = 375;
                int height_1 = 375;
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                int widthDiff = width - width_1;
                int heightDiff = height - height_1;
                if(x < 0){
                    x = widthDiff / 2;
                }else if(x > widthDiff){
                    x = widthDiff;
                }
                if(y < 0){
                    y = heightDiff / 2;
                }else if(y > heightDiff){
                    y = heightDiff;
                }
                g.drawImage(waterImage, x, y, width_1, height_1, null); // 水印文件结束
            }
            g.dispose();
            ImageIO.write(bufferedImage, "jpg", file);
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加图片水印
     *
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg
     * @param waterImg  水印图片路径，如：C://myPictrue//logo.png
     * @param x         水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y         水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha     透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     */
//    public final static void pressImage(File waterImg, int x, int y,int twidth, float alpha) {
    public synchronized String pressImage2(String url,HttpServletRequest request,List<String> names,String company) {
        try {
            //生成520证书
//            String path = "upload/";
//            Date date=new Date();
//            String fileName = new SimpleDateFormat(
//                    "yyyyMMddHHmmssSSS").format(date)+".jpg";
            File file = new File(request.getRealPath("/")+url);
//            ImagesUtils.copyFile(new File(request.getRealPath("/")+"images/2.jpg"), file);

            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);

            int j = 0;
            for (int i = 0; i < names.size(); i++) {
                int clearX = 168;
                int clearY = 850;
//                int recX = 400;
                int recX = 375;
//                int recY = 864;
                int recY = 860;
//                int textX = 400;
                int textX = 375;
//                int textY = 910;
                int textY = 906;
                int x = 760;
                int y = 475;
                if (i % 5 == 0) {
                    x = x;
                    y = y+510*j -j/2;

                    clearX = clearX;
                    clearY = clearY + 510 * j-j;

                    recX = recX;
                    recY = recY + 510 * j-j;

                    textX = textX;
                    textY = textY + 510 * j-j;
                    j++;
                } else {
                    x = x + 868 * (i % 5);
                    y = y+510*(j-1)-j/2;

                    clearX = clearX + 870 * (i % 5);
                    clearY = clearY + 510 * (j - 1)-j;

                    recX = recX + 865 * (i % 5);
                    recY = recY + 510 * (j - 1)-j;

                    textX = textX + 865 * (i % 5);
                    textY = textY + 510 * (j - 1)-j;
                }

                if (i%5 == 4){
                    //去除水印
                    g.setColor(Color.white);
                    g.fillRect(4515, clearY, 100, 100);  //35+175 = 210 +175=385
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
                }
                //去除水印
                g.setColor(Color.white);
                g.fillRect(clearX, clearY, 406, 96);  //35+175 = 210 +175=385
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                int width_l = 61 * ImagesUtils.getLength(company) + 25;
                /* 画圆角矩形 */
                g.setColor(Color.black);
                RoundRectangle2D rect = new RoundRectangle2D.Double(recX, recY, width_l, 75,
                        20, 20);
                g.setStroke(new BasicStroke(6.0f));//关键,设置画笔的宽度.  越大,边框越粗
                g.draw(rect);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                //水印字
                g.setFont(new Font("黑体", Font.BOLD, 66));
                g.setColor(Color.black);
                // 80 + 183
                g.drawString(company, textX+5, textY+15);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                //水印二维码
                String qrcodeImg = request.getRealPath("/") + names.get(i);
                System.out.println(qrcodeImg);
                Image waterImage = ImageIO.read(new File(qrcodeImg));    // 水印文件
                int width_1 = 375;
                int height_1 = 375;
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                int widthDiff = width - width_1;
                int heightDiff = height - height_1;
                if(x < 0){
                    x = widthDiff / 2;
                }else if(x > widthDiff){
                    x = widthDiff;
                }
                if(y < 0){
                    y = heightDiff / 2;
                }else if(y > heightDiff){
                    y = heightDiff;
                }
                g.drawImage(waterImage, x, y, width_1, height_1, null); // 水印文件结束
            }
            g.dispose();
            ImageIO.write(bufferedImage, "jpg", file);
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
