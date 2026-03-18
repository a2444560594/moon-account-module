package com.market.account.Service;

import com.market.account.Dto.LevelDTO;
import com.market.account.Vo.RpcResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LevelService {

    RpcResult<LevelDTO> getLevelInfo(int level);

    RpcResult<List<LevelDTO>> getAllLevelInfo();

    RpcResult<LevelDTO> getLevelInfoByPoint(long point);

}
