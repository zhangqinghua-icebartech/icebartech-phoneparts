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
access-id: LTAIt3vIGEQOyxs7
access-key: vmlIoysSPo9D14NXheYLpSHdqBAf96
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
access-id: LTAIt3vIGEQOyxs7
access-key: vmlIoysSPo9D14NXheYLpSHdqBAf96
oss-endpoint: http://oss-cn-hongkong.aliyuncs.com/
oss-url: https://cut0820.oss-cn-hongkong.aliyuncs.com
bucket-name: cut0820