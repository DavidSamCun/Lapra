package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Questionnaire;
import com.mycompany.myapp.repository.QuestionnaireRepository;
import com.mycompany.myapp.service.QuestionnaireService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Questionnaire}.
 */
@Service
@Transactional
public class QuestionnaireServiceImpl implements QuestionnaireService {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireServiceImpl.class);

    private final QuestionnaireRepository questionnaireRepository;

    public QuestionnaireServiceImpl(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    @Override
    public Questionnaire save(Questionnaire questionnaire) {
        log.debug("Request to save Questionnaire : {}", questionnaire);
        return questionnaireRepository.save(questionnaire);
    }

    @Override
    public Questionnaire update(Questionnaire questionnaire) {
        log.debug("Request to save Questionnaire : {}", questionnaire);
        return questionnaireRepository.save(questionnaire);
    }

    @Override
    public Optional<Questionnaire> partialUpdate(Questionnaire questionnaire) {
        log.debug("Request to partially update Questionnaire : {}", questionnaire);

        return questionnaireRepository
            .findById(questionnaire.getId())
            .map(existingQuestionnaire -> {
                if (questionnaire.getAnswer1UsedAt() != null) {
                    existingQuestionnaire.setAnswer1UsedAt(questionnaire.getAnswer1UsedAt());
                }
                if (questionnaire.getAnswer2Type() != null) {
                    existingQuestionnaire.setAnswer2Type(questionnaire.getAnswer2Type());
                }
                if (questionnaire.getAnswer3Size() != null) {
                    existingQuestionnaire.setAnswer3Size(questionnaire.getAnswer3Size());
                }
                if (questionnaire.getAnswer4Function() != null) {
                    existingQuestionnaire.setAnswer4Function(questionnaire.getAnswer4Function());
                }
                if (questionnaire.getAnswer5Price() != null) {
                    existingQuestionnaire.setAnswer5Price(questionnaire.getAnswer5Price());
                }

                return existingQuestionnaire;
            })
            .map(questionnaireRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Questionnaire> findAll(Pageable pageable) {
        log.debug("Request to get all Questionnaires");
        return questionnaireRepository.findAll(pageable);
    }

    public Page<Questionnaire> findAllWithEagerRelationships(Pageable pageable) {
        return questionnaireRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Questionnaire> findOne(Long id) {
        log.debug("Request to get Questionnaire : {}", id);
        return questionnaireRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Questionnaire : {}", id);
        questionnaireRepository.deleteById(id);
    }
}
