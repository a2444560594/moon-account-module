package com.market.account.Mapper;

import com.market.account.Dto.BankCardDTO;

import java.util.List;

public interface BankMapper {

    public List<BankCardDTO> getBankCardListByUserId(Integer userId);

}
