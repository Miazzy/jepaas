package com.je.cache.redis;

/**
 * 平台Redis缓存类型
 *
 * @ProjectName: je-saas-platform
 * @Package: com.je.redis
 * @ClassName: PlatformCacheType
 * @Description: 平台Redis缓存类型
 * @Author: LIULJ
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public enum PlatformCacheType {

    /**
     * 平台核心认证Token缓存
     */
    JE_AUTHENTICATION_CACHE("authenticationCache"),
    /**
     * 平台数据字典的缓存
     */
    JE_DIC_CACHE("dicCache"),
    /**
     * 平台数据字典列表的缓存
     */
    JE_DIC_INFO_CACHE("dicInfoCache"),
    /**
     * 平台DynaCache的缓存
     */
    JE_DYNA_CACHE("dynaCache"),
    /**
     * 平台文件类型缓存
     */
    JE_FILE_TYPE_CACHE("fileTypeCache"),
    /**
     * 平台功能缓存
     */
    JE_FUNC_INFO_CACHE("funcInfoCache"),
    /**
     * 平台功能缓存
     */
    JE_FUNC_INFOSAAS_CACHE("funcInfoSaasCache"),
    /**
     * 平台功能权限缓存
     */
    JE_FUNC_PERM_CACHE("funcUserPermCache"),
    /**
     * 功能静态缓存
     */
    JE_FUNC_STATICIZE_CACHE("funcStaticizeCache"),
    /**
     * 菜单静态缓存
     */
    JE_MENU_STATICIZE_CACHE("menuStaticizeCahce"),
    /**
     * 资源表缓存
     */
    JE_TABLE_CACHE("tableCache");

    private String value;

    public String getValue() {
        return value;
    }

    PlatformCacheType(String value) {
        this.value = value;
    }
}
