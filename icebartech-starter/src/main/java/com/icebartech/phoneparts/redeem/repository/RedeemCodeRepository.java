package com.icebartech.phoneparts.redeem.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.redeem.po.RedeemCode;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author pc
 * @Date 2019-08-28T15:38:14.870
 * @Description 兑换码
 */

public interface RedeemCodeRepository extends BaseRepository<RedeemCode> {

    @Query(nativeQuery = true, value = "SELECT s.redeem_id FROM redeem_code s WHERE s.email = ?1")
    List<Long> findRedeemIdList(String email);
}
