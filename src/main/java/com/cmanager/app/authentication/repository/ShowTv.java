package com.cmanager.app.authentication.repository;

import com.cmanager.app.authentication.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShowTvRepository extends JpaRepository<ShowTv, Long> {

    Page<ShowTv> findAll(Pageable pageable);

}
