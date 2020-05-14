/*
package com.qs;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class QRCodeUtils {
	public static void main(String[] args) {
		qrCodeDecode(new File("/Users/xujianbin/ideaProjects/erweima/target/erweima/upload/erweima/1008710480.png"));
	}
	*/
/**
	 * 解析二维码，返回解析内容
	 *//*

	public static String qrCodeDecode(File imageFile) {
		String decodedData = null;
		QRCodeDecoder decoder = new QRCodeDecoder();
		BufferedImage image = null;
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
		try {
			decodedData = new String(decoder.decode(new J2SEImage(image)), "utf-8");
			System.out.println("Output Decoded Data is：" + decodedData);
		} catch (DecodingFailedException dfe) {
			System.out.println("Error: " + dfe.getMessage());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decodedData;
	}
}

class J2SEImage implements QRCodeImage {
	BufferedImage image;

	public J2SEImage(BufferedImage image) {
		this.image = image;
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	public int getPixel(int x, int y) {
		return image.getRGB(x, y);
	}
}*/
