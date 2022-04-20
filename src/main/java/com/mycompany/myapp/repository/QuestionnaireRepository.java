package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Questionnaire;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Questionnaire entity.
 */
@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long>, JpaSpecificationExecutor<Questionnaire> {
    @Query("select questionnaire from Questionnaire questionnaire where questionnaire.assignedTo.login = ?#{principal.username}")
    List<Questionnaire> findByAssignedToIsCurrentUser();

    default Optional<Questionnaire> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Questionnaire> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Questionnaire> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct questionnaire from Questionnaire questionnaire left join fetch questionnaire.model left join fetch questionnaire.assignedTo",
        countQuery = "select count(distinct questionnaire) from Questionnaire questionnaire"
    )
    Page<Questionnaire> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct questionnaire from Questionnaire questionnaire left join fetch questionnaire.model left join fetch questionnaire.assignedTo"
    )
    List<Questionnaire> findAllWithToOneRelationships();

    @Query(
        "select questionnaire from Questionnaire questionnaire left join fetch questionnaire.model left join fetch questionnaire.assignedTo where questionnaire.id =:id"
    )
    Optional<Questionnaire> findOneWithToOneRelationships(@Param("id") Long id);
}
