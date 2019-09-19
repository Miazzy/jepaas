package com.je.core.exception;

/**
 * 平台异常枚举类，定义平台功能的异常值
 *    2000:表异常, 3000:功能异常, 4000:数据字典异常
 *
 */
public enum PlatformExceptionEnum {
    /**
     * 平台核心异常
     */
    JE_CORE_ERROR("1000"),
    /**
     * 平台核心MQ框架服务异常
     */
    JE_CORE_MQ_ERROR("1001"),
    /**
     * 平台核心接口引擎API框架服务异常
     */
    JE_CORE_API_ERROR("1002"),
    /**
     * 平台编号异常
     */
    JE_CORE_SEQCODE_ERROR("1003"),
    /**
     * 当前编号长度大于了规定长度异常
     */
    JE_CORE_SEQCODE_TOLONG_ERROR("1004"),
    /**
     * 平台Controler返回异常
     */
    JE_CORE_CONTROLLER_ERROR("1010"),
    /**
     * 平台JSON构建成字符串
     */
    JE_CORE_JSONSTR_ERROR("1011"),
    /**
     * 请求参数加密异常
     */
    JE_CORE_PARAMPW_ERROR("1012"),
    /**
     * 使用SQL查询字段异常
     */
    JE_CORE_SQL_MODEL_ERROR("1013"),
    /**
     * 使用SQL查询数据异常
     */
    JE_CORE_SQL_DATA_ERROR("1014"),
    /**
     * 批量执行SQL异常
     */
    JE_CORE_SQL_BATCHEXECUTE_ERROR("1015"),
    /**
     * Excel导出模版异常
     */
    JE_CORE_EXCEL_EXPORT_ERROR("1020"),
    /**
     * 新版Excel导出模版异常
     */
    JE_CORE_EXCEL_EXP_ERROR("1021"),
    /**
     * 存储过程名未传入
     */
    JE_CORE_EXCEL_PROCEDURE_ERROR("1022"),
    /**
     * 自定义sql未传入
     */
    JE_CORE_EXCEL_DIYSQL_ERROR("1023"),
    /**
     * 功能表名未传入
     */
    JE_CORE_EXCEL_TABLECODE_ERROR("1024"),
    /**
     * Excel导入的关闭流异常
     */
    JE_CORE_EXCEL_INPUTSTREAM_ERROR("1025"),
    /**
       * Excel读取文件异常
     */
    JE_CORE_EXCEL_READFILE_ERROR("1026"),
    /**
     * Excel导入数据中止异常
     */
    JE_CORE_EXCEL_IMPDATA_ERROR("1028"),
    /**
     * DynaBean操作异常
     */
    JE_CORE_DYNABEAN_ERROR("1100"),
    /**
     * DynaBean保存修改字段过长
     */
    JE_CORE_DYNABEAN_FIELDLONG_ERROR("1101"),
    /**
     * 执行传入参数失败
     */
    JE_CORE_DYNABEAN_PARAM_ERROR("1102"),
    /**
     * 数据插入异常
     */
    JE_CORE_DYNABEAN_INSERT_ERROR("1103"),
    /**
     * 数据修改
     */
    JE_CORE_DYNABEAN_UPDATE_ERROR("1104"),
    /**
     * 数据删除异常
     */
    JE_CORE_DYNABEAN_DELETE_ERROR("1105"),
    /**
     * 数据禁用
     */
    JE_CORE_DYNABEAN_DISABLED_ERROR("1106"),
    /**
     * 数据启用
     */
    JE_CORE_DYNABEAN_ENABLED_ERROR("1107"),
    /**
     * 查询
     */
    JE_CORE_DYNABEAN_QUERY_ERROR("1107"),
    /**
     * 查询一条
     */
    JE_CORE_DYNABEAN_QUERYONE_ERROR("1108"),
    /**
     * 统计数量
     */
    JE_CORE_DYNABEAN_COUNT_ERROR("1109"),
    /**
     * 获取DYNABEAN模型异常
     */
    JE_CORE_DYNABEAN_MODEL_ERROR("1110"),
    /**
     * 根据DynaBean构建Query异常
     */
    JE_CORE_DYNABEAN_BUILDING_ERROR("1111"),
    /**
     * 实体BEAN保存数据异常
     */
    JE_CORE_ENEITY_SAVE_ERROR("1150"),
    /**
     * 实体BEAN修改数据异常
     */
    JE_CORE_ENEITY_UPDATE_ERROR("1151"),
    /**
     * 实体BEAN删除数据异常
     */
    JE_CORE_ENEITY_DELETE_ERROR("1152"),
    /**
     * 实体BEAN查询数据异常
     */
    JE_CORE_ENEITY_QUERY_ERROR("1153"),
    /**
     * 实体BEAN查询一条数据异常
     */
    JE_CORE_ENEITY_QUERY_ONE_ERROR("1154"),
    /**
     * 根据SQL查询出所有列名异常
     */
    JE_CORE_ENEITY_SQL_COLUMN_ERROR("1155"),
    /**
     * 调用存储过程异常
     */
    JE_CORE_ENEITY_PROCEDURE_ERROR("1156"),
    /**
     * 调用出参存储过程异常
     */
    JE_CORE_ENEITY_PROCEDURE_OUT_ERROR("1157"),
    /**
     * 获取connention链接异常
     */
    JE_CORE_ENEITY_CONNENTION_ERROR("1158"),
    /**
     * 加载存储过程列异常
     */
    JE_CORE_DB_COLUMN_PROCEDURE_ERROR("1159"),
    /**
     * 加载SQL列异常
     */
    JE_CORE_DB_COLUMN_SQL_ERROR("1160"),
    /**
     * 加载表名列异常
     */
    JE_CORE_DB_COLUMN_TABLE_ERROR("1161"),
    /**
     * 第三方数据源不支持数据异常
     */
    JE_CORE_DSFDB_UNKOWN_ERROR("1171"),
    /**
     * 第三方数据源写入配置文件
     */
    JE_CORE_DSFDB_WRITECONFIG_ERROR("1172"),
    /**
     * 第三方数据源调用存储过程异常
     */
    JE_CORE_DSFDB_PROCEDURE_ERROR("1173"),
    /**
     * 第三方数据源调用SQL出错
     */
    JE_CORE_DSFDB_SQL_ERROR("1174"),
    /**
     * 第三方数据源查询出一条数据
     */
    JE_CORE_DSFDB_QUERY_ONE_ERROR("1175"),
    /**
     * 未找到字典异常
     */
    JE_CORE_DIC_UNKOWN_ERROR("1180"),
    /**
     * 字典列对照异常
     */
    JE_CORE_DIC_CHECKITEM_ERROR("1181"),
    /**
     * 未找到主键信息
     */
    JE_CORE_DIC_UNKOWNPKFIELD_ERROR("1182"),
    /**
     * 未找到父节点字段信息
     */
    JE_CORE_DIC_UNKOWNPARENTFIELD_ERROR("1183"),
    /**
     * 未找到父节点信息信息
     */
    JE_CORE_DIC_UNKOWNPARENT_ERROR("1184"),
    /**
     * 资源表异常
     */
    JE_CORE_TABLE_ERROR("1200"),
    /**
     * 资源表DDL语句异常
     */
    JE_CORE_TABLE_DDL_ERROR("1201"),
    /**
     * 资源表构建DDL异常
     */
    JE_CORE_TABLE_BUILDING_ERROR("1202"),
    /**
     * 未找到指定资源表信息
     */
    JE_CORE_TABLE_NOFIND_ERROR("1203"),
    /**
     * 表信息应用异常
     */
    JE_CORE_TABLE_CREATE_ERROR("1204"),
    /**
     * 表信息删除异常
     */
    JE_CORE_TABLE_DELETE_ERROR("1205"),
    /**
     * 表信息应用异常
     */
    JE_CORE_TABLE_UPDATE_ERROR("1206"),
    /**
     * 资源表表格列批量保存
     */
    JE_CORE_TABLECOLUMN_UPDATE_ERROR("1207"),
    /**
     * 资源表表格索引批量保存
     */
    JE_CORE_TABLEINDEX_UPDATE_ERROR("1208"),
    /**
     * 资源表表格键批量保存
     */
    JE_CORE_TABLEKEY_UPDATE_ERROR("1209"),
    /**
     * 将资源表格式数据应用到其他数据库
     */
    JE_CORE_TABLE_CREATEOTHERDB_ERROR("1210"),
    /**
     * 检查表的列信息异常
     */
    JE_CORE_TABLE_CHECKCOLUMN_ERROR("1211"),
    /**
     * 检查表的键信息异常
     */
    JE_CORE_TABLE_CHECKKEY_ERROR("1212"),
    /**
     * 检查表的索引信息异常
     */
    JE_CORE_TABLE_CHECKINDEX_ERROR("1213"),
    /**
     * 资源表索引删除异常
     */
    JE_CORE_TABLEINDEX_DELETE_ERROR("1214"),
    /**
     * 资源表获取Oracle的clob字段
     */
    JE_CORE_TABLECOLUMN_CLOBFIELD_ERROR("1215"),
    /**
     * 缓存异常
     */
    JE_CORE_CACHE_ERROR("1300"),
    /**
     * 工具类异常
     */
    JE_CORE_UTIL_ERROR("1400"),
    /**
     * 日历节日工具异常
     */
    JE_CORE_UTIL_CALENDARHOLIDAY_ERROR("1401"),
    /**
     * 转换视频工具异常
     */
    JE_CORE_UTIL_TOVIDEO_ERROR("1402"),
    /**
     * CSS解析工具异常
     */
    JE_CORE_UTIL_CSSPARSER_ERROR("1403"),
    /**
     * 资源表工具异常
     */
    JE_CORE_UTIL_DATABASE_ERROR("1404"),
    /**
     * 日期工具异常
     */
    JE_CORE_UTIL_DATE_ERROR("1405"),
    /**
     * DES加密工具异常
     */
    JE_CORE_UTIL_DES_ERROR("1406"),
    /**
     * 文件工具类读取文件内容异常
     */
    JE_CORE_UTIL_FILE_READTEXT_ERROR("1407"),
    /**
     * 文件工具类关闭文件流异常
     */
    JE_CORE_UTIL_FILE_CLOSEIO_ERROR("1408"),
    /**
     * 文件工具类文件未找到异常
     */
    JE_CORE_UTIL_FILE_NOTFOUND_ERROR("1409"),
    /**
     * 文件工具类读取异常
     */
    JE_CORE_UTIL_FILE_READ_ERROR("1410"),
    /**
     * 文件工具类创建目录异常
     */
    JE_CORE_UTIL_FILE_CREATEFOLDER_ERROR("1411"),
    /**
     * 文件工具类创建文件异常
     */
    JE_CORE_UTIL_FILE_CREATEFILE_ERROR("1412"),
    /**
     * 文件工具类校验文件是否存在异常
     */
    JE_CORE_UTIL_FILE_EXISTSFILE_ERROR("1413"),
    /**
     * 文件工具类删除文件异常
     */
    JE_CORE_UTIL_FILE_DELETEFILE_ERROR("1414"),
    /**
     * 文件工具类删除文件夹异常
     */
    JE_CORE_UTIL_FILE_DELETEFOLDER_ERROR("1415"),
    /**
     * 文件工具类复制文件异常
     */
    JE_CORE_UTIL_FILE_COPYFILE_ERROR("1416"),
    /**
     * 文件工具类复制文件夹异常
     */
    JE_CORE_UTIL_FILE_COPYFOLDER_ERROR("1417"),
    /**
     * 文件工具类写入文件流异常
     */
    JE_CORE_UTIL_FILE_WRITEFILE_ERROR("1418"),
    /**
     * 文件工具类截取图片异常
     */
    JE_CORE_UTIL_FILE_CUTIMG_ERROR("1419"),
    /**
     * 文件工具类获取文件大小异常
     */
    JE_CORE_UTIL_FILE_GETSIZE_ERROR("1420"),
    /**
     * 文件工具类获取文件内容异常
     */
    JE_CORE_UTIL_FILE_GETCONTENT_ERROR("1421"),
    /**
     * 文件工具类保存文件异常
     */
    JE_CORE_UTIL_FILE_SAVEFILE_ERROR("1422"),
    /**
     * 文件工具类创建文件流异常
     */
    JE_CORE_UTIL_FILE_CREATEIO_ERROR("1423"),
    /**
     * 文件工具类写入文件流异常
     */
    JE_CORE_UTIL_FILE_WRITEIO_ERROR("1424"),
    /**
     * 文件工具类获取XML信息异常
     */
    JE_CORE_UTIL_FILE_GETXML_ERROR("1425"),
    /**
     * 文件工具类获取XML信息异常
     */
    JE_CORE_UTIL_FILE_BASE64_ERROR("1426"),
    /**
     * 文件工具类修改XML信息异常
     */
    JE_CORE_UTIL_FILE_UPDATEXML_ERROR("1430"),
    /**
     * HTTP请求工具类发送请求异常
     */
    JE_CORE_UTIL_HTTP_SEND_ERROR("1431"),
    /**
     * HTTP请求工具类关闭请求链接异常
     */
    JE_CORE_UTIL_HTTP_CLOSECONNECT_ERROR("1432"),
    /**
     * HTTP请求工具类SLL请求异常
     */
    JE_CORE_UTIL_HTTP_SSLCLIENT_ERROR("1433"),
    /**
     * 身份证工具类解析数值异常
     */
    JE_CORE_UTIL_IDCARD_PARSENUM_ERROR("1434"),
    /**
     * HTTP文件工具类下载文件异常
     */
    JE_CORE_UTIL_HTTPIO_DOWNLOAD_ERROR("1435"),
    /**
     * HTTP文件工具类关闭文件流异常
     */
    JE_CORE_UTIL_HTTPIO_CLOSEIO_ERROR("1436"),
    /**
     * HTTP工具类GET请求异常
     */
    JE_CORE_UTIL_HTTP_DOGET_ERROR("1437"),
    /**
     * HTTP工具类POST请求异常
     */
    JE_CORE_UTIL_HTTP_DOPOST_ERROR("1438"),
    /**
     * HTTP工具类关闭文件流异常
     */
    JE_CORE_UTIL_HTTP_CLOSEIO_ERROR("1439"),
    /**
     * HTTP工具类XML请求异常
     */
    JE_CORE_UTIL_HTTP_DOXML_ERROR("1440"),
    /**
     * HTTP工具类文件请求异常
     */
    JE_CORE_UTIL_HTTP_DOFILE_ERROR("1441"),
    /**
     * 数据库工具类获取链接异常
     */
    JE_CORE_UTIL_JDBC_CONNECT_ERROR("1448"),
    /**
     * 数据库工具类获取系统链接异常
     */
    JE_CORE_UTIL_JDBC_SYSTEMCONNECT_ERROR("1449"),
    /**
     * 数据库工具类关闭异常
     */
    JE_CORE_UTIL_JDBC_CLOSE_ERROR("1450"),
    /**
     * 数据库工具类查询SQL异常
     */
    JE_CORE_UTIL_JDBC_QUERY_ERROR("1451"),
    /**
     * 数据库工具类执行SQL异常
     */
    JE_CORE_UTIL_JDBC_EXECUTE_ERROR("1452"),
    /**
     * 数据库工具类关闭文件流异常
     */
    JE_CORE_UTIL_JDBC_CLOSEIO_ERROR("1453"),
    /**
     * 数据库工具类获取字节异常
     */
    JE_CORE_UTIL_JDBC_GETBYTE_ERROR("1454"),
    /**
     * 拼音工具类获取拼音异常
     */
    JE_CORE_UTIL_PINGYIN_GET_ERROR("1460"),
    /**
     * 配置文件工具类读取文件异常
     */
    JE_CORE_UTIL_PROP_READFILE_ERROR("1465"),
    /**
     * 系统配置文件工具类获取属性值异常
     */
    JE_CORE_UTIL_SYSPROP_CONFIG_ERROR("1466"),
    /**
     * 系统配置文件工具类获取JDBC属性值异常
     */
    JE_CORE_UTIL_SYSPROP_JDBC_ERROR("1467"),
    /**
     * 二维码工具类生成图片异常
     */
    JE_CORE_UTIL_QR_CREATE_ERROR("1472"),
    /**
     * 二维码工具类解析二维码异常
     */
    JE_CORE_UTIL_QR_PARSE_ERROR("1473"),
    /**
     * 反射工具类异常
     */
    JE_CORE_UTIL_REFLECTION_ERROR("1480"),
    /**
     * 反射工具类获取方法异常
     */
    JE_CORE_UTIL_REFLECTION_GETMETHOD_ERROR("1481"),
    /**
     * 反射工具类执行方法异常
     */
    JE_CORE_UTIL_REFLECTION_INVOKE_ERROR("1482"),
    /**
     * 反射工具类根据实体名称获取类异常
     */
    JE_CORE_UTIL_REFLECTION_GETCLASS_ERROR("1483"),
    /**
     * 字符串工具类获取后缀信息异常
     */
    JE_CORE_UTIL_STRING_GETSUBVALUE_ERROR("1490"),
    /**
     * 字符串工具类加密字符异常
     */
    JE_CORE_UTIL_STRING_ENHEX_ERROR("1491"),
    /**
     * 字符串工具类解密字符异常
     */
    JE_CORE_UTIL_STRING_DEHEX_ERROR("1492"),
    /**
     * 字符串工具类解密base64异常
     */
    JE_CORE_UTIL_STRING_DEBASE64_ERROR("1493"),
    /**
     * 字符串工具类获取异常信息异常
     */
    JE_CORE_UTIL_STRING_EXCEPTION_ERROR("1494"),
    /**
     * 系统工具类获取是否集群异常
     */
    JE_CORE_UTIL_WEBUTILS_GETCLUSTER_ERROR("1500"),
    /**
     * XML工具类读取文件信息异常
     */
    JE_CORE_UTIL_XML_READ_ERROR("1510"),
    /**
     * XML工具类关闭文件流异常
     */
    JE_CORE_UTIL_XML_CLOSEIO_ERROR("1511"),
    /**
     * ZIP压缩工具类压缩文件异常
     */
    JE_CORE_UTIL_ZIP_DOZIP_ERROR("1512"),
    /**
     * ZIP压缩工具类解压文件异常
     */
    JE_CORE_UTIL_ZIP_UNZIP_ERROR("1513"),
    /**
     * 平台配置功能异常
     */
    JE_FUNCINFO_ERROR("2000"),
    /**
     *  平台功能复制异常
     */
    JE_FUNCINFO_FUNCCOPY_ERROR("2001"),
    /**
     *  平台功能硬连接复制异常
     */
    JE_FUNCINFO_FUNCCOPYHARD_ERROR("2002"),
    /**
     *  平台功能清理异常
     */
    JE_FUNCINFO_FUNCCLEAR_ERROR("2003"),
    /**
     *  平台功能初始化异常
     */
    JE_FUNCINFO_FUNCINIT_ERROR("2004"),
    /**
     *  平台生成ICON图标异常
     */
    JE_SYS_ICON_GENERATE_ERROR("2200"),
    /**
     *  平台码库生成html文件
     */
    JE_SYS_MK_CREATEHTML_ERROR("2210"),
    /**
     *  平台图表Chart生成图片出错
     */
    JE_SYS_CHART_CREATEIMG_ERROR("2220"),
    /**
     *  平台图表Chart生成图片加密出错
     */
    JE_SYS_CHART_CREATEIMG_ENCODE_ERROR("2221"),
    /**
     *  平台图表Chart生成PDF出错
     */
    JE_SYS_CHART_CREATEPDF_ERROR("2222"),
    /**
     *  平台图表Chart生成PDF加密出错
     */
    JE_SYS_CHART_CREATEPDF_ENCODE_ERROR("2223"),
    /**
     * 定时任务执行延时操作异常
     */
    JE_SYS_TASK_DELOY_ERROR("2224"),
    /**
     * 定时任务取消任务异常
     */
    JE_SYS_TASK_CALL_ERROR("2225"),
    /**
     * 定时任务加入任务异常
     */
    JE_SYS_TASK_ADD_ERROR("2226"),
    /**
     * 定时任务执行任务异常
     */
    JE_SYS_TASK_EXECUTE_ERROR("2227"),
    /**
     * 升级功能打包异常
     */
    JE_SYS_UPGRADE_PACKAGE_ERROR("2300"),
    /**
     * 升级功能打包功能异常
     */
    JE_SYS_UPGRADE_PACKAGE_FUNCINFO_ERROR("2301"),
    /**
     * 升级功能打包表异常
     */
    JE_SYS_UPGRADE_PACKAGE_TABLE_ERROR("2302"),
    /**
     * 升级功能打包字典异常
     */
    JE_SYS_UPGRADE_PACKAGE_DIC_ERROR("2303"),
    /**
     * 升级功能打包图表异常
     */
    JE_SYS_UPGRADE_PACKAGE_CHART_ERROR("2304"),
    /**
     * 升级功能打包报表异常
     */
    JE_SYS_UPGRADE_PACKAGE_REPORT_ERROR("2305"),
    /**
     * 升级功能打包数据源异常
     */
    JE_SYS_UPGRADE_PACKAGE_DATASOURCE_ERROR("2306"),
    /**
     * 升级功能导入压缩包异常
     */
    JE_SYS_UPGRADE_IMPZIP_ERROR("2307"),
    /**
     * 升级功能解析压缩包异常
     */
    JE_SYS_UPGRADE_PARSEPZIP_ERROR("2308"),
    /**
     * 升级功能升级出错异常
     */
    JE_SYS_UPGRADE_UPGRADE_ERROR("2309"),
    /**
     * 升级功能升级功能信息出错异常
     */
    JE_SYS_UPGRADE_UPGRADE_FUNCINFO_ERROR("2310"),
    /**
     * 升级功能升级流程出错异常
     */
    JE_SYS_UPGRADE_UPGRADE_PROCESS_ERROR("2311"),
    /**
     * 升级功能升级SQL出错异常
     */
    JE_SYS_UPGRADE_UPGRADE_SQL_ERROR("2312"),
    /**
     * 升级功能批量升级延迟出错异常
     */
    JE_SYS_UPGRADE_UPGRADEBATCH_DELOY_ERROR("2313"),
    /**
     * 升级功能升级执行方法出错异常
     */
    JE_SYS_UPGRADE_UPGRADE_METHOD_ERROR("2314"),
    /**
     * 平台文档附件异常
     */
    JE_DOC_ERROR("3000"),
    /**
     * 平台获取文件大小
     */
    JE_DOC_FILE_SIZE_ERROR("3001"),
    /**
     * 平台获取文件内容
     */
    JE_DOC_FILE_CONTENT_ERROR("3002"),
    /**
     * 平台保存文件
     */
    JE_DOC_FILE_SAVE_ERROR("3003"),
    /**
     * 获取文件读取器
     */
    JE_DOC_FILE_READER_ERROR("3004"),
    /**
     * 获取文件流读取器
     */
    JE_DOC_FILE_READER_IN_ERROR("3005"),
    /**
     * 获取文件输出流读取器
     */
    JE_DOC_FILE_READER_OUT_ERROR("3006"),
    /**
     * 写入文件
     */
    JE_DOC_FILE_WRITE_ERROR("3008"),
    /**
     * 关闭文件流引擎
     */
    JE_DOC_FILE_CLOSEABLE_ERROR("3009"),
    /**
     * 获取文件的XML信息
     */
    JE_DOC_FILE_IM_ERROR("3010"),
    /**
     * 文件未找到
     */
    JE_DOC_FILE_NOTFOUND_ERROR("3011"),
    /**
     * 文件读取异常
     */
    JE_DOC_FILE_READ_ERROR("3012"),
    /**
     * 文件流关闭异常
     */
    JE_DOC_FILE_INPUTSTRAM_CLOSE_ERROR("3013"),
    /**
     * 文件输出流关闭异常
     */
    JE_DOC_FILE_OUTPUTSTRAM_CLOSE_ERROR("3014"),
    /**
     * 文件压缩文件未找到
     */
    JE_DOC_ZIPFILE_NOTFOUND_ERROR("3015"),
    /**
     * 下载多文件错误
     */
    JE_DOC_LOADFILES_ERROR("3016"),
    /**
     * 下载多文件参数错误
     */
    JE_DOC_LOADFILES_PARAM_ERROR("3017"),
    /**
     * 写文件参数错误
     */
    JE_DOC_FILE_WRITEPARAM_ERROR("3018"),
    /**
     * 将office文档转换成html
     */
    JE_DOC_FILE_DOCTOHTML_ERROR("3019"),
    /**
     * 读取EML文件失败
     */
    JE_DOC_FILE_READEML_ERROR("3020"),
    /**
     * 功能上传附件异常
     */
    JE_DOC_FUNC_UPLOAD_ERROR("3021"),
    /**
     * 业务字段上传附件异常
     */
    JE_DOC_BEAN_UPLOAD_ERROR("3022"),
    /**
     * 其他附件上传异常
     */
    JE_DOC_OTHER_UPLOAD_ERROR("3023"),
    /**
     * 文件上传异常
     */
    JE_DOC_FILE_UPLOAD_ERROR("3024"),
    /**
     * 文件复制异常
     */
    JE_DOC_FILE_COPY_ERROR("3025"),
    /**
     * 导出菜单的操作文档异常
     */
    JE_DOC_EXPORTMENU_ERROR("3026"),
    /**
     * 报表转换成WORD文档异常
     */
    JE_DOC_REPORT2WORD_ERROR("3027"),
    /**
     * 报表转换成EXCEL文档异常
     */
    JE_DOC_REPORT2EXCEL_ERROR("3028"),
    /**
     * 文件压缩的时候异常
     */
    JE_DOC_ZIP_ERROR("3029"),
    /**
     * 文件解压的时候异常
     */
    JE_DOC_UNZIP_ERROR("3030"),
    /**
     * 文件读取异常
     */
    JE_DOC_FILE_READTXT_ERROR("3031"),
    /**
     * 图片获取高宽异常
     */
    JE_DOC_FILE_READIMAGE_ERROR("3032"),
    //平台RBAC异常
    JE_RBAC_ERROR("4000"),
    //平台分级管理员RBAC异常
    JE_RBAC_ADMINPERM_ERROR("4100"),
    /**
     * 权限过滤器异常
     */
    JE_RBAC_FILTER_ERROR("4999"),
    //平台SAAS引擎异常
    JE_SAAS_ERROR("5000"),
    //SAAS引擎中人员的控制
    JE_SAAS_USER_ERROR("5100"),
    /**
     * 第三方支付异常
     */
    JE_SAAS_PAY_ERROR("5101"),
    /**
     * 微信APP支付回调链接出错
     */
    JE_SAAS_PAY_WXAPP_ERROR("5102"),
    /**
     * 微信支付下订单异常
     */
    JE_SAAS_PAY_WXORDER_ERROR("5103"),
    /**
     * 微信APP支付下订单异常
     */
    JE_SAAS_PAY_WXAPPORDER_ERROR("5104"),
    /**
     * 支付MD5加密异常
     */
    JE_SAAS_PAY_MD5ENCODE_ERROR("5105"),
    /**
     * 订单校验通过
     */
    CHECK_ORDER_CODE_SUCCESS ("100"),
    CHECK_ORDER_PARAMETER_ERROR ("101"),
    CHECK_ORDER_AMOUNT_ERROR ("102"),
    /**
     * 产品价格变动
     */
    CHECK_ORDER_CODE_FAIL_201 ("201"),
    /**
     * 订单中的产品已在支付过程中，请核查。
     */
    CHECK_ORDER_CODE_FAIL_202 ("202"),
    /**
     * 订单已在支付过程中，请核查。
     */
    CHECK_ORDER_CODE_FAIL_203 ("203"),
    /**
     * 只有待支付的订单才能修改
     */
    CHECK_ORDER_CODE_FAIL_204 ("204"),
    /**
     * 人员限额数量必填
     */
    CHECK_ORDER_CODE_FAIL_205 ("100"),
    /**
     * 已超过最大限额数量
     */
    CHECK_ORDER_CODE_FAIL_206 ("206"),
    /**
     * 人员限额数量必须小于xx
     */
    CHECK_ORDER_CODE_FAIL_207 ("207"),
    /**
     * 订单中选购的产品必须为人员限额
     */
    CHECK_ORDER_CODE_FAIL_208 ("208"),
    CHECK_ORDER_CODE_FAIL_209 ("209"),
    /**
     * 订单状态必须为待支付或者部分支付才能发起立即支付操作
     */
    CHECK_ORDER_CODE_FAIL_210 ("210"),
    /**
     * 订单已超时
     */
    CHECK_ORDER_CODE_FAIL_211 ("211"),
    /**
     * 订单未完成支付或已取消，不可执行产品安装操作。
     */
    CHECK_ORDER_CODE_FAIL_212 ("212"),
    /**
     * 订单数据缺失
     */
    CHECK_ORDER_CODE_FAIL_213 ("213"),
    /**
     * 手机回执码验证成功
     */
    VALIDATE_ACCOUNT_CODE_SUCCESS ("300"),
    /**
     * 手机回执码验证失败，失败原因为未生成验证码
     */
    VALIDATE_ACCOUNT_CODE_401 ("401"),
    /**
     * 手机回执码输入有误
     */
    VALIDATE_ACCOUNT_CODE_402 ("402"),
    /**
     * 账户操作成功
     */
    ACCOUNT_OPERATION_CODE_SUCCESS ("500"),
    /**
     * 冻结操作失败,账户当前冻结金额已变动
     */
    ACCOUNT_OPERATION_CODE_601 ("601"),
    /**
     * 冻结操作失败，每个订单只能有一条冻结记录
     */
    ACCOUNT_OPERATION_CODE_602 ("602"),
    /**
     * 解冻操作失败,账户当前冻结金额已变动。
     */
    ACCOUNT_OPERATION_CODE_603 ("603"),
    /**
     * 划账失败，账户余额已变动
     */
    ACCOUNT_OPERATION_CODE_604 ("604"),
    /**
     * 划账失败，账户可用金额已不足以支付本次订单的金额
     */
    ACCOUNT_OPERATION_CODE_605 ("605"),
    /**
     * 充值失败,账户余额已变动
     */
    ACCOUNT_OPERATION_CODE_606 ("606"),
    /**
     * 解冻操作失败，解冻金额大于账户当前冻结金额
     */
    ACCOUNT_OPERATION_CODE_607 ("607"),
    /**
     * 冻结操作失败,本次申请冻结金额大于当前账户可使用的余额。
     */
    ACCOUNT_OPERATION_CODE_608 ("608"),
    /**
     * 支付成功
     */
    PAY_SUCCESS ("1000"),
    /**
     * 待支付，跳转到余额支付页面
     */
    PAY_SUCCESS_1001 ("1001"),
    /**
     * 支付宝建立下单请求成功
     */
    PAY_SUCCESS_1002 ("1002"),
    /**
     * 微信支付下单成功
     */
    PAY_SUCCESS_1003 ("1003"),
    /**
     *支付失败
     */
    PAY_FAIL ("2000"),
    /**
     * 订单状态已变更
     */
    PAY_FAIL_2001 ("2001"),
    /**
     * 微信支付回参解析出错
     */
    PAY_FAIL_2002 ("2002"),
    /**
     * 微信下单失败
     */
    PAY_FAIL_2003 ("2003"),
    /**
     * 渠道卷检查ok
     */
    SUBVOUCHERNO_CHECK_SUCCESS ("700"),
    /**
     * 未找到对应的渠道子卷，请检查兑换码
     */
    SUBVOUCHERNO_CHECK_FAIL_701 ("701"),
    /**
     *  渠道子卷已被激活
     */
    SUBVOUCHERNO_CHECK_FAIL_702 ("702"),
    /**
     * 渠道子卷已被停用
     */
    SUBVOUCHERNO_CHECK_FAIL_703 ("703"),
    /**
     * 渠道主卷已被停用
     */
    SUBVOUCHERNO_CHECK_FAIL_704 ("704"),
    /**
     * 渠道子卷未绑定验证手机号
     */
    SUBVOUCHERNO_CHECK_FAIL_705 ("705"),
    /**
     * 手机回执码输入有误
     */
    VALIDATE_SUBVOUCHER_CODE_801 ("801"),
    /**
     * 手机回执码验证失败，失败原因为未生成验证码
     */
    VALIDATE_SUBVOUCHER_CODE_802 ("802"),
    /**
     * 渠道卷兑换验证码校验通过
     */
    VALIDATE_SUBVOUCHER_CODE_SUCCESS ("800"),
    /**
     * 丢失关键参数
     */
    MISS_EXPECTED_PARAMETER ("901"),
    /**
     *
     */
    SUBVOUCHER_KEY_CODE_CHECK_FAIL ("706"),
    /**
     *
     */
    SUBVOUCHER_PHONENO_CHECK_FAIL ("707"),
    /**
     * 渠道子卷未通过认证
     */
    SUBVOUCHER_TOKEN_CHECK_FAIL ("708"),
    /**
     *
     */
    SHOPPING_UNKNOWN_ERROR ("0000"),
    /**
     * 充值金额小于0
     */
    RECHAREGE_ERROR_9001 ("9001"),
    /**
     *
     */
    REDIRECT_TO_BANLANCE ("1004"),
    /**
     *
     */
    REDIRECT_TO_WECHAT ("1005"),
    /**
     *
     */
    REDIRECT_TO_ALI ("1006"),
    /**
     * 平台工作流异常
     */
    JE_WF_EROOR("6000"),
    /**
     * 流程保存部署文件异常
     */
    JE_WF_SAVEFILE_EROOR("6001"),
    /**
     * 流程执行自定义事件异常
     */
    JE_WF_EVENT_EXECUTEDIY_EROOR("6002"),
    /**
     * 流程启动异常
     */
    JE_WF_START_EROOR("6003"),
    /**
     * 流程发起异常
     */
    JE_WF_SPONSOR_EROOR("6004"),
    /**
     * 流程发起未找到节点异常
     */
    JE_WF_SPONSOR_NOTASK_EROOR("6005"),
    /**
     * 流程清理脏数据异常
     */
    JE_WF_CLEAR_DIRTYDATA_EROOR("6006"),
    /**
     * 流程未找到节点异常
     */
    JE_WF_NOTASK_EROOR("6007"),
    /**
     * 流程找到多个定义异常
     */
    JE_WF_MOREPROCESS_EROOR("6008"),
    /**
     * 流程路径构建失败异常
     */
    JE_WF_TRANSITION_BUILDTASK_EROOR("6009"),
    /**
     * 未知流程路径异常
     */
    JE_WF_TRANSITION_UNKOWN_EROOR("6010"),
    /**
     * 流程完成流转动作异常
     */
    JE_WF_COMPLETETASK_EROOR("6011"),
    /**
     * 流程流转未找到任务节点异常
     */
    JE_WF_COMPLETETASK_FINDTASK_EROOR("6012"),
    /**
     * 流程流转聚合节点异常
     */
    JE_WF_COMPLETETASK_JOIN_EROOR("6013"),
    /**
     * 流程流转未找到回退任务信息异常
     */
    JE_WF_COMPLETETASK_NOBACKTASK_EROOR("6014"),
    /**
     * 流程流转送交固定节点未找到处理人异常
     */
    JE_WF_SUBMIT_TOASSGINEE_NOUSER_EROOR("6015"),
    /**
     * 流程流转送交候选节点未找到处理人异常
     */
    JE_WF_SUBMIT_JOINT_NOUSER_EROOR("6016"),
    /**
     * 流程流转送交分支异常
     */
    JE_WF_SUBMIT_FORK_EROOR("6017"),
    /**
     * 流程流转直接送交未找到节点异常
     */
    JE_WF_RETURNSUBMIT_NOTASK_EROOR("6018"),
    /**
     * 流程流转调拨未找到节点异常
     */
    JE_WF_ALLOT_NOTASK_EROOR("6019"),
    /**
     * 流程流转跳跃未找到节点异常
     */
    JE_WF_JUMP_NOTASK_EROOR("6020"),
    /**
     * 流程流转驳回未找到节点异常
     */
    JE_WF_REJECT_NOTASK_EROOR("6021"),
    /**
     * 流程流转退回未找到节点异常
     */
    JE_WF_ROLLBACK_NOTASK_EROOR("6021"),
    /**
     * 流程流转委托未找到节点异常
     */
    JE_WF_ENTRUST_NOTASK_EROOR("6022"),
    /**
     * 流程流转撤销委托未找到节点异常
     */
    JE_WF_CALLENTRUST_NOTASK_EROOR("6023"),
    /**
     * 流程流转转办未找到节点异常
     */
    JE_WF_TRANSMIT_NOTASK_EROOR("6024"),
    /**
     * 流程流转自由节点送交未找到节点异常
     */
    JE_WF_CIRCULAR_NOTASK_EROOR("6025"),
    /**
     * 流程预定义未找到处理人
     */
    JE_WF_DIYASSGINEE_NOUSER_EROOR("6026"),
    /**
     * 流程预定义未找到节点
     */
    JE_WF_DIYASSGINEE_NOTASK_EROOR("6027"),
    /**
     * 流程作废流程异常
     */
    JE_WF_ENDPROCESS_EROOR("6028"),
    /**
     * 流程会签异常
     */
    JE_WF_COUNTERSIGN_EROOR("6029"),
    /**
     * 流程会签异常
     */
    JE_WF_COUNTERSIGN_NOBACKTASK_EROOR("6030"),
    /**
     * 流程流转会签时候找到多个聚合节点处理人
     */
    JE_WF_COUNTERSIGN_JOIN_MOREUSER_EROOR("6031"),




