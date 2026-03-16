package com.market.account.Vo;

import com.market.account.Enum.RpcResultEnum;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class RpcResult<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> RpcResult<T> success(){
        RpcResult<T> RpcResult = new RpcResult<>();
        RpcResult.setCode(RpcResultEnum.SUCCESS.getCode());
        RpcResult.setMessage(RpcResultEnum.SUCCESS.getMessage());
        return RpcResult;
    }
    public static <T> RpcResult<T> success(T data){
        RpcResult<T> RpcResult = new RpcResult<>();
        RpcResult.setCode(RpcResultEnum.SUCCESS.getCode());
        RpcResult.setMessage(RpcResultEnum.SUCCESS.getMessage());
        RpcResult.setData(data);
        return RpcResult;
    }

    public static <T> RpcResult<T> fail(){
        RpcResult<T> RpcResult = new RpcResult<>();
        RpcResult.setCode(RpcResultEnum.FAIL.getCode());
        RpcResult.setMessage(RpcResultEnum.FAIL.getMessage());
        return RpcResult;
    }

    public static <T> RpcResult<T> fail(Integer code, String message){
        RpcResult<T> RpcResult = new RpcResult<>();
        RpcResult.setCode(code);
        RpcResult.setMessage(message);
        return RpcResult;
    }
    public static <T> RpcResult<T> fail(String message){
        RpcResult<T> RpcResult = new RpcResult<>();
        RpcResult.setCode(null);
        RpcResult.setMessage(message);
        return RpcResult;
    }

    public static <T> RpcResult<T> fail(Integer code, String message, T data){
        RpcResult<T> RpcResult = new RpcResult<>();
        RpcResult.setCode(code);
        RpcResult.setMessage(message);
        RpcResult.setData(data);
        return RpcResult;
    }


}
