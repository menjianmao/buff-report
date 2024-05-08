# price-get
对网易buff、igxe、C5等饰品交易平台csgo饰品价格与其他属性进行定时爬取后推送到你的邮箱；cookie过期可以邮件自动提醒，

自动读取buff收藏武器，路径：个人信息-我的收藏-饰品收藏
自动获取收藏武器的goods_id，自动获取到当前价格，并自动判断是否满足你的底价，满足则推送到邮箱。
也可根据你设置的关注等级Level，自由配置推送的权重；
详情可看logo2.jpg

PS：buff登陆一次后，读取完成你的收藏武器，可以在未登录状态提供当前武器最新的价格信息，不需要再次登录也可更新数据，具体方法如：getCSGOPriceInDate代码 中演示




# 重要声明
本项目是对其做的二次开发，感谢原作者yeafel666
原项目：https://github.com/yeafel666/price-get

此项目为个人学习项目，仅供学习参考，禁止用于商业用途。

增加了邮件推送功能，可以获取到当前售价和你设置的底价，触发底价后推送或每日推送,详情：logo.jpg 中查看
同时对其逻辑进行了大量的优化，



## 详情
网易buff封号很严格，谨慎使用,注意爬取频率和次数。目前6小时执行一次，
网易buff和C5平台需要在用户登录后，看控制台，抓到自己的cookie，贴到yml配置文件下。
Igxe平台无需任何配置。


## 使用说明
找到项目的src --> main -->java -->com.yeafel.priceget -->scheduled 目录，以下3个定时任务，可通过@Scheduled注解修改cron表达式更换定时任务。
。



## 项目架构
本项目使用java语言开发，使用springBoot框架作为框架。使用mysql5.7版本作为数据库。



## 创建表结构脚本
cs_transact_record.sql  交易记录表
历史交易信息

cs_need_get_goods.sql   实体数据表

重要信息：

base_price为你设置的底价，未设置为0，

base_mark 为你设置的底价标志，0为不设置，1为设置，

level 为当前饰品的关注等级，可根据等级推送，


共包含俩张表 运行sql脚本即可
```

## 关于cookie
在application-dev.yml(开发环境)文件或者application-pro.yml(线上环境)下的yeafel.cookie和yeafel.c5cookie配置自己账号登录之后的网易buff的cookie和c5cookie (igxe不需要配置)
,如您有服务器可修改 application-pro.yml的mysql地址为您线上服务器，application.yml下active切换为prod即可。
cookie配置例如:

```
yeafel:
  cookie: yourBuffCookie
  c5cookie: yourC5Cookie


## 项目运行模板!
logo.jpg

## 项目运行流程
将代码下载到本地电脑；

配置数据库 运行sql脚本 cs_need_get_goods.sql，与 cs_transact_record.sql

配置cookie；可运行后

对email配置，

运行项目（启动后可自动读取你的收藏武器，开始工作）



## 致谢
感谢 yeafel666 对本项目的支持，

由于框架限制，目前程序不能对sql灵活变动，为了更加完善程序，增加更多的功能，我把这个程序移至到了我的另一个项目中，Origin 中，感兴趣的小伙伴可去我的github上查看

PS:后面的更新中将不对本项目进行维护。
