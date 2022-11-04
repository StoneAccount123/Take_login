package edu.whu.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Result {
    //状态码 0-错误 1-正常
    private int code;

    //数据
    private Object data;

    //错误信息
    private String msg;

    public static Result success(Object obj){
        return new Result().setCode(1).setData(obj);
    }

    public static Result Fail(String msg){
        return new Result().setCode(0).setMsg(msg);
    }

}
