package com.je.core.locale;

import java.util.Locale;

public class LocaleContext {
    private static ThreadLocal<Locale> localeContext = new ThreadLocal<Locale>();

    public static ThreadLocal<Locale> getLocaleContext() {
        return localeContext;
    }

    public static void setLocale(Locale locale) {
        localeContext.set(locale);
    }

    public static Locale getLocale() {
        return getLocaleContext().get() == null ? Locale.getDefault() : getLocaleContext().get();
    }
}
