package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Laptop;
import com.mycompany.myapp.domain.Questionnaire;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.QuestionnaireRepository;
import com.mycompany.myapp.service.QuestionnaireService;
import com.mycompany.myapp.service.criteria.QuestionnaireCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link QuestionnaireResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class QuestionnaireResourceIT {

    private static final String DEFAULT_ANSWER_1_USED_AT = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER_1_USED_AT = "BBBBBBBBBB";

    private static final String DEFAULT_ANSWER_2_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER_2_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANSWER_3_SIZE = 1;
    private static final Integer UPDATED_ANSWER_3_SIZE = 2;
    private static final Integer SMALLER_ANSWER_3_SIZE = 1 - 1;

    private static final String DEFAULT_ANSWER_4_FUNCTION = "AAAAAAAAAA";
    private static final String UPDATED_ANSWER_4_FUNCTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANSWER_5_PRICE = 1;
    private static final Integer UPDATED_ANSWER_5_PRICE = 2;
    private static final Integer SMALLER_ANSWER_5_PRICE = 1 - 1;

    private static final String ENTITY_API_URL = "/api/questionnaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Mock
    private QuestionnaireRepository questionnaireRepositoryMock;

    @Mock
    private QuestionnaireService questionnaireServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionnaireMockMvc;

    private Questionnaire questionnaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionnaire createEntity(EntityManager em) {
        Questionnaire questionnaire = new Questionnaire()
            .answer1UsedAt(DEFAULT_ANSWER_1_USED_AT)
            .answer2Type(DEFAULT_ANSWER_2_TYPE)
            .answer3Size(DEFAULT_ANSWER_3_SIZE)
            .answer4Function(DEFAULT_ANSWER_4_FUNCTION)
            .answer5Price(DEFAULT_ANSWER_5_PRICE);
        return questionnaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionnaire createUpdatedEntity(EntityManager em) {
        Questionnaire questionnaire = new Questionnaire()
            .answer1UsedAt(UPDATED_ANSWER_1_USED_AT)
            .answer2Type(UPDATED_ANSWER_2_TYPE)
            .answer3Size(UPDATED_ANSWER_3_SIZE)
            .answer4Function(UPDATED_ANSWER_4_FUNCTION)
            .answer5Price(UPDATED_ANSWER_5_PRICE);
        return questionnaire;
    }

    @BeforeEach
    public void initTest() {
        questionnaire = createEntity(em);
    }

    @Test
    @Transactional
    void createQuestionnaire() throws Exception {
        int databaseSizeBeforeCreate = questionnaireRepository.findAll().size();
        // Create the Questionnaire
        restQuestionnaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionnaire)))
            .andExpect(status().isCreated());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeCreate + 1);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.getAnswer1UsedAt()).isEqualTo(DEFAULT_ANSWER_1_USED_AT);
        assertThat(testQuestionnaire.getAnswer2Type()).isEqualTo(DEFAULT_ANSWER_2_TYPE);
        assertThat(testQuestionnaire.getAnswer3Size()).isEqualTo(DEFAULT_ANSWER_3_SIZE);
        assertThat(testQuestionnaire.getAnswer4Function()).isEqualTo(DEFAULT_ANSWER_4_FUNCTION);
        assertThat(testQuestionnaire.getAnswer5Price()).isEqualTo(DEFAULT_ANSWER_5_PRICE);
    }

    @Test
    @Transactional
    void createQuestionnaireWithExistingId() throws Exception {
        // Create the Questionnaire with an existing ID
        questionnaire.setId(1L);

        int databaseSizeBeforeCreate = questionnaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionnaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionnaire)))
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQuestionnaires() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList
        restQuestionnaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer1UsedAt").value(hasItem(DEFAULT_ANSWER_1_USED_AT)))
            .andExpect(jsonPath("$.[*].answer2Type").value(hasItem(DEFAULT_ANSWER_2_TYPE)))
            .andExpect(jsonPath("$.[*].answer3Size").value(hasItem(DEFAULT_ANSWER_3_SIZE)))
            .andExpect(jsonPath("$.[*].answer4Function").value(hasItem(DEFAULT_ANSWER_4_FUNCTION)))
            .andExpect(jsonPath("$.[*].answer5Price").value(hasItem(DEFAULT_ANSWER_5_PRICE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionnairesWithEagerRelationshipsIsEnabled() throws Exception {
        when(questionnaireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionnaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionnaireServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllQuestionnairesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(questionnaireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionnaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(questionnaireServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get the questionnaire
        restQuestionnaireMockMvc
            .perform(get(ENTITY_API_URL_ID, questionnaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionnaire.getId().intValue()))
            .andExpect(jsonPath("$.answer1UsedAt").value(DEFAULT_ANSWER_1_USED_AT))
            .andExpect(jsonPath("$.answer2Type").value(DEFAULT_ANSWER_2_TYPE))
            .andExpect(jsonPath("$.answer3Size").value(DEFAULT_ANSWER_3_SIZE))
            .andExpect(jsonPath("$.answer4Function").value(DEFAULT_ANSWER_4_FUNCTION))
            .andExpect(jsonPath("$.answer5Price").value(DEFAULT_ANSWER_5_PRICE));
    }

    @Test
    @Transactional
    void getQuestionnairesByIdFiltering() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        Long id = questionnaire.getId();

        defaultQuestionnaireShouldBeFound("id.equals=" + id);
        defaultQuestionnaireShouldNotBeFound("id.notEquals=" + id);

        defaultQuestionnaireShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestionnaireShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestionnaireShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestionnaireShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer1UsedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer1UsedAt equals to DEFAULT_ANSWER_1_USED_AT
        defaultQuestionnaireShouldBeFound("answer1UsedAt.equals=" + DEFAULT_ANSWER_1_USED_AT);

        // Get all the questionnaireList where answer1UsedAt equals to UPDATED_ANSWER_1_USED_AT
        defaultQuestionnaireShouldNotBeFound("answer1UsedAt.equals=" + UPDATED_ANSWER_1_USED_AT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer1UsedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer1UsedAt not equals to DEFAULT_ANSWER_1_USED_AT
        defaultQuestionnaireShouldNotBeFound("answer1UsedAt.notEquals=" + DEFAULT_ANSWER_1_USED_AT);

        // Get all the questionnaireList where answer1UsedAt not equals to UPDATED_ANSWER_1_USED_AT
        defaultQuestionnaireShouldBeFound("answer1UsedAt.notEquals=" + UPDATED_ANSWER_1_USED_AT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer1UsedAtIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer1UsedAt in DEFAULT_ANSWER_1_USED_AT or UPDATED_ANSWER_1_USED_AT
        defaultQuestionnaireShouldBeFound("answer1UsedAt.in=" + DEFAULT_ANSWER_1_USED_AT + "," + UPDATED_ANSWER_1_USED_AT);

        // Get all the questionnaireList where answer1UsedAt equals to UPDATED_ANSWER_1_USED_AT
        defaultQuestionnaireShouldNotBeFound("answer1UsedAt.in=" + UPDATED_ANSWER_1_USED_AT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer1UsedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer1UsedAt is not null
        defaultQuestionnaireShouldBeFound("answer1UsedAt.specified=true");

        // Get all the questionnaireList where answer1UsedAt is null
        defaultQuestionnaireShouldNotBeFound("answer1UsedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer1UsedAtContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer1UsedAt contains DEFAULT_ANSWER_1_USED_AT
        defaultQuestionnaireShouldBeFound("answer1UsedAt.contains=" + DEFAULT_ANSWER_1_USED_AT);

        // Get all the questionnaireList where answer1UsedAt contains UPDATED_ANSWER_1_USED_AT
        defaultQuestionnaireShouldNotBeFound("answer1UsedAt.contains=" + UPDATED_ANSWER_1_USED_AT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer1UsedAtNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer1UsedAt does not contain DEFAULT_ANSWER_1_USED_AT
        defaultQuestionnaireShouldNotBeFound("answer1UsedAt.doesNotContain=" + DEFAULT_ANSWER_1_USED_AT);

        // Get all the questionnaireList where answer1UsedAt does not contain UPDATED_ANSWER_1_USED_AT
        defaultQuestionnaireShouldBeFound("answer1UsedAt.doesNotContain=" + UPDATED_ANSWER_1_USED_AT);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer2TypeIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer2Type equals to DEFAULT_ANSWER_2_TYPE
        defaultQuestionnaireShouldBeFound("answer2Type.equals=" + DEFAULT_ANSWER_2_TYPE);

        // Get all the questionnaireList where answer2Type equals to UPDATED_ANSWER_2_TYPE
        defaultQuestionnaireShouldNotBeFound("answer2Type.equals=" + UPDATED_ANSWER_2_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer2TypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer2Type not equals to DEFAULT_ANSWER_2_TYPE
        defaultQuestionnaireShouldNotBeFound("answer2Type.notEquals=" + DEFAULT_ANSWER_2_TYPE);

        // Get all the questionnaireList where answer2Type not equals to UPDATED_ANSWER_2_TYPE
        defaultQuestionnaireShouldBeFound("answer2Type.notEquals=" + UPDATED_ANSWER_2_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer2TypeIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer2Type in DEFAULT_ANSWER_2_TYPE or UPDATED_ANSWER_2_TYPE
        defaultQuestionnaireShouldBeFound("answer2Type.in=" + DEFAULT_ANSWER_2_TYPE + "," + UPDATED_ANSWER_2_TYPE);

        // Get all the questionnaireList where answer2Type equals to UPDATED_ANSWER_2_TYPE
        defaultQuestionnaireShouldNotBeFound("answer2Type.in=" + UPDATED_ANSWER_2_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer2TypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer2Type is not null
        defaultQuestionnaireShouldBeFound("answer2Type.specified=true");

        // Get all the questionnaireList where answer2Type is null
        defaultQuestionnaireShouldNotBeFound("answer2Type.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer2TypeContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer2Type contains DEFAULT_ANSWER_2_TYPE
        defaultQuestionnaireShouldBeFound("answer2Type.contains=" + DEFAULT_ANSWER_2_TYPE);

        // Get all the questionnaireList where answer2Type contains UPDATED_ANSWER_2_TYPE
        defaultQuestionnaireShouldNotBeFound("answer2Type.contains=" + UPDATED_ANSWER_2_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer2TypeNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer2Type does not contain DEFAULT_ANSWER_2_TYPE
        defaultQuestionnaireShouldNotBeFound("answer2Type.doesNotContain=" + DEFAULT_ANSWER_2_TYPE);

        // Get all the questionnaireList where answer2Type does not contain UPDATED_ANSWER_2_TYPE
        defaultQuestionnaireShouldBeFound("answer2Type.doesNotContain=" + UPDATED_ANSWER_2_TYPE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer3SizeIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer3Size equals to DEFAULT_ANSWER_3_SIZE
        defaultQuestionnaireShouldBeFound("answer3Size.equals=" + DEFAULT_ANSWER_3_SIZE);

        // Get all the questionnaireList where answer3Size equals to UPDATED_ANSWER_3_SIZE
        defaultQuestionnaireShouldNotBeFound("answer3Size.equals=" + UPDATED_ANSWER_3_SIZE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer3SizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer3Size not equals to DEFAULT_ANSWER_3_SIZE
        defaultQuestionnaireShouldNotBeFound("answer3Size.notEquals=" + DEFAULT_ANSWER_3_SIZE);

        // Get all the questionnaireList where answer3Size not equals to UPDATED_ANSWER_3_SIZE
        defaultQuestionnaireShouldBeFound("answer3Size.notEquals=" + UPDATED_ANSWER_3_SIZE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer3SizeIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer3Size in DEFAULT_ANSWER_3_SIZE or UPDATED_ANSWER_3_SIZE
        defaultQuestionnaireShouldBeFound("answer3Size.in=" + DEFAULT_ANSWER_3_SIZE + "," + UPDATED_ANSWER_3_SIZE);

        // Get all the questionnaireList where answer3Size equals to UPDATED_ANSWER_3_SIZE
        defaultQuestionnaireShouldNotBeFound("answer3Size.in=" + UPDATED_ANSWER_3_SIZE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer3SizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer3Size is not null
        defaultQuestionnaireShouldBeFound("answer3Size.specified=true");

        // Get all the questionnaireList where answer3Size is null
        defaultQuestionnaireShouldNotBeFound("answer3Size.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer3SizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer3Size is greater than or equal to DEFAULT_ANSWER_3_SIZE
        defaultQuestionnaireShouldBeFound("answer3Size.greaterThanOrEqual=" + DEFAULT_ANSWER_3_SIZE);

        // Get all the questionnaireList where answer3Size is greater than or equal to UPDATED_ANSWER_3_SIZE
        defaultQuestionnaireShouldNotBeFound("answer3Size.greaterThanOrEqual=" + UPDATED_ANSWER_3_SIZE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer3SizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer3Size is less than or equal to DEFAULT_ANSWER_3_SIZE
        defaultQuestionnaireShouldBeFound("answer3Size.lessThanOrEqual=" + DEFAULT_ANSWER_3_SIZE);

        // Get all the questionnaireList where answer3Size is less than or equal to SMALLER_ANSWER_3_SIZE
        defaultQuestionnaireShouldNotBeFound("answer3Size.lessThanOrEqual=" + SMALLER_ANSWER_3_SIZE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer3SizeIsLessThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer3Size is less than DEFAULT_ANSWER_3_SIZE
        defaultQuestionnaireShouldNotBeFound("answer3Size.lessThan=" + DEFAULT_ANSWER_3_SIZE);

        // Get all the questionnaireList where answer3Size is less than UPDATED_ANSWER_3_SIZE
        defaultQuestionnaireShouldBeFound("answer3Size.lessThan=" + UPDATED_ANSWER_3_SIZE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer3SizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer3Size is greater than DEFAULT_ANSWER_3_SIZE
        defaultQuestionnaireShouldNotBeFound("answer3Size.greaterThan=" + DEFAULT_ANSWER_3_SIZE);

        // Get all the questionnaireList where answer3Size is greater than SMALLER_ANSWER_3_SIZE
        defaultQuestionnaireShouldBeFound("answer3Size.greaterThan=" + SMALLER_ANSWER_3_SIZE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer4FunctionIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer4Function equals to DEFAULT_ANSWER_4_FUNCTION
        defaultQuestionnaireShouldBeFound("answer4Function.equals=" + DEFAULT_ANSWER_4_FUNCTION);

        // Get all the questionnaireList where answer4Function equals to UPDATED_ANSWER_4_FUNCTION
        defaultQuestionnaireShouldNotBeFound("answer4Function.equals=" + UPDATED_ANSWER_4_FUNCTION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer4FunctionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer4Function not equals to DEFAULT_ANSWER_4_FUNCTION
        defaultQuestionnaireShouldNotBeFound("answer4Function.notEquals=" + DEFAULT_ANSWER_4_FUNCTION);

        // Get all the questionnaireList where answer4Function not equals to UPDATED_ANSWER_4_FUNCTION
        defaultQuestionnaireShouldBeFound("answer4Function.notEquals=" + UPDATED_ANSWER_4_FUNCTION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer4FunctionIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer4Function in DEFAULT_ANSWER_4_FUNCTION or UPDATED_ANSWER_4_FUNCTION
        defaultQuestionnaireShouldBeFound("answer4Function.in=" + DEFAULT_ANSWER_4_FUNCTION + "," + UPDATED_ANSWER_4_FUNCTION);

        // Get all the questionnaireList where answer4Function equals to UPDATED_ANSWER_4_FUNCTION
        defaultQuestionnaireShouldNotBeFound("answer4Function.in=" + UPDATED_ANSWER_4_FUNCTION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer4FunctionIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer4Function is not null
        defaultQuestionnaireShouldBeFound("answer4Function.specified=true");

        // Get all the questionnaireList where answer4Function is null
        defaultQuestionnaireShouldNotBeFound("answer4Function.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer4FunctionContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer4Function contains DEFAULT_ANSWER_4_FUNCTION
        defaultQuestionnaireShouldBeFound("answer4Function.contains=" + DEFAULT_ANSWER_4_FUNCTION);

        // Get all the questionnaireList where answer4Function contains UPDATED_ANSWER_4_FUNCTION
        defaultQuestionnaireShouldNotBeFound("answer4Function.contains=" + UPDATED_ANSWER_4_FUNCTION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer4FunctionNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer4Function does not contain DEFAULT_ANSWER_4_FUNCTION
        defaultQuestionnaireShouldNotBeFound("answer4Function.doesNotContain=" + DEFAULT_ANSWER_4_FUNCTION);

        // Get all the questionnaireList where answer4Function does not contain UPDATED_ANSWER_4_FUNCTION
        defaultQuestionnaireShouldBeFound("answer4Function.doesNotContain=" + UPDATED_ANSWER_4_FUNCTION);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer5PriceIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer5Price equals to DEFAULT_ANSWER_5_PRICE
        defaultQuestionnaireShouldBeFound("answer5Price.equals=" + DEFAULT_ANSWER_5_PRICE);

        // Get all the questionnaireList where answer5Price equals to UPDATED_ANSWER_5_PRICE
        defaultQuestionnaireShouldNotBeFound("answer5Price.equals=" + UPDATED_ANSWER_5_PRICE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer5PriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer5Price not equals to DEFAULT_ANSWER_5_PRICE
        defaultQuestionnaireShouldNotBeFound("answer5Price.notEquals=" + DEFAULT_ANSWER_5_PRICE);

        // Get all the questionnaireList where answer5Price not equals to UPDATED_ANSWER_5_PRICE
        defaultQuestionnaireShouldBeFound("answer5Price.notEquals=" + UPDATED_ANSWER_5_PRICE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer5PriceIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer5Price in DEFAULT_ANSWER_5_PRICE or UPDATED_ANSWER_5_PRICE
        defaultQuestionnaireShouldBeFound("answer5Price.in=" + DEFAULT_ANSWER_5_PRICE + "," + UPDATED_ANSWER_5_PRICE);

        // Get all the questionnaireList where answer5Price equals to UPDATED_ANSWER_5_PRICE
        defaultQuestionnaireShouldNotBeFound("answer5Price.in=" + UPDATED_ANSWER_5_PRICE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer5PriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer5Price is not null
        defaultQuestionnaireShouldBeFound("answer5Price.specified=true");

        // Get all the questionnaireList where answer5Price is null
        defaultQuestionnaireShouldNotBeFound("answer5Price.specified=false");
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer5PriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer5Price is greater than or equal to DEFAULT_ANSWER_5_PRICE
        defaultQuestionnaireShouldBeFound("answer5Price.greaterThanOrEqual=" + DEFAULT_ANSWER_5_PRICE);

        // Get all the questionnaireList where answer5Price is greater than or equal to UPDATED_ANSWER_5_PRICE
        defaultQuestionnaireShouldNotBeFound("answer5Price.greaterThanOrEqual=" + UPDATED_ANSWER_5_PRICE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer5PriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer5Price is less than or equal to DEFAULT_ANSWER_5_PRICE
        defaultQuestionnaireShouldBeFound("answer5Price.lessThanOrEqual=" + DEFAULT_ANSWER_5_PRICE);

        // Get all the questionnaireList where answer5Price is less than or equal to SMALLER_ANSWER_5_PRICE
        defaultQuestionnaireShouldNotBeFound("answer5Price.lessThanOrEqual=" + SMALLER_ANSWER_5_PRICE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer5PriceIsLessThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer5Price is less than DEFAULT_ANSWER_5_PRICE
        defaultQuestionnaireShouldNotBeFound("answer5Price.lessThan=" + DEFAULT_ANSWER_5_PRICE);

        // Get all the questionnaireList where answer5Price is less than UPDATED_ANSWER_5_PRICE
        defaultQuestionnaireShouldBeFound("answer5Price.lessThan=" + UPDATED_ANSWER_5_PRICE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAnswer5PriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where answer5Price is greater than DEFAULT_ANSWER_5_PRICE
        defaultQuestionnaireShouldNotBeFound("answer5Price.greaterThan=" + DEFAULT_ANSWER_5_PRICE);

        // Get all the questionnaireList where answer5Price is greater than SMALLER_ANSWER_5_PRICE
        defaultQuestionnaireShouldBeFound("answer5Price.greaterThan=" + SMALLER_ANSWER_5_PRICE);
    }

    @Test
    @Transactional
    void getAllQuestionnairesByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);
        Laptop model;
        if (TestUtil.findAll(em, Laptop.class).isEmpty()) {
            model = LaptopResourceIT.createEntity(em);
            em.persist(model);
            em.flush();
        } else {
            model = TestUtil.findAll(em, Laptop.class).get(0);
        }
        em.persist(model);
        em.flush();
        questionnaire.setModel(model);
        questionnaireRepository.saveAndFlush(questionnaire);
        Long modelId = model.getId();

        // Get all the questionnaireList where model equals to modelId
        defaultQuestionnaireShouldBeFound("modelId.equals=" + modelId);

        // Get all the questionnaireList where model equals to (modelId + 1)
        defaultQuestionnaireShouldNotBeFound("modelId.equals=" + (modelId + 1));
    }

    @Test
    @Transactional
    void getAllQuestionnairesByAssignedToIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);
        User assignedTo;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            assignedTo = UserResourceIT.createEntity(em);
            em.persist(assignedTo);
            em.flush();
        } else {
            assignedTo = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(assignedTo);
        em.flush();
        questionnaire.setAssignedTo(assignedTo);
        questionnaireRepository.saveAndFlush(questionnaire);
        Long assignedToId = assignedTo.getId();

        // Get all the questionnaireList where assignedTo equals to assignedToId
        defaultQuestionnaireShouldBeFound("assignedToId.equals=" + assignedToId);

        // Get all the questionnaireList where assignedTo equals to (assignedToId + 1)
        defaultQuestionnaireShouldNotBeFound("assignedToId.equals=" + (assignedToId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionnaireShouldBeFound(String filter) throws Exception {
        restQuestionnaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer1UsedAt").value(hasItem(DEFAULT_ANSWER_1_USED_AT)))
            .andExpect(jsonPath("$.[*].answer2Type").value(hasItem(DEFAULT_ANSWER_2_TYPE)))
            .andExpect(jsonPath("$.[*].answer3Size").value(hasItem(DEFAULT_ANSWER_3_SIZE)))
            .andExpect(jsonPath("$.[*].answer4Function").value(hasItem(DEFAULT_ANSWER_4_FUNCTION)))
            .andExpect(jsonPath("$.[*].answer5Price").value(hasItem(DEFAULT_ANSWER_5_PRICE)));

        // Check, that the count call also returns 1
        restQuestionnaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionnaireShouldNotBeFound(String filter) throws Exception {
        restQuestionnaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionnaireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuestionnaire() throws Exception {
        // Get the questionnaire
        restQuestionnaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Update the questionnaire
        Questionnaire updatedQuestionnaire = questionnaireRepository.findById(questionnaire.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionnaire are not directly saved in db
        em.detach(updatedQuestionnaire);
        updatedQuestionnaire
            .answer1UsedAt(UPDATED_ANSWER_1_USED_AT)
            .answer2Type(UPDATED_ANSWER_2_TYPE)
            .answer3Size(UPDATED_ANSWER_3_SIZE)
            .answer4Function(UPDATED_ANSWER_4_FUNCTION)
            .answer5Price(UPDATED_ANSWER_5_PRICE);

        restQuestionnaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQuestionnaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedQuestionnaire))
            )
            .andExpect(status().isOk());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.getAnswer1UsedAt()).isEqualTo(UPDATED_ANSWER_1_USED_AT);
        assertThat(testQuestionnaire.getAnswer2Type()).isEqualTo(UPDATED_ANSWER_2_TYPE);
        assertThat(testQuestionnaire.getAnswer3Size()).isEqualTo(UPDATED_ANSWER_3_SIZE);
        assertThat(testQuestionnaire.getAnswer4Function()).isEqualTo(UPDATED_ANSWER_4_FUNCTION);
        assertThat(testQuestionnaire.getAnswer5Price()).isEqualTo(UPDATED_ANSWER_5_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();
        questionnaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, questionnaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();
        questionnaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(questionnaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();
        questionnaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(questionnaire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuestionnaireWithPatch() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Update the questionnaire using partial update
        Questionnaire partialUpdatedQuestionnaire = new Questionnaire();
        partialUpdatedQuestionnaire.setId(questionnaire.getId());

        partialUpdatedQuestionnaire.answer2Type(UPDATED_ANSWER_2_TYPE).answer4Function(UPDATED_ANSWER_4_FUNCTION);

        restQuestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionnaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionnaire))
            )
            .andExpect(status().isOk());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.getAnswer1UsedAt()).isEqualTo(DEFAULT_ANSWER_1_USED_AT);
        assertThat(testQuestionnaire.getAnswer2Type()).isEqualTo(UPDATED_ANSWER_2_TYPE);
        assertThat(testQuestionnaire.getAnswer3Size()).isEqualTo(DEFAULT_ANSWER_3_SIZE);
        assertThat(testQuestionnaire.getAnswer4Function()).isEqualTo(UPDATED_ANSWER_4_FUNCTION);
        assertThat(testQuestionnaire.getAnswer5Price()).isEqualTo(DEFAULT_ANSWER_5_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateQuestionnaireWithPatch() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Update the questionnaire using partial update
        Questionnaire partialUpdatedQuestionnaire = new Questionnaire();
        partialUpdatedQuestionnaire.setId(questionnaire.getId());

        partialUpdatedQuestionnaire
            .answer1UsedAt(UPDATED_ANSWER_1_USED_AT)
            .answer2Type(UPDATED_ANSWER_2_TYPE)
            .answer3Size(UPDATED_ANSWER_3_SIZE)
            .answer4Function(UPDATED_ANSWER_4_FUNCTION)
            .answer5Price(UPDATED_ANSWER_5_PRICE);

        restQuestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuestionnaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuestionnaire))
            )
            .andExpect(status().isOk());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.getAnswer1UsedAt()).isEqualTo(UPDATED_ANSWER_1_USED_AT);
        assertThat(testQuestionnaire.getAnswer2Type()).isEqualTo(UPDATED_ANSWER_2_TYPE);
        assertThat(testQuestionnaire.getAnswer3Size()).isEqualTo(UPDATED_ANSWER_3_SIZE);
        assertThat(testQuestionnaire.getAnswer4Function()).isEqualTo(UPDATED_ANSWER_4_FUNCTION);
        assertThat(testQuestionnaire.getAnswer5Price()).isEqualTo(UPDATED_ANSWER_5_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();
        questionnaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, questionnaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionnaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();
        questionnaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(questionnaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();
        questionnaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(questionnaire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        int databaseSizeBeforeDelete = questionnaireRepository.findAll().size();

        // Delete the questionnaire
        restQuestionnaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, questionnaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
