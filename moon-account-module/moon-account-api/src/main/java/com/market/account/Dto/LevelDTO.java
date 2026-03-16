package com.market.account.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LevelDTO {

    // 等级id
    private Integer level;
    // 等级名称
    private String levelName;
    // 最低等级积分
    private Long minLevelPoint;
    // 最高等级积分
    private Long maxLevelPoint;

}
