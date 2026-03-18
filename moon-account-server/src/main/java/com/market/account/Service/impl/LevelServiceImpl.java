package com.market.account.Service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.market.account.Dto.LevelDTO;
import com.market.account.Enum.RpcResultEnum;
import com.market.account.Mapper.LevelMapper;
import com.market.account.Redis.LevelRedisSource;
import com.market.account.Service.LevelService;
import com.market.account.Vo.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@DubboService(version = "1.0.0")
public class LevelServiceImpl implements LevelService {

    @Autowired
    LevelRedisSource levelRedisSource;

    @Autowired
    LevelMapper levelMapper;

    @Override
    public RpcResult<LevelDTO> getLevelInfo(int level) {
        try {
            log.info("获取等级信息：{}", level);
            LevelDTO levelDTO;

            String levelJsonStr = levelRedisSource.getLevelCacheByLevel(level);
            if (levelJsonStr != null) {
                levelDTO = JSONObject.parseObject(levelJsonStr, LevelDTO.class);
                return RpcResult.success(levelDTO);
            }

            levelDTO = levelMapper.getLevelInfo(level);
            if (levelDTO == null) {
                return RpcResult.fail(RpcResultEnum.NOT_FIND_LEVEL.getCode(), RpcResultEnum.NOT_FIND_LEVEL.getMessage());
            }

            levelJsonStr = JSONObject.toJSONString(levelDTO);
            levelRedisSource.setLevelCacheByLevel(level, levelJsonStr);

            return RpcResult.success(levelDTO);
        } catch (Exception e) {
            log.error("getLevelInfo userId:{} ,error:{}", level, e.getMessage());
        }
        return RpcResult.fail();
    }

    @Override
    public RpcResult<List<LevelDTO>> getAllLevelInfo() {
        try {
            log.info("获取所有等级信息");
            List<LevelDTO> levelDTOList;

            Map<String, String> levelMap = levelRedisSource.getLevelCache();
            if (levelMap != null) {
                levelDTOList = levelMap.values().stream().map(levelJsonStr -> JSONObject.parseObject(levelJsonStr, LevelDTO.class)).toList();
                return RpcResult.success(levelDTOList);
            }

            levelDTOList = levelMapper.getLevelList();
            if (levelDTOList == null) {
                return RpcResult.fail(RpcResultEnum.NOT_FIND_LEVEL.getCode(), RpcResultEnum.NOT_FIND_LEVEL.getMessage());
            }

            levelMap = levelDTOList.stream().collect(Collectors.toMap(levelDTO -> String.valueOf(levelDTO.getLevel()), JSONObject::toJSONString));
            levelRedisSource.setLevelCache(levelMap);

            return RpcResult.success(levelDTOList);
        } catch (Exception e) {
            log.error("getAllLevelInfo error:{}", e.getMessage());
        }
        return RpcResult.fail();
    }

    @Override
    public RpcResult<LevelDTO> getLevelInfoByPoint(long point) {
        try {
            log.info("根据积分获取等级信息：{}", point);
            RpcResult<List<LevelDTO>> levelListResult = getAllLevelInfo();
            if (!levelListResult.getCode().equals(RpcResultEnum.SUCCESS.getCode())) {
                return RpcResult.fail(levelListResult.getCode(), levelListResult.getMessage());
            }
            List<LevelDTO> levelDTOList = levelListResult.getData();
            for (LevelDTO levelDTO : levelDTOList) {
                if (point >= levelDTO.getMinLevelPoint() && point < levelDTO.getMaxLevelPoint()) {
                    return RpcResult.success(levelDTO);
                }
            }
            return RpcResult.fail(RpcResultEnum.NOT_FIND_LEVEL.getCode(), RpcResultEnum.NOT_FIND_LEVEL.getMessage());
        } catch (Exception e) {
            log.error("getLevelInfoByPoint point:{} ,error:{}", point, e.getMessage());
        }
        return RpcResult.fail();
    }
}
