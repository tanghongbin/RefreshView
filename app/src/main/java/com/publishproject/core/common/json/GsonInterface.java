package com.publishproject.core.common.json;

import java.util.List;

/**
 * @author Android客户端组-tanghongbin
 * @Title: GsonInterface
 * @Package com.commonconfig.jsonconfigs
 * @Description: TODO
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/8 15:54
 */
public interface GsonInterface {
    <T> T parseObject(String json, T t);
    <T> String toJSONString(T t);
    <T> List<T> parseJSONArray(String jsonArrat, T t);
}
