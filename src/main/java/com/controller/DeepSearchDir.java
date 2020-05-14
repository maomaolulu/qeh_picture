package com.controller;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class DeepSearchDir {

    //色差范围0~255
    public static int color_range = 210;




    /**
     * @param args
     */
    public static void main(String[] args) {
        File dir = new File("C:\\Users\\dell\\Pictures\\访苏\\111.jpg");
        System.out.println(dir.getParent());
        /*File dir = new File("C:\\Users\\dell\\Pictures\\访苏");
        String targetPath = "C:\\Users\\dell\\Pictures\\0514\\";
        int count = listDir(dir,targetPath,1);

        //下边的代码加图片水印，不需要的话直接注释掉
        System.out.println(count);
        File tarDir = new File(targetPath);
        File tarFiles[] = tarDir.listFiles();
        if(tarFiles.length == count){

        }*/
    }




    /**
     * 初始图片存放的文件夹
     * @param dir 源文件文件夹
     * @param targetPath 目标文件夹
     * @param level 遍历层级
     */
    public static int listDir(File dir,String targetPath,int height,int width,int level) {
        File files[]=dir.listFiles();
        level++;
        for(File f:files){
            if(f.isDirectory()){
                listDir(f,targetPath,height, width,level);
            }
            else {
                scale(f,targetPath+f.getName(),height, width, true);
            }
        }
        return files.length;
    }

    /**
     * 缩放图片方法
     * @param srcImageFile 要缩放的图片路径
     * @param result 缩放后的图片路径
     * @param height 目标高度像素
     * @param width  目标宽度像素  
     * @param bb     是否补白
     */
    public final static void scale(File srcImageFile, String result, int height, int width, boolean bb) {
        try {
            double ratio = 0.0; // 缩放比例
            File f =srcImageFile;
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);//bi.SCALE_SMOOTH  选择图像平滑度比缩放速度具有更高优先级的图像缩放算法。
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                double   ratioHeight = (new Integer(height)).doubleValue()/ bi.getHeight();
                double   ratioWhidth = (new Integer(width)).doubleValue()/ bi.getWidth();
                if(ratioHeight>ratioWhidth){
                    ratio= ratioHeight;
                }else{
                    ratio= ratioWhidth;
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform//仿射转换
                        .getScaleInstance(ratio, ratio), null);//返回表示剪切变换的变换
                itemp = op.filter(bi, null);//转换源 BufferedImage 并将结果存储在目标 BufferedImage 中。
            }
            if (bb) {//补白
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);//构造一个类型为预定义图像类型之一的 BufferedImage。
                Graphics2D g = image.createGraphics();//创建一个 Graphics2D，可以将它绘制到此 BufferedImage 中。
                g.setColor(Color.white);//控制颜色
                g.fillRect(0, 0, width, height);// 使用 Graphics2D 上下文的设置，填充 Shape 的内部区域。
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                g.dispose();
                itemp = image;
            }
            System.out.println(result);
            File outFile=new File(result);
            if  (!outFile .exists() && !outFile .isDirectory())
            {
                outFile .mkdir();
                ImageIO.write((BufferedImage) itemp, "JPEG",outFile);

            } else
            {
                ImageIO.write((BufferedImage) itemp, "JPEG",outFile);
            }
            //输出压缩图片

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toumingPicture(String sourceFileStr) throws Exception{
        File sourceFile = new File(sourceFileStr);
        BufferedImage image = ImageIO.read(sourceFile);
        // 高度和宽度
        int height = image.getHeight();
        int width = image.getWidth();

        // 生产背景透明和内容透明的图片
        ImageIcon imageIcon = new ImageIcon(image);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics(); // 获取画笔
        g2D.drawImage(imageIcon.getImage(), 0, 0, null); // 绘制Image的图片，使用了imageIcon.getImage()，目的就是得到image,直接使用image就可以的

        int alpha = 0; // 图片透明度
        // 外层遍历是Y轴的像素
        for (int y = bufferedImage.getMinY(); y < bufferedImage.getHeight(); y++) {
            // 内层遍历是X轴的像素
            for (int x = bufferedImage.getMinX(); x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                // 对当前颜色判断是否在指定区间内
                if (colorInRange(rgb)){
                    alpha = 0;
                }else{
                    // 设置为不透明
                    alpha = 255;
                }
                // #AARRGGBB 最前两位为透明度
                rgb = (alpha << 24) | (rgb & 0x00ffffff);
                bufferedImage.setRGB(x, y, rgb);
            }
        }
        // 绘制设置了RGB的新图片,这一步感觉不用也可以只是透明地方的深浅有变化而已，就像蒙了两层的感觉
        g2D.drawImage(bufferedImage, 0, 0, null);
        // 生成图片为PNG
        ImageIO.write(bufferedImage, "png", new File(sourceFile.getParent() + "\\touming.png"));
    }

    // 判断是背景还是内容
    public static boolean colorInRange(int color) {
        int red = (color & 0xff0000) >> 16;// 获取color(RGB)中R位
        int green = (color & 0x00ff00) >> 8;// 获取color(RGB)中G位
        int blue = (color & 0x0000ff);// 获取color(RGB)中B位
        // 通过RGB三分量来判断当前颜色是否在指定的颜色区间内
        if (red >= color_range && green >= color_range && blue >= color_range){
            return true;
        };
        return false;
    }


    }
