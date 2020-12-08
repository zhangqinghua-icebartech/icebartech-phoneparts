package com.icebartech.phoneparts.product.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.product.param.UseRecordProductPageParam;
import com.icebartech.phoneparts.product.param.UseRecordUserPageParam;
import com.icebartech.phoneparts.product.po.UseRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Map;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
public interface UseRecordRepository extends BaseRepository<UseRecord> {
    @Query(nativeQuery = true, value = "SELECT u.serial_num as serialNum,u.email as email,ur.user_id as userId,COUNT(*) as useCount FROM use_record ur LEFT JOIN `user` u ON ur.user_id = u.id WHERE ur.gmt_created >= :#{#param.strTime} AND ur.gmt_created <= :#{#param.endTime}  AND if(:#{#param.agentId} is null, 1=1, ur.agent_id = :#{#param.agentId}) AND if(:#{#param.secondAgentId} is null, 1=1, ur.second_agent_id = :#{#param.secondAgentId}) AND u.serial_num LIKE %:#{#param.serialNum}% AND u.email LIKE %:#{#param.email}% GROUP BY ur.user_id ORDER BY useCount ASC"
            ,countQuery = "select * from (SELECT COUNT(*) FROM use_record ur LEFT JOIN `user` u ON ur.user_id = u.id WHERE ur.gmt_created >= :#{#param.strTime} AND ur.gmt_created <= :#{#param.endTime}  AND if(:#{#param.agentId} is null, 1=1, ur.agent_id = :#{#param.agentId}) AND if(:#{#param.secondAgentId} is null, 1=1, ur.second_agent_id = :#{#param.secondAgentId}) AND u.serial_num LIKE %:#{#param.serialNum}% AND u.email LIKE %:#{#param.email}% GROUP BY ur.user_id) a")
    Page<Map> findUserRecordASC(@Param("param") UseRecordUserPageParam param, Pageable pageable);


    @Query(nativeQuery = true, value = "SELECT u.serial_num as serialNum,u.email as email,ur.user_id as userId,COUNT(*) as useCount FROM use_record ur LEFT JOIN `user` u ON ur.user_id = u.id WHERE ur.gmt_created >= :#{#param.strTime} AND ur.gmt_created <= :#{#param.endTime}  AND if(:#{#param.agentId} is null, 1=1, ur.agent_id = :#{#param.agentId}) AND if(:#{#param.secondAgentId} is null, 1=1, ur.second_agent_id = :#{#param.secondAgentId}) AND u.serial_num LIKE %:#{#param.serialNum}% AND u.email LIKE %:#{#param.email}% GROUP BY ur.user_id ORDER BY useCount DESC"
            ,countQuery = "select * from (SELECT COUNT(*) FROM use_record ur LEFT JOIN `user` u ON ur.user_id = u.id WHERE ur.gmt_created >= :#{#param.strTime} AND ur.gmt_created <= :#{#param.endTime}  AND if(:#{#param.agentId} is null, 1=1, ur.agent_id = :#{#param.agentId}) AND if(:#{#param.secondAgentId} is null, 1=1, ur.second_agent_id = :#{#param.secondAgentId}) AND u.serial_num LIKE %:#{#param.serialNum}% AND u.email LIKE %:#{#param.email}% GROUP BY ur.user_id) a")
    Page<Map> findUserRecordDESC(@Param("param") UseRecordUserPageParam param, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT u.serial_num AS serialNum, u.email AS email, one.china_name as classOne, two.china_name as classTwo, three.china_name as classThree, p.china_name as product, ur.user_id AS userId, ur.gmt_created as gmtCreated FROM use_record ur LEFT JOIN `user` u ON ur.user_id = u.id LEFT JOIN `product` p ON ur.product_id = p.id LEFT JOIN `sys_class_one` one ON p.class_one_id = one.id LEFT JOIN `sys_class_two` two ON p.class_two_id = two.id LEFT JOIN `sys_class_three` three ON p.class_three_id = three.id WHERE ur.gmt_created >= :#{#param.strTime} AND ur.gmt_created <= :#{#param.endTime}  AND if(:#{#param.userId} is null, 1=1, ur.user_id = :#{#param.userId}) AND if(:#{#param.agentId} is null, 1=1, ur.agent_id = :#{#param.agentId}) AND if(:#{#param.secondAgentId} is null, 1=1, ur.second_agent_id = :#{#param.secondAgentId}) AND u.serial_num LIKE %:#{#param.serialNum}% AND u.email LIKE %:#{#param.email}% ORDER BY ur.gmt_created DESC"
        ,countQuery = "select * from (SELECT COUNT(*) FROM use_record ur LEFT JOIN `user` u ON ur.user_id = u.id WHERE ur.gmt_created >= :#{#param.strTime} AND ur.gmt_created <= :#{#param.endTime} AND if(:#{#param.userId} is null, 1=1, ur.user_id = :#{#param.userId})  AND if(:#{#param.agentId} is null, 1=1, ur.agent_id = :#{#param.agentId}) AND if(:#{#param.secondAgentId} is null, 1=1, ur.second_agent_id = :#{#param.secondAgentId}) AND u.serial_num LIKE %:#{#param.serialNum}% AND u.email LIKE %:#{#param.email}%) a")
    Page<Map> findUserRecordDesc1(@Param("param") UseRecordUserPageParam param, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT s.english_name as englishBrandName,s.china_name as chinaBrandName,p.id as productId,p.china_name as chinaName,p.english_name as englishName,COUNT(*) as useCount FROM use_record u LEFT JOIN product p ON u.product_id = p.id LEFT JOIN sys_class_two s ON s.id = p.class_two_id WHERE u.user_id = :#{#param.userId} AND if(:#{#param.agentId} is null, 1=1, u.agent_id = :#{#param.agentId}) AND if(:#{#param.secondAgentId} is null, 1=1, u.second_agent_id = :#{#param.secondAgentId}) AND u.gmt_created >= :#{#param.strTime} AND u.gmt_created <= :#{#param.endTime} GROUP BY u.product_id ORDER BY useCount ASC"
        ,countQuery = "select * from (SELECT COUNT(*) FROM use_record u LEFT JOIN product p ON u.product_id = p.id LEFT JOIN sys_class_two s ON s.id = p.class_two_id WHERE u.user_id = :#{#param.userId} AND if(:#{#param.agentId} is null, 1=1, u.agent_id = :#{#param.agentId}) AND if(:#{#param.secondAgentId} is null, 1=1, u.second_agent_id = :#{#param.secondAgentId}) AND u.gmt_created >= :#{#param.strTime} AND u.gmt_created <= :#{#param.endTime} GROUP BY u.product_id) a")
    Page<Map> findProductRecordASC(@Param("param") UseRecordProductPageParam param, Pageable pageable);