    /**
     * 工作流生成流程图异常
     */
    JE_WF_EXPIMAGE_EROOR("6001"),
    //平台手机APP异常
    JE_PHONE_ERROR("7000"),
    //平台消息引擎异常
    JE_MESSAGE_ERROR("8000"),
    /**
     * 系统同步钉钉上的人员异常
     */
    JE_MESSAGE_DD_SYNC4DD_ERROR("8001"),
    /**
     * 系统同步到钉钉上的人员异常
     */
    JE_MESSAGE_DD_SYNCTODD_ERROR("8002"),
    /**
     * 系统导入钉钉上的人员异常
     */
    JE_MESSAGE_DD_IMPDDUSER_ERROR("8003"),
    /**
     * 钉钉邀请用户
     */
    JE_MESSAGE_DD_YQUSER_ERROR("8004"),
    /**
     * 钉钉启用用户
     */
    JE_MESSAGE_DD_ENABLEUSER_ERROR("8005"),
    /**
     * 钉钉禁用用户
     */
    JE_MESSAGE_DD_DISABLEUSER_ERROR("8006"),
    /**
     * 钉钉同步人员状态
     */
    JE_MESSAGE_DD_SYNCUSERSTATUS_ERROR("8007"),
    /**
     * 系统导入钉钉上的单个部门异常
     */
    JE_MESSAGE_DD_IMPDDONEDEPT_ERROR("8008"),
    /**
     * 邮箱测试服务器异常
     */
    JE_MESSAGE_EMAIL_TESTCONFIG_ERROR("8100"),
    /**
     * 邮箱连接服务器异常
     */
    JE_MESSAGE_EMAIL_CONNECT_ERROR("8101"),
    /**
     * 邮箱接受获取目录异常
     */
    JE_MESSAGE_EMAIL_GETFOLDER_ERROR("8102"),
    /**
     * 邮箱接受获取消息内容异常
     */
    JE_MESSAGE_EMAIL_GETMESSAGE_ERROR("8103"),
    /**
     * 邮箱接受连接关闭异常
     */
    JE_MESSAGE_EMAIL_STORECLOSE_ERROR("8104"),
    /**
     * 邮箱接受构建消息内容异常
     */
    JE_MESSAGE_EMAIL_BUILDMESSAGE_ERROR("8105"),
    /**
     * 邮箱接受获取消息内容异常
     */
    JE_MESSAGE_EMAIL_GETCONTENT_ERROR("8106"),
    /**
     * 邮箱发送未提供帐号密码异常
     */
    JE_MESSAGE_EMAIL_NOUSERNAME_ERROR("8107"),
    /**
     * 邮箱测试发送异常
     */
    JE_MESSAGE_EMAIL_TESTSEND_ERROR("8108"),
    /**
     * IM异常
     */
    JE_MESSAGE_IM__ERROR("8900"),
    //平台项目异常
    JE_PROJECT_ERROR("9000"),
    /**
     * 未登录用户
     */
    UNKOWN_LOGINUSER("9000"),
    //未知异常
    UNKOWN_ERROR("9999");
//    //未知异常，后台处理出错了，请联系管理员
//    UNKOWN_ERROR("1000"),
//    //DynaBean的增删改查错误
//    DYNABEAN_CRUD_ERROR("2001"),
//    //DynaBean保存修改字段长度过长异常
//    DYNABEAN_FIELDLONG_ERROR("2002"),
//    //功能加载异常
//    FUNCINFO_LOAD_ERROR("3001"),
//    //平台表资源异常
//    TABLE_DDL_ERROR("4001"),
//    //文档附件异常
//    DOCUMENT_ERROR("5001"),
//    //未登陆用户
//    UNKOWN_LOGINUSER("9000"),
//    // 支付异常
//    PAY_FAIL("6001"),
//    // 兑换卷异常
//    VOUCHER_FAIL("7001"),
//    // 内部调用异常
//    INTERNAL_CALLBACK_ERROR("8001"),
//    //过滤器异常
//    INTERNAL_FILTER_ERROR("8002");
    private String code;

    private PlatformExceptionEnum(String code) {
        this.code = code;
    }

    public static PlatformExceptionEnum getDefault(PlatformExceptionEnum exceptionEnum) {
        if (exceptionEnum == null) {
            return UNKOWN_ERROR;
        }

        for (PlatformExceptionEnum value : PlatformExceptionEnum.values()) {
            if (value.equals(exceptionEnum)) {
                return exceptionEnum;
            }
        }

        return UNKOWN_ERROR;
    }

    public static PlatformExceptionEnum getDefault(String code) {
        if (code == null || "".equals(code)) {
            return UNKOWN_ERROR;
        }

        for (PlatformExceptionEnum value : PlatformExceptionEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }

        return UNKOWN_ERROR;
    }

    public String getCode() {
        return code;
    }


}
