2020.12.13 手机膜思腾版本，以V5版为蓝本。

服务器信息
系统版本	CentOS Linux release 7.6.1810 (Core)
内核版本	 3.10.0-957.27.2.el7.x86_64
公网IP	47.242.210.88
SSH-PORT	5822
内网IP	172.31.123.123
账号	root	Bg3605608A@*	先使用userhk登录再切换
	userhk	Bg3605608A@
部署项目	Mysql,phoneparts程序
数据库
mysql
版本	8.0.19 x86_64 (MySQL Community Server - GPL)
字符集	utf8mb4
账号	root	Bg360123456	权限：所有
	phoneparts	phoneparts
本地连接IP	172.31.123.123
数据库	devia
部署路径	程序路径	/usr/local/mysql
	datadir	/u01/mysqldata/mysql
	错误日志	/u01/mysqldata/mysql/mysql-error.log
	二进制日志	/u01/mysqldata/mysql/mysql-bin.XXX
启动关闭	启动： systemctl start mysqld
	重启：systemctl restart mysqld
	关闭： systemctl stop mysqld

redis
版本	5.0.7 X64
端口和密码	端口：6579  密码：bH360891
本地连接IP	172.31.123.123
部署路径	/usr/local/redis/
配置文件	/usr/local/redis/etc/redis.conf
日志文件	/usr/local/redis/logs/redis.log
启动关闭	启动： sudo -u redis /usr/local/redis/bin/redis-server /usr/local/redis/etc/redis.conf关闭:kill 进程号

2021-01-09
1. 删除EncryptUtil测试数据
2. 删除重复序列号的账号（物理删除）
3. 对用户表加上唯一索引
4. 对用户注册加上分布式锁
5. 切割统计-今日切割数量（userecordrespositry）
6. MyControllerAdvice 打印异常信息
7. RedisLock 修复Bug