    @Query(nativeQuery = true, value = "SELECT s.english_name as englishBrandName,s.china_name as chinaBrandName,p.id as productId,p.china_name as chinaName,p.english_name as englishName,COUNT(*) as useCount FROM use_record u LEFT JOIN product p ON u.product_id = p.id LEFT JOIN sys_class_two s ON s.id = p.class_two_id WHERE u.user_id = :#{#param.userId}  AND if(:#{#param.agentId} is null, 1=1, u.agent_id = :#{#param.agentId}) AND if(:#{#param.secondAgentId} is null, 1=1, u.second_agent_id = :#{#param.secondAgentId}) AND u.gmt_created >= :#{#param.strTime} AND u.gmt_created <= :#{#param.endTime} GROUP BY u.product_id ORDER BY useCount DESC"
            ,countQuery = "select * from (SELECT COUNT(*) FROM use_record u LEFT JOIN product p ON u.product_id = p.id LEFT JOIN sys_class_two s ON s.id = p.class_two_id WHERE u.user_id = :#{#param.userId}  AND if(:#{#param.agentId} is null, 1=1, u.agent_id = :#{#param.agentId}) AND if(:#{#param.secondAgentId} is null, 1=1, u.second_agent_id = :#{#param.secondAgentId}) AND u.gmt_created >= :#{#param.strTime} AND u.gmt_created <= :#{#param.endTime} GROUP BY u.product_id) a")
    Page<Map> findProductRecordDESC(@Param("param") UseRecordProductPageParam param, Pageable pageable);

    @Query(nativeQuery = true, value = "select sum(a.useCount) from (SELECT COUNT(*) as useCount FROM use_record ur LEFT JOIN `user` u ON ur.user_id = u.id WHERE ur.gmt_created >= :#{#param.strTime} AND ur.gmt_created <= :#{#param.endTime} AND if(:#{#param.userId} is null, 1=1, ur.user_id = :#{#param.userId})  AND if(:#{#param.agentId} is null, 1=1, ur.agent_id = :#{#param.agentId}) AND if(:#{#param.secondAgentId} is null, 1=1, ur.second_agent_id = :#{#param.secondAgentId}) AND u.serial_num LIKE %:#{#param.serialNum}% AND u.email LIKE %:#{#param.email}% GROUP BY ur.user_id) a")
    Integer findUserCountRecord(@Param("param") UseRecordUserPageParam param);
}
