package com.market.account.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
public class UserConfig {

    @Value("${user.cacheExpireTime}")
    private int cacheExpireTime;
}
