package com.controller;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 图片工具类， 图片水印，文字水印，缩放，补白等
 * @author Carl He
 */
public  class ImagesUtils {
    /**图片格式：JPG*/
    private static final String PICTRUE_FORMATE_JPG = "jpg";

    public static List<HashMap<String,Object>> photo = new ArrayList<>();
    /**
     * 添加图片水印
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg
     * @param waterImg  水印图片路径，如：C://myPictrue//logo.png
     * @param x 水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y 水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     */
    public final static void pressImage(File file, File waterImg, int x, int y,int twidth, float alpha) {
            try {
                Image image = ImageIO.read(file);
                int width = image.getWidth(null);
                int height = image.getHeight(null);
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = bufferedImage.createGraphics();
                g.drawImage(image, 0, 0, width, height, null);
            
                Image waterImage = ImageIO.read(waterImg);    // 水印文件
                int width_1 = /*waterImage.getWidth(null);*/twidth;
                int height_1 = /*waterImage.getHeight(null);*/twidth;
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
                
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
                g.dispose();
                ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public final static void pressImage2(File file, File waterImg, int x, int y,int twidth,int theight, float alpha) {
        try {
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);
        
            Image waterImage = ImageIO.read(waterImg);    // 水印文件
            int width_1 = /*waterImage.getWidth(null);*/twidth;
            int height_1 = /*waterImage.getHeight(null);*/theight;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            
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
            g.dispose();
            ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
}
    /**
     * 添加文字水印
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg
     * @param pressText 水印文字， 如：中国证券网
     * @param fontName 字体名称，    如：宋体
     * @param fontStyle 字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC)
     * @param fontSize 字体大小，单位为像素
     * @param color 字体颜色
     * @param x 水印文字距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y 水印文字距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
*/
    public static void pressText(File file, String pressText, String fontName, int fontStyle, int fontSize, Color color, int x, int y, float alpha) {
        try {
            //File file = new File(targetImg);
            
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setColor(color);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            
            int width_1 = fontSize * getLength(pressText);
            int height_1 = fontSize;
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
            g.drawString(pressText, x, y + height_1);
            g.dispose();
            ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符
     * @param text
     * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
*/
    public static int getLength(String text) {
        int textLength = text.length();
        int length = textLength;
        for (int i = 0; i < textLength; i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
                length++;
            }
        }
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;
    }

    /**
     * 图片缩放
     * @param filePath 图片路径
     * @param height 高度
     * @param width 宽度
     * @param bb 比例不对时是否需要补白
     */
    public static void resize(String filePath, int height, int width, boolean bb) {
        try {
            double ratio = 0; //缩放比例    
            File f = new File(filePath);   
            BufferedImage bi = ImageIO.read(f);   
            Image itemp = bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);   
            //计算比例   
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {   
                if (bi.getHeight() > bi.getWidth()) {   
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();   
                } else {   
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();   
                }   
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);   
                itemp = op.filter(bi, null);   
            }   
            if (bb) {   
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);   
                Graphics2D g = image.createGraphics();   
                g.setColor(Color.white);   
                g.fillRect(0, 0, width, height);   
                if (width == itemp.getWidth(null))   
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);   
                else  
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);   
                g.dispose();   
                itemp = image;   
            }
            ImageIO.write((BufferedImage) itemp, "jpg", f);   
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //pressImage("C://pic//jpg", "C://pic//test.gif", 5000, 5000, 0f);
        /*pressText(new File("D://scene.jpg"), "旺仔之印", "宋体", Font.BOLD, 20, Color.BLACK, 170, 28, 0.8f);
        pressImage("D://scene.jpg", "D://qr.png", 15, 15,80, (float) 1.0);
        pressImage("D://scene.jpg", "D://qr.png", 130, 520,230, (float) 1.0);*/
    	download("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQE/8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL1R6aVNiLUxrSkNsZElqUnpIeFFtAAIE0ke2VQMEAAAAAA==", "xxx.jpg", "D://");
    }
    
    public static String download(String urlString, String filename,String savePath){  
        try {
        	// 构造URL  
            URL url = new URL(urlString);  
            // 打开连接  
            URLConnection con = url.openConnection();  
            //设置请求超时为5s  
            con.setConnectTimeout(5*1000);  
            // 输入流  
            InputStream is = con.getInputStream();  
          
            // 1K的数据缓冲  
            byte[] bs = new byte[1024];  
            // 读取到的数据长度  
            int len;  
            // 输出的文件流  
           File sf=new File(savePath);  
           if(!sf.exists()){  
               sf.mkdirs();  
           }  
           OutputStream os = new FileOutputStream(savePath+filename);  
            // 开始读取  
            while ((len = is.read(bs)) != -1) {  
              os.write(bs, 0, len);  
            }  
            // 完毕，关闭所有链接  
            os.close();  
            is.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
        return savePath+filename;
    }   
    
 // 复制名片图片
    public static void copyFile(File sourceFile, File targetFile) {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
            // 关闭流
            try {
            	if (inBuff != null)
                    inBuff.close();
                if (outBuff != null)
                    outBuff.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
        }
    } 
    
    /*public static void Qrcode(File f,String text) {
    	try {
    		int width = 250;   
            int height = 250;   
            String format = "png";   
            Hashtable hints= new Hashtable();   
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");   
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height,hints);   
            MartixToImageWriter.writeToFile(bitMatrix, format, f);   
		} catch (Exception e) {
			e.printStackTrace();
		}
    }*/
    
    /**
     * 添加图片水印
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg
     * @param waterImg  水印图片路径，如：C://myPictrue//logo.png
     * @param x 水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y 水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @param rotate 旋转角度
     */
	public final static void pressImageRotate(File file, File waterImg, int x,
			int y, int twidth, float alpha, double rotate) {
		try {
			Image image = ImageIO.read(file);
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(image, 0, 0, width, height, null);
			Image waterImage = ImageIO.read(waterImg); // 水印文件
			int width_1 = twidth;
			int height_1 = twidth;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			int widthDiff = width - width_1;
			int heightDiff = height - height_1;
			if (x < 0) {
				x = widthDiff / 2;
			} else if (x > widthDiff) {
				x = widthDiff;
			}
			if (y < 0) {
				y = heightDiff / 2;
			} else if (y > heightDiff) {
				y = heightDiff;
			}
			g.rotate(Math.toRadians(rotate), x, y + height_1);
			g.drawImage(waterImage, x, y, width_1, height_1, null); // 水印文件结束
			g.dispose();
			ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 添加文字水印
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg
     * @param pressText 水印文字， 如：中国证券网
     * @param fontName 字体名称，    如：宋体
     * @param fontStyle 字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC)
     * @param fontSize 字体大小，单位为像素
     * @param color 字体颜色
     * @param x 水印文字距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y 水印文字距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     * @param rotate 旋转角度
     */
	public static void pressTextRotate(File file, String pressText,
			String fontName, int fontStyle, int fontSize, Color color, int x,
			int y, float alpha, double rotate) {
		try {
			Image image = ImageIO.read(file);
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(image, 0, 0, width, height, null);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setColor(color);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			int width_1 = fontSize * getLength(pressText);
			int height_1 = fontSize;
			int widthDiff = width - width_1;
			int heightDiff = height - height_1;
			if (x < 0) {
				x = widthDiff / 2;
			} else if (x > widthDiff) {
				x = widthDiff;
			}
			if (y < 0) {
				y = heightDiff / 2;
			} else if (y > heightDiff) {
				y = heightDiff;
			}
			g.rotate(Math.toRadians(rotate), x, y + height_1);
			g.drawString(pressText, x, y + height_1);
			g.dispose();
			ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public final static void pressImage(File file, File waterImg, int x, int y, float alpha) {
		try {
			Image image = ImageIO.read(file);
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			BufferedImage bufferedImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(image, 0, 0, width, height, null);

			Image waterImage = ImageIO.read(waterImg); // 水印文件
			int width_1 = waterImage.getWidth(null);
			int height_1 = waterImage.getHeight(null); 
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
					alpha));

			int widthDiff = width - width_1;
			int heightDiff = height - height_1;
			if (x < 0) {
				x = widthDiff / 2;
			} else if (x > widthDiff) {
				x = widthDiff;
			}
			if (y < 0) {
				y = heightDiff / 2;
			} else if (y > heightDiff) {
				y = heightDiff;
			}
			g.drawImage(waterImage, x, y, width_1, height_1, null); // 水印文件结束
			g.dispose();
			ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}