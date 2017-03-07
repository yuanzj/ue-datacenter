package net.rokyinfo.basedao.mapper;

import net.rokyinfo.basedao.entity.FirmExchangeUser;

import java.util.List;

/**
 * Created by Administrator on 2017-02-24.
 */
public interface FirmExchangeUserMapper {

    public FirmExchangeUser getFirmExchangeUserByUsername(String username);

    public List<FirmExchangeUser> getAllFirmExchangeUser();

}
