@echo off
chcp 65001
title Instant-push-server Ctrl+C键结束服务
@echo 正在启动Instant-push-server...
@echo .
@echo ...小提示..................................................................                                                                      .
@echo .                                                                         .
@echo .                                                                         .
@echo . Instant-push-server默认参数                                             .
@echo . 主机名/IP：127.0.0.1                                                    .
@echo . 端口：8088                                                              .
@echo .                                                                         .
@echo .                                                                          .
@echo .                                                                         .
@echo ...........................................................................


JAVA -jar -Dspring.config.location=instant-push-server-1.0.0.RELEASE\config\application.yml   instant-push-server-1.0.0.RELEASE\instant-push-server-1.0.0.RELEASE.jar