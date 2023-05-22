package com.qwqer.adplatform.net.base;

import java.io.Serializable;
/**
 * 跟据后台接口定义的数据类型。
 * @param <T>
 * @author E
 */
public class BaseRetrofitBean <T> implements Serializable {

    public int code;
    public String timestamp;
    public String msg;
    public T data;

}
