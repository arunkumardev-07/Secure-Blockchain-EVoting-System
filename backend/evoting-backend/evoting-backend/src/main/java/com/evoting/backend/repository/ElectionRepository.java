package com.evoting.backend.repository;

import com.evoting.backend.entity.Election;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectionRepository extends JpaRepository<Election, Long> {
}