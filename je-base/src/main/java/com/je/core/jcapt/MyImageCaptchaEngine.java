package com.je.core.jcapt;


import java.awt.Font;

import com.je.core.util.StringUtil;
import com.je.core.util.WebUtils;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;


public class MyImageCaptchaEngine extends ListImageCaptchaEngine {

    // 随机生成的字符组成
//    private static final String RANDOM_WORD_GENERATOR = "123456789";
//
//    // 验证码图片上显示的字符个数
//    private static final int TEXT_SIZE = 4;
//
//    //验证码图片上显示的字符的大小设置
//    private static final int TEXT_WIDTH = 40;
//    private static final int TEXT_HEIGHT = 40;
//
//    // 验证码图片的大小设置
//    private static final int IMAGE_CAPTCHA_WIDTH = 170;
//    private static final int IMAGE_CAPTCHA_HEIGHT = 50;

    protected void buildInitialFactories() {
        // 随机生成的字符组成
        String RANDOM_WORD_GENERATOR = StringUtil.getDefaultValue(WebUtils.getBackVar("RANDOM_WORD_GENERATOR"), "123456789");

        // 验证码图片上显示的字符个数
        int TEXT_SIZE = Integer.parseInt(StringUtil.getDefaultValue(WebUtils.getBackVar("TEXT_SIZE"), "4"));

        //验证码图片上显示的字符的大小设置
        int TEXT_WIDTH = Integer.parseInt(StringUtil.getDefaultValue(WebUtils.getBackVar("TEXT_WIDTH"), "40"));
        int TEXT_HEIGHT = Integer.parseInt(StringUtil.getDefaultValue(WebUtils.getBackVar("TEXT_HEIGHT"), "40"));

        // 验证码图片的大小设置
        int IMAGE_CAPTCHA_WIDTH = Integer.parseInt(StringUtil.getDefaultValue(WebUtils.getBackVar("IMAGE_CAPTCHA_WIDTH"), "170"));
        int IMAGE_CAPTCHA_HEIGHT = Integer.parseInt(StringUtil.getDefaultValue(WebUtils.getBackVar("IMAGE_CAPTCHA_HEIGHT"), "50"));
        // 随机生成的字符
        WordGenerator wgen = new RandomWordGenerator(RANDOM_WORD_GENERATOR);
        RandomRangeColorGenerator cgen = new RandomRangeColorGenerator(
                new int[]{ 0, 100 }, new int[]{ 0, 100 }, new int[]{ 0, 100 });
        // 文字显示的个数
        TextPaster textPaster = new RandomTextPaster(new Integer(TEXT_SIZE),
                new Integer(TEXT_SIZE), cgen, true);
        // 图片的大小
        BackgroundGenerator backgroundGenerator =new  com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator(new Integer(IMAGE_CAPTCHA_WIDTH), new Integer(
                IMAGE_CAPTCHA_HEIGHT));


//        		new FunkyBackgroundGenerator(
//                new Integer(IMAGE_CAPTCHA_WIDTH), new Integer(
//                        IMAGE_CAPTCHA_HEIGHT));
        // 字体格式
        Font[] fontsList = new Font[]{ new Font("Arial", 0, 14),
                new Font("Arial", 0, 14), new Font("Arial", 0, 14), };
        // 文字的大小
        FontGenerator fontGenerator = new RandomFontGenerator(new Integer(
                TEXT_WIDTH), new Integer(TEXT_HEIGHT), fontsList);

        WordToImage wordToImage = new ComposedWordToImage(fontGenerator,
                backgroundGenerator, textPaster);
        this.addFactory(new GimpyFactory(wgen, wordToImage));
    }
}
