package com.icebartech.phoneparts.product.service.impl;

import com.google.common.collect.ImmutableMap;
import com.icebartech.core.modules.AbstractService;
import com.icebartech.phoneparts.product.dto.UseRecordDTO;
import com.icebartech.phoneparts.product.param.UseRecordInsertParam;
import com.icebartech.phoneparts.product.param.UseRecordProductPageParam;
import com.icebartech.phoneparts.product.param.UseRecordUserPageParam;
import com.icebartech.phoneparts.product.po.UseRecord;
import com.icebartech.phoneparts.product.repository.UseRecordRepository;
import com.icebartech.phoneparts.product.service.ProductService;
import com.icebartech.phoneparts.product.service.UseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Created by liuao on 2019/10/16.
 * @desc
 */
@Service
public class UseRecordServiceImpl extends AbstractService
                                                  <UseRecordDTO, UseRecord, UseRecordRepository> implements UseRecordService {

    @Autowired
    private ProductService productService;
    @Autowired
    private UseRecordRepository repository;

    @Override
    protected void warpDTO(Long id, UseRecordDTO useRecord) {
        useRecord.setProduct(productService.findOneOrNull(useRecord.getProductId()));
    }

    @Override
    @Async
    public void add(Long userId, Long productId, Long agentId, Long secondAgentId) {
        super.insert(new UseRecordInsertParam(userId, productId, agentId, secondAgentId));
    }

    @Override
    public Page<Map> findUserRecord(UseRecordUserPageParam param) {
        Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPageSize());
        if (param.getUseCountDESC()) {
            return repository.findUserRecordDESC(param, pageable);
        }
        return repository.findUserRecordASC(param, pageable);
    }

    @Override
    public Page<Map> findUserRecord1(UseRecordUserPageParam param) {
        Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPageSize());
        return repository.findUserRecordDesc1(param, pageable);
    }

    @Override
    public Page<Map> findProductRecord(UseRecordProductPageParam param) {
        Pageable pageable = PageRequest.of(param.getPageIndex() - 1, param.getPageSize());
        if (param.getUseCountDESC()) {
            return repository.findProductRecordDESC(param, pageable);
        }
        return repository.findProductRecordASC(param, pageable);
    }

    @Override
    public Map<String, Object> findUserRecordCount(UseRecordUserPageParam param) {
        Integer countRecord = repository.findUserCountRecord(param);
        Map<String, Object> map = new HashMap<>();
        map.put("countRecord", countRecord);
        return map;
    }

    @Override
    public List<Map<String, Object>> find_day_stats(Long userId) {


        LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate findish = LocalDate.now();


        List<String> list = new ArrayList<>();
        long distance = ChronoUnit.DAYS.between(start, findish);
        Stream.iterate(start, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> list.add(f.toString()));

        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> maps = repository.find_day_stats(userId);
        for (String date : list) {
            result.add(ImmutableMap.of("date", date,
                                       "count",
                                       maps.stream().filter(m -> m.get("date").equals(date))
                                           .map(m -> m.get("count")).findAny().orElse(0)));
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> find_month_stats(Long userId) {
        LocalDate start = LocalDate.now().minusDays(6).with(TemporalAdjusters.firstDayOfYear());
        LocalDate findish = LocalDate.now();

        System.out.println("start: " + start);
        System.out.println("end  : " + findish);

        List<String> list = new ArrayList<>();

        long distance = ChronoUnit.MONTHS.between(start, findish);
        Stream.iterate(start, d -> d.plusMonths(1)).limit(distance + 1).forEach(f -> list.add(f.format(DateTimeFormatter.ofPattern("yyyy-MM"))));


        List<Map<String, Object>> result = new ArrayList<>();
        List<Map<String, Object>> maps = repository.find_month_stats(userId);
        for (String date : list) {
            result.add(ImmutableMap.of("date", date,
                                       "count",
                                       maps.stream().filter(m -> m.get("date").equals(date))
                                           .map(m -> m.get("count")).findAny().orElse(0)));
        }

        return result;
    }

    public static void main(String[] args) {
        LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
        LocalDate findish = LocalDate.now();

        System.out.println("start: " + start);
        System.out.println("end  : " + findish);

        List<String> list = new ArrayList<>();

        long distance = ChronoUnit.MONTHS.between(start, findish);
        Stream.iterate(start, d -> d.plusMonths(1)).limit(distance + 1).forEach(f -> list.add(f.format(DateTimeFormatter.ofPattern("yyyy-MM"))));

        System.out.println(list);
    }
}

