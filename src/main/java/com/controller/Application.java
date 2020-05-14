package com.controller;

import java.io.File;

/**
 * @ClassName Application
 * @Description
 * @Author maoLy
 * @Date 2020/5/14
 **/
public class Application {

    //源图片所在文件夹
    public static String source_dir = "C:\\Users\\dell\\Pictures\\访苏";
    //目标图片所在文件夹
    public static String target_path = "C:\\Users\\dell\\Pictures\\0514\\";

    //生成透明图片时，图片的源路径
    public static String touming_path = "";

    //加图片水印 ,源图片
    public static String press_image_path = "";
    //水印图片
    public static String sy_image_path = "C:\\Users\\dell\\Pictures\\111.png";


    //设置图片裁剪尺寸
    public static int height = 400;
    public static int width = 500;



    public static void main(String[] args){
           /*1.设置图片为统一尺寸*/
        int count = changePictureSize();

          /*2.需要加图片水印时打开*/
        pressImage(count);


    }

    /**
     * 设置图片为统一尺寸
     */
    public static int changePictureSize(){
        File sourceDir = new File(source_dir);
        return DeepSearchDir.listDir(sourceDir,target_path,height,width,1);
    }

    /**
     * 生成透明图片
     * @throws Exception
     */
    public static void toumingPicture(int count) throws Exception{
        DeepSearchDir.toumingPicture(touming_path);
    }

    /**
     * 加图片水印
     */
    public static void pressImage(int count){
        File tarDir = new File(target_path);
        File tarFiles[] = tarDir.listFiles();
        if(tarFiles.length == count){
            for(File f:tarFiles){
                System.out.println("加图片水印：图片路径："+f.getPath());
                File file2 = new File(sy_image_path);
                ImagesUtils.pressImage(f,file2,470,495,(float)1.0);
            }
        }

    }
}
