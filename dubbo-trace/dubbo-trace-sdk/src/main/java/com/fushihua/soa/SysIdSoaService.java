package com.fushihua.soa;

/** 
 * @ClassName: SysIdService 
 * @Description: 系统序列号生成 
 * @author fushihua
 * @date 2017年12月5日 下午3:37:00 
 */
public interface SysIdSoaService {

	/**
	 * @Description: 获取序列号
	 * @return
	 * @author fushihua
	 * @Time 2017年12月5日 下午3:38:54
	 */
	Long findNextId();
}
