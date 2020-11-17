package com.icebartech.phoneparts.agent.repository;

import com.icebartech.core.repository.BaseRepository;
import com.icebartech.phoneparts.agent.po.Agent;
import org.springframework.data.jpa.repository.Query;

public interface AgentRepository extends BaseRepository<Agent> {

    @Query(nativeQuery = true, value = "select class_name from agent where id = ?1")
    String className(Long agentId);

    @Query(nativeQuery = true, value = "select company_name from agent where id = ?1")
    String companyName(Long agentId);
}
