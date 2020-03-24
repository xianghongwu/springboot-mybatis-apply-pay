package com.xhw.applypay.util;

/**
 * Ajax返回统一封装
 *
 * @author Somer
 * @date 2018-05-25 15:56
 */
public class AjaxResult {

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 数据总条数
     */
    private Integer total;

    public AjaxResult() {
    }

    public AjaxResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public AjaxResult(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public AjaxResult(Integer code, String message, Object data, Integer total) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.total = total;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "AjaxResult [code=" + code + ", message=" + message + ", data=" + data + ", total=" + total + "]";
    }
    
}