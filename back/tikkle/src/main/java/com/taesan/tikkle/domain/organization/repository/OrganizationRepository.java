package com.taesan.tikkle.domain.organization.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.taesan.tikkle.domain.organization.entity.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

	@Query(value = "SELECT * FROM organizations WHERE name = :name", nativeQuery = true)
	Optional<Organization> findByName(@Param("name") String name);
}
