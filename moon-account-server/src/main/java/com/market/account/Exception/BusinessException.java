package com.market.account.Exception;

import com.market.account.Enum.RpcResultEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final RpcResultEnum error;

    public BusinessException(RpcResultEnum error) {
        super(error.getMessage());
        this.error = error;
    }

}
