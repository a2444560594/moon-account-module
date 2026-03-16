package com.market.account.Service;

import com.market.account.Dto.AccountDTO;
import com.market.account.Entity.AccountEntity;
import com.market.account.Vo.RpcResult;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface AccountService {

    RpcResult<AccountDTO> getAccountByAccountId(int accountId);

    RpcResult<AccountDTO> getAccountByContact(int contactType, String contact);

    RpcResult<AccountDTO> getAccountByLoginAccount(int loginType, String loginKey, String password);

    RpcResult<Set<String>> getAllAccountsByContactType(int contactType);

    RpcResult<Boolean> registerAccount(AccountEntity accountEntity);

    RpcResult<Boolean> updateAccountPassword(int accountId, String loginPassword);

    RpcResult<Boolean> updateAccountContact(int contactType, int accountId, String contact);
}
