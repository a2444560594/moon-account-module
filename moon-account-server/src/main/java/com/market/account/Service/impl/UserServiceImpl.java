package com.market.account.Service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import com.market.account.Dto.LevelDTO;
import com.market.account.Dto.UserDTO;
import com.market.account.Enum.RpcResultEnum;
import com.market.account.Mapper.FollowMapper;
import com.market.account.Mapper.UserMapper;
import com.market.account.MqProducer.MqMessageService;
import com.market.account.Redis.UserRedisSource;
import com.market.account.Service.LevelService;
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
    FollowMapper followMapper;

    @Autowired
    UserRedisSource userRedisSource;

    @Autowired
    MqMessageService mqMessageService;

    @Autowired
    LevelService levelService;


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
    public RpcResult<List<UserDTO>> getUserListByUserIds(List<Integer> userIdList) {
        try {
            log.info("根据用户id列表查找用户信息！userIdList:{}", userIdList);
            if (userIdList == null || userIdList.isEmpty()) {
                return RpcResult.fail(RpcResultEnum.NOT_FIND_USER.getCode(), RpcResultEnum.NOT_FIND_USER.getMessage());
            }

            // 需要查询的账户id
            List<Integer> searchIdList = Lists.newArrayList();

            // 获取缓存账户信息
            List<UserDTO> userDTOList = Lists.newArrayList();
            for (Integer userId : userIdList) {
                String userJsonStr = userRedisSource.getUserCacheByUserId(userId);
                if (userJsonStr != null) {
                    UserDTO userDTO = JSONObject.parseObject(userJsonStr, UserDTO.class);
                    userDTOList.add(userDTO);
                    continue;
                }
                searchIdList.add(userId);
            }

            // 获取账户信息
            List<UserDTO> searchUserDTOList = userMapper.getBatchUserByUserId(searchIdList);
            if (searchUserDTOList == null || searchUserDTOList.isEmpty()) {
                return RpcResult.fail(RpcResultEnum.NOT_FIND_USER.getCode(), RpcResultEnum.NOT_FIND_USER.getMessage());
            }

            // 缓存账户信息
            searchUserDTOList.forEach(userDTO -> {
                String userJsonStr = userRedisSource.getUserCacheByUserId(userDTO.getUserId());
                userRedisSource.setUserCacheByUserId(userDTO.getUserId(), userJsonStr);
            });

            // 合并缓存和查询的账户信息
            userDTOList.addAll(searchUserDTOList);

            return RpcResult.success(userDTOList);
        } catch (Exception e) {
            log.error("getUserListByUserIds userIdList:{} ,error:{}", userIdList, e.getMessage());
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
            RpcResult<List<LevelDTO>> levelListResult = levelService.getAllLevelInfo();
            if (!levelListResult.getCode().equals(RpcResultEnum.SUCCESS.getCode())) {
                return RpcResult.fail(levelListResult.getCode(), levelListResult.getMessage());
            }
            List<LevelDTO> levelList = levelListResult.getData();

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

            RpcResult<UserDTO> userInfoResult = getUserInfoByUserId(userId);
            if (!userInfoResult.getCode().equals(RpcResultEnum.SUCCESS.getCode())) {
                return RpcResult.fail(userInfoResult.getCode(), userInfoResult.getMessage());
            }

            // 增加用户等级积分
            UserDTO userDTO = userMapper.addUserLevelPoint(userId, addPoint);

            RpcResult<LevelDTO> oldLevelInfoResult = levelService.getLevelInfoByPoint(userDTO.getUserLevelPoint() - addPoint);
            if (!oldLevelInfoResult.getCode().equals(RpcResultEnum.SUCCESS.getCode())) {
                return RpcResult.fail(oldLevelInfoResult.getCode(), oldLevelInfoResult.getMessage());
            }
            LevelDTO oldLevelDTO = oldLevelInfoResult.getData();

            RpcResult<LevelDTO> levelInfoResult = levelService.getLevelInfoByPoint(userDTO.getUserLevelPoint());
            if (!levelInfoResult.getCode().equals(RpcResultEnum.SUCCESS.getCode())) {
                return RpcResult.fail(levelInfoResult.getCode(), levelInfoResult.getMessage());
            }
            LevelDTO levelDTO = levelInfoResult.getData();

            // 发送mq积分消息
            mqMessageService.sendAsyncMqMessage(addPointTopicMessage(userDTO, addPoint));

            // 发送升级消息
            if (oldLevelDTO.getLevel() < levelDTO.getLevel()) {
                mqMessageService.sendAsyncMqMessage(addLevelTopicMessage(userDTO, oldLevelDTO.getLevel(), levelDTO.getLevel()));
            }

            // 删除用户缓存
            userRedisSource.removeUserCacheByUserId(userId);
            userRedisSource.removeUserCacheByAccountId(userDTO.getAccountId());

            // 清除旧的等级缓存
            userRedisSource.removeUserCacheByLevel(oldLevelDTO.getLevel());

            // 清除新的等级缓存
            userRedisSource.removeUserCacheByLevel(levelDTO.getLevel());

            return RpcResult.success(userDTO);
        } catch (Exception e) {
            log.error("addUserLevelPoint userId:{},addPoint:{} ,error:{}", userId, addPoint, e.getMessage());
        }
        return RpcResult.fail();
    }

    @Override
    public RpcResult<Boolean> addBatchUserLevelPoint(List<Integer> userIdList, int addPoint) {
        try {
            log.info("批量增加用户等级积分！userIdList:{},addPoint:{}", userIdList, addPoint);
            if (addPoint <= 0) {
                return RpcResult.fail(RpcResultEnum.ADD_LEVEL_POINT_LESS_ZERO.getCode(), RpcResultEnum.ADD_LEVEL_POINT_LESS_ZERO.getMessage());
            }

            // 批量获取用户信息
            RpcResult<List<UserDTO>> userListResult = getUserListByUserIds(userIdList);
            if (!userListResult.getCode().equals(RpcResultEnum.SUCCESS.getCode())) {
                return RpcResult.fail(userListResult.getCode(), userListResult.getMessage());
            }

            List<UserDTO> userDTOList = userListResult.getData();
            List<Integer> userIds = userDTOList.stream().map(UserDTO::getUserId).toList();
            List<UserDTO> addPointList = userMapper.addBatchUserLevelPoint(userIds, addPoint);

            RpcResult<List<LevelDTO>> levelListResult = levelService.getAllLevelInfo();
            if (!levelListResult.getCode().equals(RpcResultEnum.SUCCESS.getCode())) {
                return RpcResult.fail(levelListResult.getCode(), levelListResult.getMessage());
            }
            List<LevelDTO> levelDTOList = levelListResult.getData();

            for (UserDTO userDTO : addPointList) {
                // 发送积分消息
                mqMessageService.sendAsyncMqMessage(addPointTopicMessage(userDTO, addPoint));

                long oldLevelPoint = userDTO.getUserLevelPoint() - addPoint;
                LevelDTO oldLevelDTO = levelDTOList.stream().filter(dto -> dto.getMinLevelPoint() <= oldLevelPoint && oldLevelPoint <= dto.getMaxLevelPoint()).findFirst().orElse(levelDTOList.get(0));

                long nowLevelPoint = userDTO.getUserLevelPoint();
                LevelDTO nowLevelDTO = levelDTOList.stream().filter(dto -> dto.getMinLevelPoint() <= nowLevelPoint && nowLevelPoint <= dto.getMaxLevelPoint()).findFirst().orElse(levelDTOList.get(0));

                if (oldLevelDTO.getLevel() < nowLevelDTO.getLevel()) {
                    // 发送升级消息
                    addLevelTopicMessage(userDTO, oldLevelDTO.getLevel(), nowLevelDTO.getLevel());
                }
            }

            // 批量删除用户缓存
            userRedisSource.removeUserCacheByUserIds(userIds);

            // 批量删除账户缓存
            List<Integer> accountIds = userDTOList.stream().map(UserDTO::getAccountId).toList();
            userRedisSource.removeUserCacheByAccountIds(accountIds);


        } catch (Exception e) {
            log.error("addBatchUserLevelPoint userIdList:{},addPoint:{} ,error:{}", userIdList, addPoint, e.getMessage());
        }
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

            RpcResult<List<UserDTO>> resultReturn = getUserListByUserIds(userIdList);
            if (!resultReturn.getCode().equals(RpcResultEnum.SUCCESS.getCode())) {
                return RpcResult.fail(resultReturn.getCode(), resultReturn.getMessage());
            }
            userDTOList = resultReturn.getData();

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


    private String addPointTopicMessage(UserDTO userDTO, long addPoint) {
        JSONObject sendPointMessage = new JSONObject();
        sendPointMessage.put("topic", "topic-addLevelPoint");
        sendPointMessage.put("userId", userDTO.getUserId());
        sendPointMessage.put("addPoint", addPoint);
        sendPointMessage.put("oldLevelPoint", userDTO.getUserLevelPoint() - addPoint);
        sendPointMessage.put("nowLevelPoint", userDTO.getUserLevelPoint());
        sendPointMessage.put("time", System.currentTimeMillis());
        return sendPointMessage.toJSONString();
    }

    private String addLevelTopicMessage(UserDTO userDTO, long oldLevel, long nowLevel) {
        JSONObject sendLevelMessage = new JSONObject();
        sendLevelMessage.put("topic", "topic-levelUp");
        sendLevelMessage.put("userId", userDTO.getUserId());
        sendLevelMessage.put("oldLevel", oldLevel);
        sendLevelMessage.put("nowLevel", nowLevel);
        sendLevelMessage.put("time", System.currentTimeMillis());
        return sendLevelMessage.toJSONString();
    }
}
