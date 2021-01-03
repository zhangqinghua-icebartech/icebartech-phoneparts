package com.icebartech.phoneparts.redeem.service.impl;

import com.icebartech.core.modules.AbstractService;
import com.icebartech.core.utils.BeanMapper;
import com.icebartech.core.utils.QRCodeUtil;
import com.icebartech.phoneparts.redeem.dto.RedeemCodeDTO;
import com.icebartech.phoneparts.redeem.excel.RedeemExports;
import com.icebartech.phoneparts.redeem.po.RedeemCode;
import com.icebartech.phoneparts.redeem.repository.RedeemCodeRepository;
import com.icebartech.phoneparts.redeem.service.RedeemCodeService;
import com.icebartech.phoneparts.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author pc
 * @Date 2019-08-28T15:38:14.870
 * @Description 兑换码
 */

@Service
public class RedeemCodeServiceImpl extends AbstractService<RedeemCodeDTO, RedeemCode, RedeemCodeRepository> implements RedeemCodeService {

    @Autowired
    UserService userService;

    @Autowired
    RedeemCodeRepository repository;

    @Override
    protected void warpDTO(Long id, RedeemCodeDTO redeemCode) {
//        if (redeemCode.getUserId()!=null){
//            redeemCode.setUser(userService.findOneOrNull(redeemCode.getUserId()));
//        }
        try {
            redeemCode.setQrCode(QRCodeUtil.qrCode(redeemCode.getCode()));
        } catch (Exception e) {
            redeemCode.setQrCode(null);
        }
    }


    @Override
    public List<Long> findRedeemIdList(String email) {
        return repository.findRedeemIdList(email);
    }

    @Override
    public List<RedeemExports> exportData(Long redeemId) {
        if (redeemId == null) return new ArrayList<>();
        return BeanMapper.map(repository.exportData(redeemId), RedeemExports.class);
    }
}