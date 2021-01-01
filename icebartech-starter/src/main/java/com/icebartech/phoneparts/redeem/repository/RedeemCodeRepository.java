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

    @Query(nativeQuery = true, value = "SELECT " +
                                       "rd.code, " +
                                       "rd.title, " +
                                       "rd.state," +
                                       "rd.email," +
                                       "rd.gmt_created AS gmtCreated, " +
                                       "rd.use_num as useNum, " +
                                       "a.class_name as agentName " +
                                       "FROM " +
                                       "redeem_code rd " +
                                       "left join redeem r on r.id = rd.redeem_id " +
                                       "left join agent a on a.id = r.agent_id " +
                                       "WHERE rd.is_deleted = 'n' " +
                                       "AND rd.redeem_id = ?1")
    List<Map<String, Object>>  exportData(Long redeemId);
}
