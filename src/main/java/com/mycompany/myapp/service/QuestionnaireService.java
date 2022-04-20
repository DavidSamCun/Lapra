package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Questionnaire;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Questionnaire}.
 */
public interface QuestionnaireService {
    /**
     * Save a questionnaire.
     *
     * @param questionnaire the entity to save.
     * @return the persisted entity.
     */
    Questionnaire save(Questionnaire questionnaire);

    /**
     * Updates a questionnaire.
     *
     * @param questionnaire the entity to update.
     * @return the persisted entity.
     */
    Questionnaire update(Questionnaire questionnaire);

    /**
     * Partially updates a questionnaire.
     *
     * @param questionnaire the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Questionnaire> partialUpdate(Questionnaire questionnaire);

    /**
     * Get all the questionnaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Questionnaire> findAll(Pageable pageable);

    /**
     * Get all the questionnaires with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Questionnaire> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" questionnaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Questionnaire> findOne(Long id);

    /**
     * Delete the "id" questionnaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
