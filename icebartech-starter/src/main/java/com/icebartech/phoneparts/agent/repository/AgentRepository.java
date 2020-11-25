package com.icebartech.phoneparts.agent.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.agent.po.Agent;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface AgentRepository extends BaseRepository<Agent> {

    @Query(nativeQuery = true, value = "select id from agent where is_deleted = 'n' and parent_id = ?1")
    List<BigInteger> findAgentIdsByParentId(Long parentId);

    @Query(nativeQuery = true, value = "SELECT s.id, s.class_name as className FROM agent s WHERE s.id in (?1)")
    List<Map<String, Object>> findClassNames(List<Long> ids);
}
