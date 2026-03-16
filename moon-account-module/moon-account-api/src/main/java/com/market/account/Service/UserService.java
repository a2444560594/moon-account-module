package com.market.account.Service;

import com.market.account.Dto.UserDTO;
import com.market.account.Vo.RpcResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    RpcResult<UserDTO> getUserInfoByUserId(int userId);

    RpcResult<UserDTO> getUserInfoByAccountId(int accountId);

    RpcResult<List<UserDTO>> getUsersByLevel(int minLevel, int maxLevel);

    RpcResult<UserDTO> addUserLevelPoint(int userId, int addPoint);

    RpcResult<Boolean> addBatchUserLevelPoint(List<Integer> userIdList, int addPoint);

    RpcResult<List<UserDTO>> fuzzySearchUsers(String keyword);

    RpcResult<List<UserDTO>> getUserFollows(int userId);

    RpcResult<List<UserDTO>> getUserFans(int userId);

    RpcResult<Boolean> followUser(int userId);

    RpcResult<Boolean> cancelFollowUser(int userId);

    RpcResult<Boolean> createUserInfo(UserDTO userDTO);

    RpcResult<Boolean> updateUserInfo(UserDTO userDTO);

}
