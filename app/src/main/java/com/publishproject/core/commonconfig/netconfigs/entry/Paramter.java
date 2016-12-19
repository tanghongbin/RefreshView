package com.publishproject.core.commonconfig.netconfigs.entry;

import java.io.Serializable;

/**
 * @author Android客户端组-tanghongbin
 * @Title: Paramter
 * @Package com.publishproject.core.commonconfig.netconfigs.entry
 * Copyright: Copyright (c) 2016
 * Company: 成都壹柒互动科技有限公司
 * @date 2016/12/13 10:17
 * @Description: TODO
 */
public class Paramter<KEY,VALUE> implements Serializable{
    private KEY key;
    private VALUE value;

    public Paramter() {
    }

    public Paramter(KEY key, VALUE value) {
        this.key = key;
        this.value = value;
    }

    public KEY getKey() {
        return key;
    }

    public void setKey(KEY key) {
        this.key = key;
    }

    public VALUE getValue() {
        return value;
    }

    public void setValue(VALUE value) {
        this.value = value;
    }
}
