package com.icebartech.core.repository;

import com.icebartech.core.po.BasePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends BasePo> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
}