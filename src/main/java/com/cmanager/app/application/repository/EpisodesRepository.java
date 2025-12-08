package com.cmanager.app.application.repository;

import com.cmanager.app.application.domain.Episodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EpisodesRepository extends JpaRepository<Episodes, Long>, JpaSpecificationExecutor<Episodes> {

}
