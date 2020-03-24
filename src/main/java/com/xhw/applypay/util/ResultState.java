package com.xhw.applypay.util;

/**
 * @author Somer
 * @date 2018-04-18 15:27
 **/
public class ResultState {

    // 请求成功
    public static final Integer OK = 200;
    public static final String OK_MESSAGE = "请求成功";

    // 请求内容错误
    public static final Integer CONTENT_ERROR = 201;
    public static final String CONTENT_ERROR_MESSAGE = "请求错误";

    // 内部错误
    public static final Integer INNER_ERROR = 202;
    public static final String INNER_ERROR_MESSAGE = "内部错误";

    // 没有权限
    public static final Integer PERM_ERROR = 401;
    public static final String NO_PERMISSION = "没有访问权限";

    // 权限不足
    public static final Integer JURISDICTION = 403;

    // 失败
    public static final Integer FAIL = 400;

    // 添加成功
    public static final String ADD_SUCCESS = "添加成功";

    // 更新成功
    public static final String UPDATE_SUCCESS = "更新成功";

    // 获取成功
    public static final String GET_SUCCESS = "获取成功";
    public static final String NO_DATA = "暂无数据";

    // 删除成功
    public static final String DELETE_SUCCESS = "删除成功";

    // 数值过大
    public static final String OVERSIZE = "数值过大";

    // 数值类型不匹配
    public static final String TYPE_MISMATCH = "数值类型不匹配";

    // 库存不足
    public static final String KUCUN_ERROR = "库存不足";

    public static final String USER_ALREADY_LOGIN_IN = "用户已登录";
    public static final String USER_NOT_LOGIN_IN = "用户未登录";

}
