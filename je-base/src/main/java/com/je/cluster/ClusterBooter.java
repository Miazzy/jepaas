package com.je.cluster;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 集群启动器，用于初始化集群环境
 *
 * @ProjectName: je-saas-platform
 * @Package: com.je.cluster
 * @ClassName: ClusterBooter
 * @Description: 用于初始化集群环境
 * @Author: LIULJ
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2018</p>
 */
@Component
public class ClusterBooter implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterBooter.class);
    private static final String DEFAULT_ENABLED = "1";
    private static AtomicBoolean enabled = new AtomicBoolean(false);
    private static AtomicBoolean started = new AtomicBoolean(false);
    /**
     * 集群消息通道
     */
    public static final String PLUS_CLUSTER_CHANNEL = "_plusClusterChannel";

    /**
     * 获取集群索引，此索引必须是集群唯一
     * @return
     */
    public static String getClusterIndex(){
        return CLUSTER_PARAMS.get("index");
    }

    /**
     * 获取集群服务名称
     * @return
     */
    public static String getClusterName(){
        return CLUSTER_PARAMS.get("name");
    }

    /**
     * 获取JDK版本
     * @return
     */
    public static String getJavaVersion(){
        return CLUSTER_PARAMS.get("JavaVersion");
    }

    /**
     * 获取操作系统名称
     * @return
     */
    public static String getSystemName(){
        return CLUSTER_PARAMS.get("SystemName");
    }

    /**
     * 获取操作系统版本
     * @return
     */
    public static String getSystemVersion(){
        return CLUSTER_PARAMS.get("SystemName");
    }

    /**
     * 获取是否是集群启动
     * @return
     */
    public static boolean isClusterEnabled(){
        return enabled.get();
    }

    /**
     * 获取集群中该节点的项目类型
     * @return
     */
    public static String getClusterProject(){
        return CLUSTER_PARAMS.get("project");
    }

    public static String getOutsideIp() { return CLUSTER_PARAMS.get("outsideIp"); }

    /**
     * 获取是否启动
     * @return
     */
    public static boolean isStarted(){
        return started.get();
    }

    /**
     *  TODO 缓存 刘利军
     * 集群环境参数
     */
    private static final Map<String,String> CLUSTER_PARAMS = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties clusterProps;
        try {
            clusterProps = PropertiesLoaderUtils.loadAllProperties("cluster.properties");
            String clusterEnable = clusterProps.getProperty("cluster.enable");
            if(DEFAULT_ENABLED.equals(clusterEnable)){
//                LOGGER.info("cluster enabled,begin init cluster params");
                CLUSTER_PARAMS.put("SystemName", System.getProperties().getProperty("os.name"));
                CLUSTER_PARAMS.put("SystemVersion", System.getProperties().getProperty("os.version"));
                CLUSTER_PARAMS.put("JavaVersion", System.getProperties().getProperty("java.version"));
                CLUSTER_PARAMS.put("index",clusterProps.getProperty("cluster.server.index"));
                CLUSTER_PARAMS.put("name",clusterProps.getProperty("cluster.server.name"));
                CLUSTER_PARAMS.put("project",clusterProps.getProperty("cluster.project"));
                CLUSTER_PARAMS.put("outsideIp", clusterProps.getProperty("cluster.server.outsideIp"));
                if(!enabled.compareAndSet(false,true)){
                    throw new Exception("clusterBooter init failure");
                }
            }
        }catch (Exception e){
            throw new PlatformException("init cluster params failure", PlatformExceptionEnum.JE_CORE_CACHE_ERROR,e);
//            LOGGER.error("init cluster params failure",e);
//            System.exit(0);
        }

        // 启动
        if(!started.compareAndSet(false,true)){
            throw new PlatformException("clusterBooter init failure", PlatformExceptionEnum.JE_CORE_CACHE_ERROR);
//            throw new Exception("clusterBooter init failure");
        }
    }

}
