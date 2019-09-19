package com.je.rbac.service.onlineuser;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.je.cache.service.rbac.OnlineUserCache;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class OnLineUserService {
    private static Logger logger = LoggerFactory.getLogger(OnLineUserService.class);

    @Autowired
    protected PCDynaServiceTemplate serviceTemplate;
    @Scheduled(cron="0 0/10 * * * ?")
    public void loadOnlineUserStatus() {
        serviceTemplate.executeSql("update je_core_enduser set ONLINESYS='0'");
//        long start = System.currentTimeMillis();
        List list = OnlineUserCache.getAllKey();
        List<String> keys = Lists.newArrayList();
        for (Object o : list) {
            try {
                String key = (String) o;
                keys.add(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int count = keys.size();
        int num = 200;//每次查询的条数
        //需要查询的次数
        int times = count / num;
        if (count % num != 0) {
            times = times + 1;
        }
        //开始查询的行数
        List<Callable<List<String>>> tasks = Lists.newArrayList();//添加任务
        for (int i = 0; i <= times; i++) {
            Callable<List<String>> qfe = new OnlineUserThreadQuery(keys, i * num, num,serviceTemplate);
            tasks.add(qfe);
        }
        ExecutorService execservice = Executors.newFixedThreadPool(5);
        List<Future<List<String>>> futures;
        try {
            futures = execservice.invokeAll(tasks);
            // 处理线程返回结果
            if (futures != null && futures.size() > 0) {
                for (Future<List<String>> future : futures) {
                    //不需要处理结果
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        execservice.shutdown();  // 关闭线程池
//        long end = System.currentTimeMillis();
//        logger.info("用时" + (end - start));
    }

    class OnlineUserThreadQuery implements Callable<List<String>> {
        private List<String> keys;
        private int bindex;
        private int num;
        PCDynaServiceTemplate serviceTemplate;
        public OnlineUserThreadQuery(List<String> keys, int bindex, int num, PCDynaServiceTemplate serviceTemplate) {
            this.keys = keys;
            this.bindex = bindex;
            this.num = num;
            this.serviceTemplate = serviceTemplate;
        }

        /**
         * TODO 暂不明确
         * @return
         * @throws Exception
         */
        @Override
        public List<String> call() throws Exception {
            int fromIndex = bindex;
            int toIndex = bindex + num;
            if (toIndex > keys.size()) {
                toIndex = keys.size() - 1;
            }
            List<String> thisKeys = keys.subList(fromIndex, toIndex);
            logger.info("start:{},end{}----{}", fromIndex, toIndex, JSON.toJSONString(thisKeys));
            if (null != keys && !keys.isEmpty()) {
                serviceTemplate.executeSql("update je_core_enduser set ONLINESYS='1' where user_id in(" + StringUtil.buildArrayToString(thisKeys.toArray(new String[thisKeys.size()])) + ")");
            }
            return null;
        }
    }

}
