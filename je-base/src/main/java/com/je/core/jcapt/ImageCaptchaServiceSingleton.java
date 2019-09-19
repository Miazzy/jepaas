package com.je.core.jcapt;


import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;


public class ImageCaptchaServiceSingleton {

    private static ImageCaptchaService imageCaptchaService = new DefaultManageableImageCaptchaService(
            new FastHashMapCaptchaStore(), new MyImageCaptchaEngine(), 180,
            100000, 75000);

    public static ImageCaptchaService getInstance() {
        return imageCaptchaService;
    }
}
