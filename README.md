# dubbo-trace
Dubbo调用链Demo，基于Spring Boot + Dubbo + 调用链，根据调用链id可将微服务各模块日志串联在一起。

访问路径：http://127.0.0.1:7001/sys/id/findNextId
用户名/密码：admin/admin

Service日志埋点样例：

	public Long findNextId() {
		long id = idWorker.nextId();
		logger.info("findNextId is {}", id);
		return id;
	}

日志输出样例：

2018-08-30 23:46:13,218|INFO|DubboServerHandler-10.10.0.129:20880-thread-2|dubbo-trace-service-core|com.fushihua.service.impl.SysIdServiceImpl|findNextId|admin|e04b327e-7ed6-48d6-9f0b-e3f7184f529e|1535643973200|findNextId is 484871496389361664|$|$|

注解：以“|”为分隔符，第4个字段“dubbo-trace-service-core“为微服务应用名，第7个字段“admin”为用户名，第8个字段“e04b327e-7ed6-48d6-9f0b-e3f7184f529e”为调用链id，第9个字段“1535643973200”为调用序号。
