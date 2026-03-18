package com.market.account.App;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableApolloConfig
public class MarketOneErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketOneErpApplication.class, args);
    }

}
