package com.cmanager.app.application.repository;

import com.cmanager.app.application.domain.Show;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowRepository extends JpaRepository<Show, String> {
}
