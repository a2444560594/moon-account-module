package com.market.account.Mapper;

import com.market.account.Dto.LevelDTO;

import java.util.List;

public interface LevelMapper {

    LevelDTO getLevelInfo(int level);

    List<LevelDTO> getLevelList();
}
