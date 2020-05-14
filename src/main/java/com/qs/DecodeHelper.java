package com.qs;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * 解析二维码
 * @author Administrator
 *
 */
public class DecodeHelper {

    public static void main(String[] args) throws Exception {

            //取得根目录路径
            //当前目录的上级目录路径
            String parentPath=DecodeHelper.class.getResource("../").getFile().toString();
            String rootPath=DecodeHelper.class.getResource("/").getPath();
            String baseDocPath = "upload/erweima/";
            File filew = new File("/Users/xujianbin/ideaProjects/erweima/target/erweima/"+baseDocPath);
            List<File> fileList = new ArrayList<File>();
            File[] files = filew.listFiles();// 获取目录下的所有文件或文件夹
            if (files == null) {// 如果目录为空，直接退出
                System.out.println("没有图片");
                return;
            }
            // 遍历，目录下的所有文件
            for (File f : files) {
                if (f.isFile()) {
                    fileList.add(f);
                }
            }

            for (File f1 : fileList) {
                System.out.println(f1.getName());
                String url = f1.getPath();
                try {
                MultiFormatReader formatReader = new MultiFormatReader();
//            String filePath = "/Users/xujianbin/ideaProjects/erweima/target/erweima/upload/erweima/1008710480.png"; //new.png
                File file = new File(url);

                BufferedImage image = ImageIO.read(file);

                LuminanceSource source = new BufferedImageLuminanceSource(image);

                Binarizer binarizer = new HybridBinarizer(source);

                BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

                Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
                hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

                Result result = formatReader.decode(binaryBitmap,hints);

                System.out.println("result = "+ result.toString());
                System.out.println("resultFormat = "+ result.getBarcodeFormat());
                System.out.println("resultText = "+ result.getText());
                System.out.println("--------------------------------------");
            }catch (Exception e) {
                    e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) throws Exception {
//        try {
//
//                MultiFormatReader formatReader = new MultiFormatReader();
//            String filePath = "/Users/xujianbin/ideaProjects/erweima/target/erweima/upload/image/1008910181-1008910240.jpg"; //new.png
//                File file = new File(filePath);
//
//                BufferedImage image = ImageIO.read(file);
//
//                LuminanceSource source = new BufferedImageLuminanceSource(image);
//
//                Binarizer binarizer = new HybridBinarizer(source);
//
//                BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
//
//                Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
//                hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
//
//                Result result = formatReader.decode(binaryBitmap,hints);
//
//                System.out.println("result = "+ result.toString());
//                System.out.println("resultFormat = "+ result.getBarcodeFormat());
//                System.out.println("resultText = "+ result.getText());
//                System.out.println("--------------------------------------");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
