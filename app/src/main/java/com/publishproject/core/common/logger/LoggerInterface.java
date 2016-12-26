package com.publishproject.core.common.logger;

/**
 * @author Android客户端组-tanghongbin
 * @Title: LoggerInterface
 * @Package com.publishproject.core.commonconfig.loggerconfigs
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/15 11:41
 * @Description: TODO
 */
public interface LoggerInterface {
    void i(String tag,String o);
    void e(String tag,String o);
    void detail(Class c,String name,String error);
    boolean isDebug();
}
