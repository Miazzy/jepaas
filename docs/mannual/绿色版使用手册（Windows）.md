# JEPAAS绿色版安装部署手册（Windows版本）

## 平台介绍：

> 欢迎您使用JEPAAS快速开发平台，JEPAAS的开发历经多个历史大版本的变更及发展，成功在多个行业应用及得到广大ISV及业务方的认可，我们将秉持初心，持续为业界提供能够快速开发的低代码开发平台，持续为您提供优质的产品和服务。
> JEPAAS绿色版为官方提供给用户的快速体验版本，用户可以根据文档快速启动JEPAAS并体验产品。

##  安装包说明

JEPaaS平台主要由JEPAAS应用服务，数据库服务，推送服务组成，所以我们在绿色版中提供了应用及数据库的快速启动方式，各服务列表如下所示


| 服务名称 | 项目路径 | 启动脚本 | 描述 |
| --- | --- | --- | --- |
| JEPaaS应用服务 | apache-tomcat-8.5.34/webapps/WebRoot | 启动tomcat运行平台.bat | 使用Tomcat作为应用容器，为JEPaaS核心业务应用 |
| 连接器服务 | connector-server | 启动connector-server服务.bat | 管理用户与手持机的推送连接，使用了Netty的NIO服务 |
| 推送应用服务 | instant-push-server | 启动tomcat运行平台.bat | 使用用户连接实现推送具体业务，为SpringBoot应用 |
| MySQL数据库服务 | mysql-5.7.26-winx64 | 启动MySQL数据库.bat | 核心业务及推送的数据库服务 |
| Redis数据库服务 | Redis-x64-3.2.100 | 启动redis服务.bat | 二级缓存服务等 |

## 启动服务

进入绿色安装包，点击“启动绿色版JEPAAS服务.bat”即可启动服务。

> 请注意，启动前，操作系统的相关端口不能被其他应用占用！

相关端口配置为：


| 服务名称 | 占用端口 | 用途 |
| --- | --- | --- |
| MySQL数据库 | 3306 | 提供推送服务和JEPaas的数据服务 |
| Redis数据库 | 6379 | 提供二级缓存等 |
| 推送服务 | 8088 | 提供推送业务功能 |
| 连接器服务 | 7000,70001 | 管理用户Socket连接 |
| JEPaaS应用服务 | 8080 | JEPaaS业务应用 |

## 服务配置

### 连接器服务配置

连接器配置主要包括原生WebSocket配置、SocketIO配置，Redis配置，jmx配置，缓存配置，业务配置。
配置文件为：<u>connector-server/application.conf</u>
连接器主要用于统一的Socket连接管理，采用多端口，统一连接管理器设计等，可以基于多端口形式，统一发送和接受消息，同时，方便集群横向扩展。

#### 原生WebSocket配置

原生WebSocket是基于Netty实现的原生WebSocke，在APP移动端，我们采用了此端口，此配置如下所示
```
    websocket {
            port=7000 #端口配置
            wsPath="/jesocket" #路径配置
    }
    
```

#### SocketIO配置

由于原生WebSocket的浏览器兼容问题，我们采用SocketIO的兼容方案，在PC端，我们采用了此端口，此配置如下所示：
```
    port:7001 # 端口配置
    origin:"http://localhost:8080" # 暂时不起效
```

#### Redis配置

连接器使用Redis做缓存存储和消息解耦，此配置如下所示
```
    redis {
            url:127.0.0.1, #IP地址
            port:6379, #端口
            password:123456, #密码
            timeout:2000, #超时设置
            database:0 #选定槽
    }
```

#### Jmx配置

jmx消息配置，如下所示
```
    jmx {
            topic:topic.im.msg
            type:redis
            enabled:true
    }
```

#### 缓存配置

缓存配置，如下所示
```
    cache {
            type:redis,
            enabled:true
    }
```

#### 业务配置

业务配置主要与推送或及时通讯应用服务的相关接口定义，如下所示
```
    business {
            instantServerUrl:"http://127.0.0.1:8088/instant",
            instantGetAllMsgUrl:"/instant/news/getNoReadNewsByUser",
            instantUpdateMsgStatusUrl:"/instant/news/updateNewsStatusByUser",
            instantAddMessageUrl:"/instant/news/addMessage",
            instantGetGroupDetailUrl:"/instant/group/getGroupDetail"
    }
```

### 推送服务配置

推送服务为SpringBoot项目，用户需要配置数据库和Redis，配置文件地址为：instant-push-server-1.0.0.RELEASE/application.yml
如下所示
```
    datasource:
    name: dataSource
    url: jdbc:mysql://127.0.0.1:3306/instant?useUnicode=true&autoReconnect=true&failOverReadOnly=false&useSSL=false
    driver-class-name: com.mysql.jdbc.Driver
    username: root
    password: bt5
    type: com.alibaba.druid.pool.DruidDataSource
```

