package com.controller;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class test {
    public static void main(String[] args) {
        File file1 = new File("C:\\Users\\dell\\Pictures\\微信图片_20191112124718.png");
        File file2 = new File("C:\\Users\\dell\\Pictures\\111.png");
        ImagesUtils.pressImage(file1,file2,470,495,(float)1.0);
//        System.out.println(1 % 5 + "");
//        //test.generateImg();
//        test.pressImage2();
 //       System.out.println(ImagesUtils.photo.size());
        //test.pressText( "九丰", "宋体", Font.PLAIN, 11, 74, 167, (float) 1.0);
    /*    String val = "1008672521-1008672580.jpg";
        String val2 = val.split("\\.")[0].split("-")[0];
        String val3 = val2.split("-")[0];
        System.out.println(val3);
*/

    }

    /**
     * 超级晚报生成
     */
    public static void generateImg() {
        //生成520证书
        String path = "upload/ad";
        String filename = "/jjr.jpg";
        File file = new File("/Users/xujianbin/ideaProjects/erweima/target/erweima/upload" + filename);
//        ImagesUtils.copyFile(new File(request.getRealPath("/")+"wx/act/ad/images/bg.png"), file);
        ImagesUtils.copyFile(new File("/Users/xujianbin/ideaProjects/erweima/target/erweima/images/1.jpg"), file);
        //二维码
        try {

            //x = 735 + 868 = 1603 + 868 = 2471 + 868 = 3339 + 868 = 4207
            //x = 148 + 175 = 323  150 +178 =
            //y = 475 + 510 = 985
            //y = 94 + 103 = 196
            //水印二维码
            String qrcodeImg = "/Users/xujianbin/ideaProjects/erweima/target/erweima/upload/sss/20180125204554690.png";
            ImagesUtils.pressImage(file, new File(qrcodeImg), 150, 95, 77, (float) 1.0);

            //水印文字
            Color color0 = new Color(Integer.decode("#000000"));
            ImagesUtils.pressText(file, "九丰", "宋体", Font.PLAIN, 11, color0, 74, 167, (float) 1.0);
            System.out.println("true");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("false");
        }
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
    public final static void pressImage() {
        try {
            File file = new File("/Users/xujianbin/ideaProjects/erweima/target/erweima/upload/qsy1.jpg");
//        ImagesUtils.copyFile(new File(request.getRealPath("/")+"wx/act/ad/images/bg.png"), file);
            ImagesUtils.copyFile(new File("/Users/xujianbin/ideaProjects/erweima/target/erweima/images/1.jpg"), file);

            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);

            int j = 0;
            for (int i = 0; i < 60; i++) {
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
                    y = y+103*j -j/2;

                    clearX = clearX;
                    clearY = clearY + 102 * j+j;

                    recX = recX;
                    recY = recY + 102 * j+j;

                    textX = textX;
                    textY = textY + 102 * j+j;
                    j++;
                } else {
                    x = x + 175 * (i % 5);
                    y = y+103*(j-1)-j/2;

                    clearX = clearX + 175 * (i % 5);
                    clearY = clearY + 102 * (j - 1)+j;

                    recX = recX + 176 * (i % 5);
                    recY = recY + 102 * (j - 1)+j;

                    textX = textX + 176 * (i % 5);
                    textY = textY + 102 * (j - 1)+j;
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

                int width_l = 10 * ImagesUtils.getLength("九丰") + 6;
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
                g.drawString("九丰", textX+2, textY+2);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                //水印二维码
                String qrcodeImg = "/Users/xujianbin/ideaProjects/erweima/target/erweima/upload/sss/20180125204554690.png";
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
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public final static void pressImage2() {
        try {
            File file = new File("/Users/xujianbin/ideaProjects/erweima/target/erweima/upload/qsy1.jpg");
//        ImagesUtils.copyFile(new File(request.getRealPath("/")+"wx/act/ad/images/bg.png"), file);
            ImagesUtils.copyFile(new File("/Users/xujianbin/ideaProjects/erweima/target/erweima/images/2.jpg"), file);

            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);

            int j = 0;
            for (int i = 0; i < 60; i++) {
                int clearX = 170;
                int clearY = 850;
                int recX = 410;
                int recY = 860;
                int textX = 410;
                int textY = 907;
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
                g.fillRect(clearX, clearY, 405, 95);  //35+175 = 210 +175=385
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                int width_l = 46 * ImagesUtils.getLength("九丰") + 6;
                /* 画圆角矩形 */
                g.setColor(Color.black);
                RoundRectangle2D rect = new RoundRectangle2D.Double(recX, recY, width_l, 75,
                        20, 20);
                g.draw(rect);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                //水印字
                g.setFont(new Font("宋体", Font.PLAIN, 46));
                g.setColor(Color.black);
                // 80 + 183
                g.drawString("九丰", textX+5, textY+5);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));

                //水印二维码
                String qrcodeImg = "/Users/xujianbin/ideaProjects/erweima/target/erweima/upload/sss/20180125204554690.png";
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
