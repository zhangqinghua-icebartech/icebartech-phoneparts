package com.icebartech.phoneparts.product.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.product.po.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author pc
 * @Date 2019-06-18T11:11:51.866
 * @Description 单品表
 */

public interface ProductRepository extends BaseRepository<Product> {

    @Query(nativeQuery = true, value = "select id, china_name as chinaName from sys_class_one where is_deleted = 'n' and id in (?1)")
    List<Map<String, Object>> oneClassNames(List<Long> sysClassOneIds);

    @Query(nativeQuery = true, value = "select id, china_name as chinaName from sys_class_two where is_deleted = 'n' and id in (?1)")
    List<Map<String, Object>> twoClassNames(List<Long> sysClassTwoIds);

    @Query(nativeQuery = true, value = "select id, china_name as chinaName from sys_class_three where is_deleted = 'n' and id in (?1)")
    List<Map<String, Object>> threeClassNames(List<Long> sysClassThreeIds);
}
