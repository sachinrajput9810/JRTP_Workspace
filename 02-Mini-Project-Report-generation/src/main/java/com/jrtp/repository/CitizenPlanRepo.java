package com.jrtp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jrtp.entity.CitizenPlan;

public interface CitizenPlanRepo extends JpaRepository<CitizenPlan, Integer> {

    @Query("SELECT DISTINCT c.planName FROM CitizenPlan c")
    List<String> findDistinctPlanNames();

    @Query("SELECT DISTINCT c.planStatus FROM CitizenPlan c")
    List<String> findDistinctPlanStatuses();
}
