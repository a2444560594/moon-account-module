package com.market.account.Service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.market.account.Config.UserConfig;
import com.market.account.Dto.LevelDTO;
import com.market.account.Dto.UserDTO;
import com.market.account.Enum.RpcResultEnum;
import com.market.account.Mapper.FollowMapper;
import com.market.account.Mapper.LevelMapper;
import com.market.account.Mapper.UserMapper;
import com.market.account.Redis.UserRedisSource;
import com.market.account.Service.UserService;
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
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    LevelMapper levelMapper;

    @Autowired
    FollowMapper followMapper;

    @Autowired
    UserRedisSource userRedisSource;

    @Autowired
    UserConfig userConfig;

    @Override
    public RpcResult<UserDTO> getUserInfoByUserId(int userId) {
        try {
            log.info("根据用户id查找用户信息！userId:{}", userId);
            UserDTO userDTO;
            // 缓存中获取账户信息
            String userJsonStr = userRedisSource.getUserCacheByUserId(userId);
            if (userJsonStr != null) {
                userDTO = JSONObject.parseObject(userJsonStr, UserDTO.class);
                return RpcResult.success(userDTO);
            }

            // 获取账户信息
            userDTO = userMapper.getUserByUserId(userId);
            if (userDTO == null) {
                return RpcResult.fail(RpcResultEnum.NOT_FIND_USER.getCode(), RpcResultEnum.NOT_FIND_USER.getMessage());
            }

            // 缓存账户信息
            userJsonStr = JSONObject.toJSONString(userDTO);
            userRedisSource.setUserCacheByUserId(userId, userJsonStr);

            return RpcResult.success(userDTO);
        } catch (Exception e) {
            log.error("getUserInfoByUserId userId:{} ,error:{}", userId, e.getMessage());
        }
        return RpcResult.fail();
    }

    @Override
    public RpcResult<UserDTO> getUserInfoByAccountId(int accountId) {
        try {
            log.info("根据账户id查找用户信息！accountId:{}", accountId);
            UserDTO userDTO;
            // 缓存中获取账户信息
            String userJsonStr = userRedisSource.getUserCacheByAccountId(accountId);
            if (userJsonStr != null) {
                userDTO = JSONObject.parseObject(userJsonStr, UserDTO.class);
                return RpcResult.success(userDTO);
            }

            // 获取账户信息
            userDTO = userMapper.getUserByAccountId(accountId);
            if (userDTO == null) {
                return RpcResult.fail(RpcResultEnum.NOT_FIND_USER.getCode(), RpcResultEnum.NOT_FIND_USER.getMessage());
            }

            // 缓存账户信息
            userJsonStr = JSONObject.toJSONString(userDTO);
            userRedisSource.setUserCacheByAccountId(accountId, userJsonStr);

            return RpcResult.success(userDTO);
        } catch (Exception e) {
            log.error("getUserInfoByAccountId accountId:{} ,error:{}", accountId, e.getMessage());
        }
        return RpcResult.fail();
    }

    @Override
    public RpcResult<List<UserDTO>> getUsersByLevel(int minLevel, int maxLevel) {
        try {
            log.info("根据等级查找用户信息！minLevel:{},maxLevel:{}", minLevel, maxLevel);

            // 获取等级信息
            List<LevelDTO> levelList = levelMapper.getLevelList();
            LevelDTO minLevelInfo = levelList.stream().filter(levelDTO -> levelDTO.getLevel() == minLevel).findFirst().orElse(null);
            if (minLevelInfo == null) {
                return RpcResult.fail(RpcResultEnum.NOT_FIND_LEVEL.getCode(), RpcResultEnum.NOT_FIND_LEVEL.getMessage());
            }

            LevelDTO maxLevelInfo = levelList.stream().filter(levelDTO -> levelDTO.getLevel() == maxLevel).findFirst().orElse(null);
            if (maxLevelInfo == null) {
                return RpcResult.fail(RpcResultEnum.NOT_FIND_LEVEL.getCode(), RpcResultEnum.NOT_FIND_LEVEL.getMessage());
            }

            // 缓存中获取账户信息
            List<Integer> lackLevelList = Lists.newArrayList();
            List<UserDTO> userDTOList = Lists.newArrayList();
            for (int level = minLevel; level <= maxLevel; level++) {
                List<String> usersJson = userRedisSource.getUserCacheByLevel(level);
                if (usersJson != null && !usersJson.isEmpty()) {
                    userDTOList.addAll(usersJson.stream().map(json -> JSONObject.parseObject(json, UserDTO.class)).toList());
                } else {
                    // 记录缺少的等级缓存
                    lackLevelList.add(level);
                }
            }

            // 获取缺少的对应等级的账户信息
            userDTOList.addAll(userMapper.getUsersByLevel(lackLevelList));

            // 刷新缓存
            for (Integer level : lackLevelList) {
                LevelDTO levelDTO = levelList.stream().filter(dto -> dto.getLevel().equals(level)).findFirst().orElse(null);
                if (levelDTO == null) {
                    continue;
                }
                Map<String, String> userLevelCacheMap = userDTOList.stream().filter(userDTO -> userDTO.getUserLevelPoint() >= levelDTO.getMinLevelPoint() && userDTO.getUserLevelPoint() <= levelDTO.getMaxLevelPoint()).collect(Collectors.toMap(userDTO -> userDTO.getUserId().toString(), JSONObject::toJSONString));
                userRedisSource.setUserCacheByLevel(level, userLevelCacheMap);
            }

            return RpcResult.success(userDTOList);
        } catch (Exception e) {
            log.error("getUsersByLevel minLevel:{},maxLevel:{} ,error:{}", minLevel, maxLevel, e.getMessage());
        }

        return RpcResult.fail();
    }

    @Override
    public RpcResult<UserDTO> addUserLevelPoint(int userId, int addPoint) {
        try {
            log.info("增加用户等级积分！userId:{},addPoint:{}", userId, addPoint);

            if (addPoint <= 0) {
                return RpcResult.fail(RpcResultEnum.ADD_LEVEL_POINT_LESS_ZERO.getCode(), RpcResultEnum.ADD_LEVEL_POINT_LESS_ZERO.getMessage());
            }

            RpcResult<UserDTO> resultReturn = getUserInfoByUserId(userId);
            if (!resultReturn.getCode().equals(RpcResultEnum.SUCCESS.getCode())) {
                return RpcResult.fail(resultReturn.getCode(), resultReturn.getMessage());
            }

            // 增加用户等级积分
            UserDTO userDTO = userMapper.addUserLevelPoint(userId, addPoint);

            // 删除用户缓存
            userRedisSource.removeUserCacheByUserId(userId);
            userRedisSource.removeUserCacheByAccountId(userDTO.getAccountId());

            // 清除旧的等级缓存
            LevelDTO oldLevelDTO = levelMapper.getLevelByLevelPoint(userDTO.getUserLevelPoint() - addPoint);
            userRedisSource.removeUserCacheByLevel(oldLevelDTO.getLevel());

            // 清除新的等级缓存
            LevelDTO levelDTO = levelMapper.getLevelByLevelPoint(userDTO.getUserLevelPoint());
            userRedisSource.removeUserCacheByLevel(levelDTO.getLevel());

            // 发送mq积分消息

            // 发送升级消息

            return RpcResult.success(userDTO);
        } catch (Exception e) {
            log.error("addUserLevelPoint userId:{},addPoint:{} ,error:{}", userId, addPoint, e.getMessage());
        }
        return RpcResult.fail();
    }

    @Override
    public RpcResult<Boolean> addBatchUserLevelPoint(List<Integer> userIdList, int addPoint) {
        return null;
    }

    @Override
    public RpcResult<List<UserDTO>> fuzzySearchUsers(String keyword) {
        return null;
    }

    @Override
    public RpcResult<List<UserDTO>> getUserFollows(int userId) {
        try {
            log.info("根据用户id查找用户关注列表！userId:{}", userId);
            List<UserDTO> userDTOList;

            List<String> usersJson = userRedisSource.getUserFollows(userId);
            if (usersJson != null) {
                userDTOList = usersJson.stream().map(json -> JSONObject.parseObject(json, UserDTO.class)).collect(Collectors.toList());
                return RpcResult.success(userDTOList);
            }

            // 获取用户关注列表
            List<Integer> userIdList = followMapper.getUserFollowIdList(userId);
            if (userIdList == null || userIdList.isEmpty()) {
                return RpcResult.success(Lists.newArrayList());
            }
            userDTOList = userMapper.getBatchUserByUserId(userIdList);

            userRedisSource.setUserFollows(userId, userDTOList.stream().map(JSONObject::toJSONString).toList());

            return RpcResult.success(userDTOList);
        } catch (Exception e) {
            log.error("getUserFollows userId:{} ,error:{}", userId, e.getMessage());
        }
        return null;
    }

    @Override
    public RpcResult<List<UserDTO>> getUserFans(int userId) {
        return null;
    }

    @Override
    public RpcResult<Boolean> followUser(int userId) {
        return null;
    }

    @Override
    public RpcResult<Boolean> cancelFollowUser(int userId) {
        return null;
    }

    @Override
    public RpcResult<Boolean> createUserInfo(UserDTO userDTO) {
        return null;
    }

    @Override
    public RpcResult<Boolean> updateUserInfo(UserDTO userDTO) {
        return null;
    }
}