```
    redis:
    timeout: 30s
    port: 6379
    password: 123456
    host: 127.0.0.1
    database: 0
    jedis:
      pool:
        min-idle: 50
        max-active: 200
        max-wait: 20s
        max-idle: 8
```

### JEPAAS应用服务配置


#### 推送配置
登录后，点击开发-->产品与功能-->系统设置
![dd59e73467ef1d23d1c79fa1acc12677.png](../images/11b5e60a.png)


> 找到websocket服务地址，修改为socket服务ip地址和端口（修改为服务所在的ip和端口），当前设置为ws://127.0.0.1:7001
##### 找到websocket业务服务地址，修改为socket业务服务ip地址和端口（修改为服务所在的ip和端口），当前设置为127.0.0.1:8080
http://127.0.0.1:8080/instant/
> 找到websocket手机服务地址，修改为socket手机服务ip地址和端口（修改为服务所在的ip和端口），当前设置为ws://127.0.0.1:7000/jesocket

设置如下图
![cd53dfbf0293b818e609c2b526abe571.png](../images/b40629fc.png)

#### Tomcat配置

Tomcat端口配置信息：

端口号：8080，8005,8009

> 注意：您的计算机请不要占用这些端口，否则就会冲突。如果需要需要修改端口号或者其他的配置信息

配置文件的的相对路径：apache-tomcat-8.5.34\conf\server.xml

修改关闭端口号
```
<Server port="8005" shutdown="SHUTDOWN">
```    

修改服务访问端口号
```
<Connector port="8080" protocol="HTTP/1.1"
                   connectionTimeout="20000"
                   redirectPort="8443" URIEncoding="GBK"/>
```

#### JEPAAS项目配置

JEPAAS部署的相对目录：apache-tomcat-8.5.34\webapps\WebRoot
相对配置路径如下：apache-tomcat-8.5.34\webapps\WebRoot\WEB-INF\classes

mysql数据库配置文件：jdbc.properties
```
jdbc.url=jdbc:mysql://127.0.0.1:3306/garlic?useOldAliasMetadataBehavior=true
jdbc.username=root
jdbc.password=bt5
```
    
redis配置文件：redis.properties
```
redis.host=127.0.0.1
redis.port=6379
redis.pass=123456
```
	
### oracle jdk
jdk对应的文件夹：jdk1.8.1
#####java 命令使用的相对路径：
    jdk1.8.1\bin\java
    
### 2.5 数据库mysql
数据库对应的文件夹：mysql-5.7.26-winx64

配置信息：

端口号：3306

版本：mysql-5.7.26

如果你本机有安装MySQL，需要修改MySQL数据库的端口，否则就会冲突。

配置文件相对目录：mysql-5.7.26-winx64\my.ini
修改端口号：
    port = 需要修改的的端口号
> mysql连接信息：
    用户名：root
    密码：bt5
    jeplus 使用的主库：garlic
    IM 推送使用的主库：instant
  
### Redis服务
redis服务对应的文件夹：Redis-x64-3.2.100
配置信息：
端口号：6379

配置文件的相对目录：Redis-x64-3.2.100\redis.windows.conf
修改绑定地址
```
bind 0.0.0.0
```
修改端口号：
```
port 6379
```
    
修改auth：
```
requirepass 123456
```

## 启动文件说明

* readme.txt 本系统部署操作说明。
* 启动connector-server服务.bat ：
启动connector-server服务程序，可以双击进行启动，关闭程序可以通过ctrl + c 进行关闭服务的黑色窗口进行关闭。
* 启动instant-push-server服务.bat：
启动instant-push-server服务程序，可以双击进行启动，关闭程序可以通过ctrl + c 进行关闭服务的黑色窗口进行关闭。
* 启动MySQL数据库.bat：
启动MySQL数据库程序，可以双击进行启动，关闭程序可以通过ctrl + c 进行关闭服务的黑色窗口进行关闭。
* 启动redis服务.bat：
启动redis服务程序，可以双击进行启动，关闭程序可以通过ctrl + c 进行关闭服务的黑色窗口进行关闭。
* 启动tomcat运行平台.bat：
启动tomcat运行平台程序，可以双击进行启动，关闭程序可以通过ctrl + c 进行关闭服务的黑色窗口进行关闭。
        
关闭服务：
需要关闭服务，选择对应服务的黑色窗口关闭即可。

## 访问服务

1. http://localhost:8080/login.html
2. 登录用户名：13131059599
3. 登录密码：123456


## 社区服务

1. 官方讨论社区，[JE生态圈](http://jestq.com)
2. SAAS云及JEPAAS授权管理，[蒜瓣SAAS](https://suanbanyun.com)
3. JEPLUS介绍及演示，[JEPLUS官网](https://jeplus.cn)

## 联系我们

**电话**

* <u>010-82809807</u>
*  <u>400-0999-235</u>

* * *

**欢迎关注我们的微博**

![c5291612b0a3613587a7204518c4195c.png](../images/官方微博.png)






