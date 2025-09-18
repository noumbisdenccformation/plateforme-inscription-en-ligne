package com.inscription.repository;

import com.inscription.entity.RegistrationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationFormRepository extends JpaRepository<RegistrationForm, Long> {
    
    Optional<RegistrationForm> findByUserId(Long userId);
    
    List<RegistrationForm> findByStatus(RegistrationForm.FormStatus status);
    
    List<RegistrationForm> findByAssignedAgentId(Long agentId);
    
    @Query("SELECT rf FROM RegistrationForm rf WHERE rf.status = 'UNDER_REVIEW' AND rf.submittedAt < :deadline")
    List<RegistrationForm> findOverdueReviews(@Param("deadline") LocalDateTime deadline);
    
    @Query("SELECT rf FROM RegistrationForm rf WHERE rf.status = 'SUBMITTED' ORDER BY rf.completionPercentage DESC, rf.submittedAt ASC")
    List<RegistrationForm> findSubmittedOrderedByPriority();
    
    @Query("SELECT COUNT(rf) FROM RegistrationForm rf WHERE rf.status = :status")
    Long countByStatus(@Param("status") RegistrationForm.FormStatus status);
    
    @Query("SELECT AVG(rf.completionPercentage) FROM RegistrationForm rf WHERE rf.currentStep = :step")
    Double getAverageCompletionByStep(@Param("step") Integer step);
    
    @Query("SELECT rf FROM RegistrationForm rf WHERE rf.firstName LIKE %:name% OR rf.lastName LIKE %:name%")
    List<RegistrationForm> findByNameContaining(@Param("name") String name);
}