package com.market.account.Mapper;

import com.market.account.Dto.LevelDTO;

import java.util.List;

public interface LevelMapper {

    List<LevelDTO> getLevelList();

    LevelDTO getLevelByLevelPoint(long beforeUserLevelPoint);
}
