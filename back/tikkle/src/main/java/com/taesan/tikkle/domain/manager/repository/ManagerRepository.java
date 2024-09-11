package com.taesan.tikkle.domain.manager.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.manager.entity.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID> {
}
