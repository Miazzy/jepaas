package com.je.core.locale;

import com.je.core.util.SpringContextHolder;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class MessageLocator {
    public static final String DEF_MSG = "后台处理出错了，请联系管理员！";

    private static MessageSource messageSource;

    static {
        messageSource = SpringContextHolder.getBean("messageSource");
        if (messageSource == null) {
            messageSource = new ResourceBundleMessageSource();
            ((ResourceBundleMessageSource) messageSource).setBasename("sys/locale/exception");
            ((ResourceBundleMessageSource) messageSource).setDefaultEncoding("UTF-8");
        }
    }


    public static String getMessage(String code) {
        return messageSource.getMessage(code, null, DEF_MSG, getLocal());
    }

    public static String getMessage(String code, String defMsg) {
        return messageSource.getMessage(code, null, defMsg, getLocal());
    }

    public static String getMessage(String code, Object[] args) throws NoSuchMessageException {
        return messageSource.getMessage(code, args, getLocal());
    }

    public static String getMessage(String code, Object[] args, String defMsg) {
        return messageSource.getMessage(code, args, defMsg, getLocal());
    }

    public static String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        return messageSource.getMessage(resolvable, locale);
    }

    //解析用户当前的Local
    public static Locale getLocal() {
        return LocaleContextHolder.getLocale();
    }
}
