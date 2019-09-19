package com.je.core.util;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import com.swetake.util.Qrcode;

public class QRCodeUtil {
	/** 
     * 生成二维码(QRCode)图片 
     * @param qrcode
     */  
    public static void createQRCode(QRCodeEntity qrcode) {  
        try {  
            Qrcode qrcodeHandler = new Qrcode();  
            if(qrcode.getWidth()<=0){
            	qrcode.setWidth(150);
            }
            if(qrcode.getHeight()<=0){
            	qrcode.setHeight(150);
            }
            if(StringUtil.isNotEmpty(qrcode.getFormat())){
            	qrcode.setFormat("png");
            }
            /* 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小 */ 
        	qrcodeHandler.setQrcodeErrorCorrect(qrcode.getQrcodeErrorCorrect());
            /* N代表数字,A代表字符a-Z,B代表其他字符 */
            qrcodeHandler.setQrcodeEncodeMode(qrcode.getQrcodeEncodeModel()); 
            /* 设置设置二维码版本，取值范围1-40，值越大尺寸越大，可存储的信息越大 */  
            qrcodeHandler.setQrcodeVersion(qrcode.getVersion());  
  
            byte[] contentBytes = qrcode.getContent().getBytes("UTF-8");
            /*二维码的宽与高*/
            int width = qrcode.getWidth();
            int height = qrcode.getHeight();
            BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
            Graphics2D gs = bufImg.createGraphics();  
            if(StringUtil.isEmpty(qrcode.getbColor())){
            	gs.setBackground(Color.white);  
            }else{
            	gs.setBackground(toColorFromString(qrcode.getbColor()));
            }
            gs.clearRect(0, 0, width, height);  
            /* 设置图像颜色 */
            if(StringUtil.isEmpty(qrcode.getqColor())){
            	gs.setColor(Color.BLACK);  
            }else{
            	gs.setColor(toColorFromString(qrcode.getqColor()));
            }
            /* 设置偏移量 不设置可能导致解析出错 */  
            int pixoff = 3;  
            /* 输出内容 > 二维码 */  
            if (contentBytes.length > 0 && contentBytes.length <width) {  
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);  
                for (int i = 0; i < codeOut.length; i++) {  
                    for (int j = 0; j < codeOut.length; j++) {  
                        if (codeOut[j][i]) {  
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);  
                        }  
                    }  
                }  
            } else {  
                System.err.println("QRCode content bytes length = "  
                        + contentBytes.length + " not in [ 0,200]. ");  
            }
            /* 判断是否需要添加logo图片 */
            if(qrcode.withLogo()){
            	int lgW = width / 4;//十六分之一 
    			int lgH = height / 4;
            	Image img = ImageIO.read(new File(qrcode.getLogoPath()));
                gs.drawImage(img, lgW +lgW/2, lgH + lgH/2,lgW,lgH, null);
                gs.dispose();  
                bufImg.flush();
            }
  
            /* 生成二维码QRCode图片 */ 
            //SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
			//String fileName = sdf.format(new Date()) + "." + qrcode.getFormat();
			//String url = qrcode.getQrcodePath() + fileName;
			String url = qrcode.getQrcodePath();
            File imgFile = new File(url);
            if(!imgFile.exists()){
            	imgFile.mkdirs();
			}
            ImageIO.write(bufImg, qrcode.getFormat(), imgFile);
            //System.out.println("生成二维码图片成功!");
        } catch (Exception e){  
            throw new PlatformException("二维码工具类生成图片异常", PlatformExceptionEnum.JE_CORE_UTIL_QR_CREATE_ERROR,new Object[]{qrcode},e);
        }
    }
    public static Color toColorFromString(String colorStr){  
    	if (colorStr.length() == 7) {
    		colorStr = colorStr.substring(1); 
		}
        Color color =  new Color(Integer.parseInt(colorStr, 16)) ;  
        return color;  
    }  
    /**
     * 解析二维码
     */
    public static String decode(String imagePath){
    	// QRCode 二维码图片的文件
    	File imageFile = new File(imagePath);
    	if(!imageFile.exists()){
    		System.out.println(imagePath+"，不存在。");return "";
    	}
    	BufferedImage bufImg = null;
    	String content = null;
    	try {
        	bufImg = ImageIO.read(imageFile);
        	QRCodeDecoder decoder = new QRCodeDecoder();
        	content = new String(decoder.decode(new QRImage(bufImg)), "utf-8");
    	} catch (IOException e) {
            throw new PlatformException("二维码工具类解析二维码异常", PlatformExceptionEnum.JE_CORE_UTIL_QR_PARSE_ERROR,new Object[]{imagePath},e);
    	} catch (DecodingFailedException dfe) {
            throw new PlatformException("二维码工具类解析二维码异常", PlatformExceptionEnum.JE_CORE_UTIL_QR_PARSE_ERROR,new Object[]{imagePath},dfe);
    	}
    	return content;
    }
    
}
class QRImage implements QRCodeImage{
	 private BufferedImage bufferedImage;  
	 public QRImage(BufferedImage image){  
	       this.bufferedImage = image;  
	}  
	@Override
	public int getHeight() {
		return bufferedImage.getHeight();
	}
	@Override
	public int getPixel(int x, int y) {
		return bufferedImage.getRGB(x, y);
	}

	@Override
	public int getWidth() {
		return bufferedImage.getHeight();
	}
	
}
