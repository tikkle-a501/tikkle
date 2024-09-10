package com.taesan.tikkle.domain.organization.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.organization.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
}
