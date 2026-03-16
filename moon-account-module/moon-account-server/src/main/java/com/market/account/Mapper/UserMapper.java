package com.market.account.Mapper;


import com.market.account.Dto.UserDTO;
import com.market.account.Entity.UserEntity;

import java.util.List;

public interface UserMapper {

    UserDTO getUserByAccountId(int accountId);

    UserDTO getUserByUserId(int userId);

    List<UserDTO> getBatchUserByUserId(List<Integer> userIdList);

    List<UserDTO> getUsersByLevel(List<Integer> levelList);

    UserDTO addUserLevelPoint(int userId, int addPoint);

    boolean createUser(UserEntity userEntity);

    boolean updateUser(UserEntity userEntity);
}
