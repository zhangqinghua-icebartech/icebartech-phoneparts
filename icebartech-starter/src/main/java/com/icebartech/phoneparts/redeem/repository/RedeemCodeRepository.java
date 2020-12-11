package com.icebartech.phoneparts.redeem.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.redeem.po.RedeemCode;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author pc
 * @Date 2019-08-28T15:38:14.870
 * @Description 兑换码
 */

public interface RedeemCodeRepository extends BaseRepository<RedeemCode> {

    @Query(nativeQuery = true, value = "SELECT s.redeem_id FROM redeem_code s WHERE s.email = ?1")
    List<Long> findRedeemIdList(String email);

    @Query(nativeQuery = true, value = "select code, title, state, email, gmt_created as gmtCreated from redeem_code where is_deleted = 'n' and redeem_id = ?1")
    List<Map<String, Object>>  exportData(Long redeemId);
}
