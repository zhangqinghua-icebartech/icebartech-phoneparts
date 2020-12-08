开发环境（2020-11-05）：
接口地址：http://120.78.135.29/api
后台地址：http://120.78.135.29/v2/mobile/#/login

## Redis
database: 0
host: 120.78.135.29
port: 6579
password:

## MySQL
url: jdbc:mysql://120.78.135.29:3306/phoneparts_test?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&autoReconnect=true&useCompression=true&zeroDateTimeBehavior=convertToNull
username: phoneparts
password: phoneparts

## 塞邮短信：
url: https://api.mysubmail.com/message/xsend.json
appId: 52983
appKey: cab23130148380dc4adc9ceb72e2b4d8
mode: prod
project-codes:
  - p3b9J1 #验证码模板

## 塞邮邮箱：
from: phone
url: https://api.mysubmail.com/mail/xsend.json
appid: 14561
appKey: fe8dfa6bf7883c20cbb7f14ab8abe35a
templates:
  - 1f12C2

## 阿里云OSS：
access-id:
access-key:
oss-endpoint: http://oss-cn-hongkong.aliyuncs.com/
oss-url: https://cut0820.oss-cn-hongkong.aliyuncs.com
bucket-name: cut0820



生产环境（2020-11-05）：
接口地址：http://boss.purcellcut.com/v4/api
后台地址：https://boss.purcellcut.com/v2/mobile

## Redis
??

## MySQL
??

## 塞邮短信：
url: https://api.mysubmail.com/message/xsend.json
appId: 52983
appKey: cab23130148380dc4adc9ceb72e2b4d8
mode: prod
project-codes:
  - p3b9J1 #验证码模板

## 塞邮邮箱：
from: phone
url: https://api.mysubmail.com/mail/xsend.json
appid: 14561
appKey: fe8dfa6bf7883c20cbb7f14ab8abe35a
templates:
  - 1f12C2

## 阿里云OSS：
access-id:
access-key:
oss-endpoint: http://oss-cn-hongkong.aliyuncs.com/
oss-url: https://cut0820.oss-cn-hongkong.aliyuncs.com
bucket-name: cut0820




数据库：
旧数据A1，旧数据备份A2，新数据B1，新数据备份B2。合并后数据C1。

原则：
1. 以旧数据库为主。
2. 以新数据的一级、二级、三级分类和产品数据代替掉旧的。
3. 以新手机的用户表（有效使用的）重新插入到旧数据中去。
4. 迁移完数据后，旧服务继续使用，但与新服务脱离关系。

步骤：
1. 关停新服务（旧服务不停）。
2. 备份A1至A2，C1，备份B1至B2。
3. 删除C1的一级、二级、三级分类和产品数据（可能包括用户记录）。
4. 将B2的一级、二级、三级分类和产品数据复制进C1。
5. 将B2的有效用户（表格绿色的）重新倒入C1数据库（不包含主键）。
6. 将新服务的链接指向C1数据库，重建表结构。
7. 清空缓存。
8. 将管理员账号改成手机号登陆（可选）。
9. 开放新服务给用户使用。