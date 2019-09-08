@echo off
chcp 65001
title connect-server Ctrl+C键结束服务
@echo 正在启动Instant-push-server...
@echo .
@echo ...小提示..................................................................                                                                      .
@echo .                                                                         .
@echo .                                                                         .
@echo . Instant-push-server默认参数                                             .
@echo . 主机名/IP：127.0.0.1                                                    .
@echo . 端口：7001                                                              .
@echo .                                                                         .
@echo . 说明：如果报错可以添加pause查看原因                                     .
@echo .                                                                         .
@echo ...........................................................................

JAVA -jar -Dspring.config.location=connector-server-1.0.0.RELEASE\application.conf connector-server-1.0.0.RELEASE\connector-server-1.0.0.RELEASE.jar



