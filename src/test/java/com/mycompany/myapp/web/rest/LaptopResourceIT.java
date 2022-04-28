package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Laptop;
import com.mycompany.myapp.repository.LaptopRepository;
import com.mycompany.myapp.service.criteria.LaptopCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LaptopResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LaptopResourceIT {

    private static final String DEFAULT_BRAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_SCREEN_SIZE = 1F;
    private static final Float UPDATED_SCREEN_SIZE = 2F;
    private static final Float SMALLER_SCREEN_SIZE = 1F - 1F;

    private static final Integer DEFAULT_MEMORY = 1;
    private static final Integer UPDATED_MEMORY = 2;
    private static final Integer SMALLER_MEMORY = 1 - 1;

    private static final Float DEFAULT_STORAGE = 1F;
    private static final Float UPDATED_STORAGE = 2F;
    private static final Float SMALLER_STORAGE = 1F - 1F;

    private static final Integer DEFAULT_SCREEN_BRIGHTNESS = 1;
    private static final Integer UPDATED_SCREEN_BRIGHTNESS = 2;
    private static final Integer SMALLER_SCREEN_BRIGHTNESS = 1 - 1;

    private static final Integer DEFAULT_SCREEN_REFRESH_HZ = 1;
    private static final Integer UPDATED_SCREEN_REFRESH_HZ = 2;
    private static final Integer SMALLER_SCREEN_REFRESH_HZ = 1 - 1;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/laptops";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LaptopRepository laptopRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLaptopMockMvc;

    private Laptop laptop;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Laptop createEntity(EntityManager em) {
        Laptop laptop = new Laptop()
            .brandName(DEFAULT_BRAND_NAME)
            .modelName(DEFAULT_MODEL_NAME)
            .screenSize(DEFAULT_SCREEN_SIZE)
            .memory(DEFAULT_MEMORY)
            .storage(DEFAULT_STORAGE)
            .screenBrightness(DEFAULT_SCREEN_BRIGHTNESS)
            .screenRefreshHz(DEFAULT_SCREEN_REFRESH_HZ)
            .price(DEFAULT_PRICE);
        return laptop;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Laptop createUpdatedEntity(EntityManager em) {
        Laptop laptop = new Laptop()
            .brandName(UPDATED_BRAND_NAME)
            .modelName(UPDATED_MODEL_NAME)
            .screenSize(UPDATED_SCREEN_SIZE)
            .memory(UPDATED_MEMORY)
            .storage(UPDATED_STORAGE)
            .screenBrightness(UPDATED_SCREEN_BRIGHTNESS)
            .screenRefreshHz(UPDATED_SCREEN_REFRESH_HZ)
            .price(UPDATED_PRICE);
        return laptop;
    }

    @BeforeEach
    public void initTest() {
        laptop = createEntity(em);
    }

    @Test
    @Transactional
    void createLaptop() throws Exception {
        int databaseSizeBeforeCreate = laptopRepository.findAll().size();
        // Create the Laptop
        restLaptopMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(laptop)))
            .andExpect(status().isCreated());

        // Validate the Laptop in the database
        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeCreate + 1);
        Laptop testLaptop = laptopList.get(laptopList.size() - 1);
        assertThat(testLaptop.getBrandName()).isEqualTo(DEFAULT_BRAND_NAME);
        assertThat(testLaptop.getModelName()).isEqualTo(DEFAULT_MODEL_NAME);
        assertThat(testLaptop.getScreenSize()).isEqualTo(DEFAULT_SCREEN_SIZE);
        assertThat(testLaptop.getMemory()).isEqualTo(DEFAULT_MEMORY);
        assertThat(testLaptop.getStorage()).isEqualTo(DEFAULT_STORAGE);
        assertThat(testLaptop.getScreenBrightness()).isEqualTo(DEFAULT_SCREEN_BRIGHTNESS);
        assertThat(testLaptop.getScreenRefreshHz()).isEqualTo(DEFAULT_SCREEN_REFRESH_HZ);
        assertThat(testLaptop.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createLaptopWithExistingId() throws Exception {
        // Create the Laptop with an existing ID
        laptop.setId(1L);

        int databaseSizeBeforeCreate = laptopRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLaptopMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(laptop)))
            .andExpect(status().isBadRequest());

        // Validate the Laptop in the database
        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBrandNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = laptopRepository.findAll().size();
        // set the field null
        laptop.setBrandName(null);

        // Create the Laptop, which fails.

        restLaptopMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(laptop)))
            .andExpect(status().isBadRequest());

        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = laptopRepository.findAll().size();
        // set the field null
        laptop.setModelName(null);

        // Create the Laptop, which fails.

        restLaptopMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(laptop)))
            .andExpect(status().isBadRequest());

        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLaptops() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList
        restLaptopMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(laptop.getId().intValue())))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].modelName").value(hasItem(DEFAULT_MODEL_NAME)))
            .andExpect(jsonPath("$.[*].screenSize").value(hasItem(DEFAULT_SCREEN_SIZE.doubleValue())))
            .andExpect(jsonPath("$.[*].memory").value(hasItem(DEFAULT_MEMORY)))
            .andExpect(jsonPath("$.[*].storage").value(hasItem(DEFAULT_STORAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].screenBrightness").value(hasItem(DEFAULT_SCREEN_BRIGHTNESS)))
            .andExpect(jsonPath("$.[*].screenRefreshHz").value(hasItem(DEFAULT_SCREEN_REFRESH_HZ)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getLaptop() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get the laptop
        restLaptopMockMvc
            .perform(get(ENTITY_API_URL_ID, laptop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(laptop.getId().intValue()))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME))
            .andExpect(jsonPath("$.modelName").value(DEFAULT_MODEL_NAME))
            .andExpect(jsonPath("$.screenSize").value(DEFAULT_SCREEN_SIZE.doubleValue()))
            .andExpect(jsonPath("$.memory").value(DEFAULT_MEMORY))
            .andExpect(jsonPath("$.storage").value(DEFAULT_STORAGE.doubleValue()))
            .andExpect(jsonPath("$.screenBrightness").value(DEFAULT_SCREEN_BRIGHTNESS))
            .andExpect(jsonPath("$.screenRefreshHz").value(DEFAULT_SCREEN_REFRESH_HZ))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getLaptopsByIdFiltering() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        Long id = laptop.getId();

        defaultLaptopShouldBeFound("id.equals=" + id);
        defaultLaptopShouldNotBeFound("id.notEquals=" + id);

        defaultLaptopShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLaptopShouldNotBeFound("id.greaterThan=" + id);

        defaultLaptopShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLaptopShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLaptopsByBrandNameIsEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where brandName equals to DEFAULT_BRAND_NAME
        defaultLaptopShouldBeFound("brandName.equals=" + DEFAULT_BRAND_NAME);

        // Get all the laptopList where brandName equals to UPDATED_BRAND_NAME
        defaultLaptopShouldNotBeFound("brandName.equals=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllLaptopsByBrandNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where brandName not equals to DEFAULT_BRAND_NAME
        defaultLaptopShouldNotBeFound("brandName.notEquals=" + DEFAULT_BRAND_NAME);

        // Get all the laptopList where brandName not equals to UPDATED_BRAND_NAME
        defaultLaptopShouldBeFound("brandName.notEquals=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllLaptopsByBrandNameIsInShouldWork() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where brandName in DEFAULT_BRAND_NAME or UPDATED_BRAND_NAME
        defaultLaptopShouldBeFound("brandName.in=" + DEFAULT_BRAND_NAME + "," + UPDATED_BRAND_NAME);

        // Get all the laptopList where brandName equals to UPDATED_BRAND_NAME
        defaultLaptopShouldNotBeFound("brandName.in=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllLaptopsByBrandNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where brandName is not null
        defaultLaptopShouldBeFound("brandName.specified=true");

        // Get all the laptopList where brandName is null
        defaultLaptopShouldNotBeFound("brandName.specified=false");
    }

    @Test
    @Transactional
    void getAllLaptopsByBrandNameContainsSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where brandName contains DEFAULT_BRAND_NAME
        defaultLaptopShouldBeFound("brandName.contains=" + DEFAULT_BRAND_NAME);

        // Get all the laptopList where brandName contains UPDATED_BRAND_NAME
        defaultLaptopShouldNotBeFound("brandName.contains=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllLaptopsByBrandNameNotContainsSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where brandName does not contain DEFAULT_BRAND_NAME
        defaultLaptopShouldNotBeFound("brandName.doesNotContain=" + DEFAULT_BRAND_NAME);

        // Get all the laptopList where brandName does not contain UPDATED_BRAND_NAME
        defaultLaptopShouldBeFound("brandName.doesNotContain=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllLaptopsByModelNameIsEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where modelName equals to DEFAULT_MODEL_NAME
        defaultLaptopShouldBeFound("modelName.equals=" + DEFAULT_MODEL_NAME);

        // Get all the laptopList where modelName equals to UPDATED_MODEL_NAME
        defaultLaptopShouldNotBeFound("modelName.equals=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllLaptopsByModelNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where modelName not equals to DEFAULT_MODEL_NAME
        defaultLaptopShouldNotBeFound("modelName.notEquals=" + DEFAULT_MODEL_NAME);

        // Get all the laptopList where modelName not equals to UPDATED_MODEL_NAME
        defaultLaptopShouldBeFound("modelName.notEquals=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllLaptopsByModelNameIsInShouldWork() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where modelName in DEFAULT_MODEL_NAME or UPDATED_MODEL_NAME
        defaultLaptopShouldBeFound("modelName.in=" + DEFAULT_MODEL_NAME + "," + UPDATED_MODEL_NAME);

        // Get all the laptopList where modelName equals to UPDATED_MODEL_NAME
        defaultLaptopShouldNotBeFound("modelName.in=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllLaptopsByModelNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where modelName is not null
        defaultLaptopShouldBeFound("modelName.specified=true");

        // Get all the laptopList where modelName is null
        defaultLaptopShouldNotBeFound("modelName.specified=false");
    }

    @Test
    @Transactional
    void getAllLaptopsByModelNameContainsSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where modelName contains DEFAULT_MODEL_NAME
        defaultLaptopShouldBeFound("modelName.contains=" + DEFAULT_MODEL_NAME);

        // Get all the laptopList where modelName contains UPDATED_MODEL_NAME
        defaultLaptopShouldNotBeFound("modelName.contains=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllLaptopsByModelNameNotContainsSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where modelName does not contain DEFAULT_MODEL_NAME
        defaultLaptopShouldNotBeFound("modelName.doesNotContain=" + DEFAULT_MODEL_NAME);

        // Get all the laptopList where modelName does not contain UPDATED_MODEL_NAME
        defaultLaptopShouldBeFound("modelName.doesNotContain=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenSize equals to DEFAULT_SCREEN_SIZE
        defaultLaptopShouldBeFound("screenSize.equals=" + DEFAULT_SCREEN_SIZE);

        // Get all the laptopList where screenSize equals to UPDATED_SCREEN_SIZE
        defaultLaptopShouldNotBeFound("screenSize.equals=" + UPDATED_SCREEN_SIZE);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenSizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenSize not equals to DEFAULT_SCREEN_SIZE
        defaultLaptopShouldNotBeFound("screenSize.notEquals=" + DEFAULT_SCREEN_SIZE);

        // Get all the laptopList where screenSize not equals to UPDATED_SCREEN_SIZE
        defaultLaptopShouldBeFound("screenSize.notEquals=" + UPDATED_SCREEN_SIZE);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenSizeIsInShouldWork() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenSize in DEFAULT_SCREEN_SIZE or UPDATED_SCREEN_SIZE
        defaultLaptopShouldBeFound("screenSize.in=" + DEFAULT_SCREEN_SIZE + "," + UPDATED_SCREEN_SIZE);

        // Get all the laptopList where screenSize equals to UPDATED_SCREEN_SIZE
        defaultLaptopShouldNotBeFound("screenSize.in=" + UPDATED_SCREEN_SIZE);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenSize is not null
        defaultLaptopShouldBeFound("screenSize.specified=true");

        // Get all the laptopList where screenSize is null
        defaultLaptopShouldNotBeFound("screenSize.specified=false");
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenSize is greater than or equal to DEFAULT_SCREEN_SIZE
        defaultLaptopShouldBeFound("screenSize.greaterThanOrEqual=" + DEFAULT_SCREEN_SIZE);

        // Get all the laptopList where screenSize is greater than or equal to UPDATED_SCREEN_SIZE
        defaultLaptopShouldNotBeFound("screenSize.greaterThanOrEqual=" + UPDATED_SCREEN_SIZE);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenSize is less than or equal to DEFAULT_SCREEN_SIZE
        defaultLaptopShouldBeFound("screenSize.lessThanOrEqual=" + DEFAULT_SCREEN_SIZE);

        // Get all the laptopList where screenSize is less than or equal to SMALLER_SCREEN_SIZE
        defaultLaptopShouldNotBeFound("screenSize.lessThanOrEqual=" + SMALLER_SCREEN_SIZE);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenSize is less than DEFAULT_SCREEN_SIZE
        defaultLaptopShouldNotBeFound("screenSize.lessThan=" + DEFAULT_SCREEN_SIZE);

        // Get all the laptopList where screenSize is less than UPDATED_SCREEN_SIZE
        defaultLaptopShouldBeFound("screenSize.lessThan=" + UPDATED_SCREEN_SIZE);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenSize is greater than DEFAULT_SCREEN_SIZE
        defaultLaptopShouldNotBeFound("screenSize.greaterThan=" + DEFAULT_SCREEN_SIZE);

        // Get all the laptopList where screenSize is greater than SMALLER_SCREEN_SIZE
        defaultLaptopShouldBeFound("screenSize.greaterThan=" + SMALLER_SCREEN_SIZE);
    }

    @Test
    @Transactional
    void getAllLaptopsByMemoryIsEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where memory equals to DEFAULT_MEMORY
        defaultLaptopShouldBeFound("memory.equals=" + DEFAULT_MEMORY);

        // Get all the laptopList where memory equals to UPDATED_MEMORY
        defaultLaptopShouldNotBeFound("memory.equals=" + UPDATED_MEMORY);
    }

    @Test
    @Transactional
    void getAllLaptopsByMemoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where memory not equals to DEFAULT_MEMORY
        defaultLaptopShouldNotBeFound("memory.notEquals=" + DEFAULT_MEMORY);

        // Get all the laptopList where memory not equals to UPDATED_MEMORY
        defaultLaptopShouldBeFound("memory.notEquals=" + UPDATED_MEMORY);
    }

    @Test
    @Transactional
    void getAllLaptopsByMemoryIsInShouldWork() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where memory in DEFAULT_MEMORY or UPDATED_MEMORY
        defaultLaptopShouldBeFound("memory.in=" + DEFAULT_MEMORY + "," + UPDATED_MEMORY);

        // Get all the laptopList where memory equals to UPDATED_MEMORY
        defaultLaptopShouldNotBeFound("memory.in=" + UPDATED_MEMORY);
    }

    @Test
    @Transactional
    void getAllLaptopsByMemoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where memory is not null
        defaultLaptopShouldBeFound("memory.specified=true");

        // Get all the laptopList where memory is null
        defaultLaptopShouldNotBeFound("memory.specified=false");
    }

    @Test
    @Transactional
    void getAllLaptopsByMemoryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where memory is greater than or equal to DEFAULT_MEMORY
        defaultLaptopShouldBeFound("memory.greaterThanOrEqual=" + DEFAULT_MEMORY);

        // Get all the laptopList where memory is greater than or equal to UPDATED_MEMORY
        defaultLaptopShouldNotBeFound("memory.greaterThanOrEqual=" + UPDATED_MEMORY);
    }

    @Test
    @Transactional
    void getAllLaptopsByMemoryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where memory is less than or equal to DEFAULT_MEMORY
        defaultLaptopShouldBeFound("memory.lessThanOrEqual=" + DEFAULT_MEMORY);

        // Get all the laptopList where memory is less than or equal to SMALLER_MEMORY
        defaultLaptopShouldNotBeFound("memory.lessThanOrEqual=" + SMALLER_MEMORY);
    }

    @Test
    @Transactional
    void getAllLaptopsByMemoryIsLessThanSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where memory is less than DEFAULT_MEMORY
        defaultLaptopShouldNotBeFound("memory.lessThan=" + DEFAULT_MEMORY);

        // Get all the laptopList where memory is less than UPDATED_MEMORY
        defaultLaptopShouldBeFound("memory.lessThan=" + UPDATED_MEMORY);
    }

    @Test
    @Transactional
    void getAllLaptopsByMemoryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where memory is greater than DEFAULT_MEMORY
        defaultLaptopShouldNotBeFound("memory.greaterThan=" + DEFAULT_MEMORY);

        // Get all the laptopList where memory is greater than SMALLER_MEMORY
        defaultLaptopShouldBeFound("memory.greaterThan=" + SMALLER_MEMORY);
    }

    @Test
    @Transactional
    void getAllLaptopsByStorageIsEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where storage equals to DEFAULT_STORAGE
        defaultLaptopShouldBeFound("storage.equals=" + DEFAULT_STORAGE);

        // Get all the laptopList where storage equals to UPDATED_STORAGE
        defaultLaptopShouldNotBeFound("storage.equals=" + UPDATED_STORAGE);
    }

    @Test
    @Transactional
    void getAllLaptopsByStorageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where storage not equals to DEFAULT_STORAGE
        defaultLaptopShouldNotBeFound("storage.notEquals=" + DEFAULT_STORAGE);

        // Get all the laptopList where storage not equals to UPDATED_STORAGE
        defaultLaptopShouldBeFound("storage.notEquals=" + UPDATED_STORAGE);
    }

    @Test
    @Transactional
    void getAllLaptopsByStorageIsInShouldWork() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where storage in DEFAULT_STORAGE or UPDATED_STORAGE
        defaultLaptopShouldBeFound("storage.in=" + DEFAULT_STORAGE + "," + UPDATED_STORAGE);

        // Get all the laptopList where storage equals to UPDATED_STORAGE
        defaultLaptopShouldNotBeFound("storage.in=" + UPDATED_STORAGE);
    }

    @Test
    @Transactional
    void getAllLaptopsByStorageIsNullOrNotNull() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where storage is not null
        defaultLaptopShouldBeFound("storage.specified=true");

        // Get all the laptopList where storage is null
        defaultLaptopShouldNotBeFound("storage.specified=false");
    }

    @Test
    @Transactional
    void getAllLaptopsByStorageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where storage is greater than or equal to DEFAULT_STORAGE
        defaultLaptopShouldBeFound("storage.greaterThanOrEqual=" + DEFAULT_STORAGE);

        // Get all the laptopList where storage is greater than or equal to UPDATED_STORAGE
        defaultLaptopShouldNotBeFound("storage.greaterThanOrEqual=" + UPDATED_STORAGE);
    }

    @Test
    @Transactional
    void getAllLaptopsByStorageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where storage is less than or equal to DEFAULT_STORAGE
        defaultLaptopShouldBeFound("storage.lessThanOrEqual=" + DEFAULT_STORAGE);

        // Get all the laptopList where storage is less than or equal to SMALLER_STORAGE
        defaultLaptopShouldNotBeFound("storage.lessThanOrEqual=" + SMALLER_STORAGE);
    }

    @Test
    @Transactional
    void getAllLaptopsByStorageIsLessThanSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where storage is less than DEFAULT_STORAGE
        defaultLaptopShouldNotBeFound("storage.lessThan=" + DEFAULT_STORAGE);

        // Get all the laptopList where storage is less than UPDATED_STORAGE
        defaultLaptopShouldBeFound("storage.lessThan=" + UPDATED_STORAGE);
    }

    @Test
    @Transactional
    void getAllLaptopsByStorageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where storage is greater than DEFAULT_STORAGE
        defaultLaptopShouldNotBeFound("storage.greaterThan=" + DEFAULT_STORAGE);

        // Get all the laptopList where storage is greater than SMALLER_STORAGE
        defaultLaptopShouldBeFound("storage.greaterThan=" + SMALLER_STORAGE);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenBrightnessIsEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenBrightness equals to DEFAULT_SCREEN_BRIGHTNESS
        defaultLaptopShouldBeFound("screenBrightness.equals=" + DEFAULT_SCREEN_BRIGHTNESS);

        // Get all the laptopList where screenBrightness equals to UPDATED_SCREEN_BRIGHTNESS
        defaultLaptopShouldNotBeFound("screenBrightness.equals=" + UPDATED_SCREEN_BRIGHTNESS);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenBrightnessIsNotEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenBrightness not equals to DEFAULT_SCREEN_BRIGHTNESS
        defaultLaptopShouldNotBeFound("screenBrightness.notEquals=" + DEFAULT_SCREEN_BRIGHTNESS);

        // Get all the laptopList where screenBrightness not equals to UPDATED_SCREEN_BRIGHTNESS
        defaultLaptopShouldBeFound("screenBrightness.notEquals=" + UPDATED_SCREEN_BRIGHTNESS);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenBrightnessIsInShouldWork() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenBrightness in DEFAULT_SCREEN_BRIGHTNESS or UPDATED_SCREEN_BRIGHTNESS
        defaultLaptopShouldBeFound("screenBrightness.in=" + DEFAULT_SCREEN_BRIGHTNESS + "," + UPDATED_SCREEN_BRIGHTNESS);

        // Get all the laptopList where screenBrightness equals to UPDATED_SCREEN_BRIGHTNESS
        defaultLaptopShouldNotBeFound("screenBrightness.in=" + UPDATED_SCREEN_BRIGHTNESS);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenBrightnessIsNullOrNotNull() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenBrightness is not null
        defaultLaptopShouldBeFound("screenBrightness.specified=true");

        // Get all the laptopList where screenBrightness is null
        defaultLaptopShouldNotBeFound("screenBrightness.specified=false");
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenBrightnessIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenBrightness is greater than or equal to DEFAULT_SCREEN_BRIGHTNESS
        defaultLaptopShouldBeFound("screenBrightness.greaterThanOrEqual=" + DEFAULT_SCREEN_BRIGHTNESS);

        // Get all the laptopList where screenBrightness is greater than or equal to UPDATED_SCREEN_BRIGHTNESS
        defaultLaptopShouldNotBeFound("screenBrightness.greaterThanOrEqual=" + UPDATED_SCREEN_BRIGHTNESS);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenBrightnessIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenBrightness is less than or equal to DEFAULT_SCREEN_BRIGHTNESS
        defaultLaptopShouldBeFound("screenBrightness.lessThanOrEqual=" + DEFAULT_SCREEN_BRIGHTNESS);

        // Get all the laptopList where screenBrightness is less than or equal to SMALLER_SCREEN_BRIGHTNESS
        defaultLaptopShouldNotBeFound("screenBrightness.lessThanOrEqual=" + SMALLER_SCREEN_BRIGHTNESS);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenBrightnessIsLessThanSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenBrightness is less than DEFAULT_SCREEN_BRIGHTNESS
        defaultLaptopShouldNotBeFound("screenBrightness.lessThan=" + DEFAULT_SCREEN_BRIGHTNESS);

        // Get all the laptopList where screenBrightness is less than UPDATED_SCREEN_BRIGHTNESS
        defaultLaptopShouldBeFound("screenBrightness.lessThan=" + UPDATED_SCREEN_BRIGHTNESS);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenBrightnessIsGreaterThanSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenBrightness is greater than DEFAULT_SCREEN_BRIGHTNESS
        defaultLaptopShouldNotBeFound("screenBrightness.greaterThan=" + DEFAULT_SCREEN_BRIGHTNESS);

        // Get all the laptopList where screenBrightness is greater than SMALLER_SCREEN_BRIGHTNESS
        defaultLaptopShouldBeFound("screenBrightness.greaterThan=" + SMALLER_SCREEN_BRIGHTNESS);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenRefreshHzIsEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenRefreshHz equals to DEFAULT_SCREEN_REFRESH_HZ
        defaultLaptopShouldBeFound("screenRefreshHz.equals=" + DEFAULT_SCREEN_REFRESH_HZ);

        // Get all the laptopList where screenRefreshHz equals to UPDATED_SCREEN_REFRESH_HZ
        defaultLaptopShouldNotBeFound("screenRefreshHz.equals=" + UPDATED_SCREEN_REFRESH_HZ);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenRefreshHzIsNotEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenRefreshHz not equals to DEFAULT_SCREEN_REFRESH_HZ
        defaultLaptopShouldNotBeFound("screenRefreshHz.notEquals=" + DEFAULT_SCREEN_REFRESH_HZ);

        // Get all the laptopList where screenRefreshHz not equals to UPDATED_SCREEN_REFRESH_HZ
        defaultLaptopShouldBeFound("screenRefreshHz.notEquals=" + UPDATED_SCREEN_REFRESH_HZ);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenRefreshHzIsInShouldWork() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenRefreshHz in DEFAULT_SCREEN_REFRESH_HZ or UPDATED_SCREEN_REFRESH_HZ
        defaultLaptopShouldBeFound("screenRefreshHz.in=" + DEFAULT_SCREEN_REFRESH_HZ + "," + UPDATED_SCREEN_REFRESH_HZ);

        // Get all the laptopList where screenRefreshHz equals to UPDATED_SCREEN_REFRESH_HZ
        defaultLaptopShouldNotBeFound("screenRefreshHz.in=" + UPDATED_SCREEN_REFRESH_HZ);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenRefreshHzIsNullOrNotNull() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenRefreshHz is not null
        defaultLaptopShouldBeFound("screenRefreshHz.specified=true");

        // Get all the laptopList where screenRefreshHz is null
        defaultLaptopShouldNotBeFound("screenRefreshHz.specified=false");
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenRefreshHzIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenRefreshHz is greater than or equal to DEFAULT_SCREEN_REFRESH_HZ
        defaultLaptopShouldBeFound("screenRefreshHz.greaterThanOrEqual=" + DEFAULT_SCREEN_REFRESH_HZ);

        // Get all the laptopList where screenRefreshHz is greater than or equal to UPDATED_SCREEN_REFRESH_HZ
        defaultLaptopShouldNotBeFound("screenRefreshHz.greaterThanOrEqual=" + UPDATED_SCREEN_REFRESH_HZ);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenRefreshHzIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenRefreshHz is less than or equal to DEFAULT_SCREEN_REFRESH_HZ
        defaultLaptopShouldBeFound("screenRefreshHz.lessThanOrEqual=" + DEFAULT_SCREEN_REFRESH_HZ);

        // Get all the laptopList where screenRefreshHz is less than or equal to SMALLER_SCREEN_REFRESH_HZ
        defaultLaptopShouldNotBeFound("screenRefreshHz.lessThanOrEqual=" + SMALLER_SCREEN_REFRESH_HZ);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenRefreshHzIsLessThanSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenRefreshHz is less than DEFAULT_SCREEN_REFRESH_HZ
        defaultLaptopShouldNotBeFound("screenRefreshHz.lessThan=" + DEFAULT_SCREEN_REFRESH_HZ);

        // Get all the laptopList where screenRefreshHz is less than UPDATED_SCREEN_REFRESH_HZ
        defaultLaptopShouldBeFound("screenRefreshHz.lessThan=" + UPDATED_SCREEN_REFRESH_HZ);
    }

    @Test
    @Transactional
    void getAllLaptopsByScreenRefreshHzIsGreaterThanSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where screenRefreshHz is greater than DEFAULT_SCREEN_REFRESH_HZ
        defaultLaptopShouldNotBeFound("screenRefreshHz.greaterThan=" + DEFAULT_SCREEN_REFRESH_HZ);

        // Get all the laptopList where screenRefreshHz is greater than SMALLER_SCREEN_REFRESH_HZ
        defaultLaptopShouldBeFound("screenRefreshHz.greaterThan=" + SMALLER_SCREEN_REFRESH_HZ);
    }

    @Test
    @Transactional
    void getAllLaptopsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where price equals to DEFAULT_PRICE
        defaultLaptopShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the laptopList where price equals to UPDATED_PRICE
        defaultLaptopShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllLaptopsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where price not equals to DEFAULT_PRICE
        defaultLaptopShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the laptopList where price not equals to UPDATED_PRICE
        defaultLaptopShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllLaptopsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultLaptopShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the laptopList where price equals to UPDATED_PRICE
        defaultLaptopShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllLaptopsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where price is not null
        defaultLaptopShouldBeFound("price.specified=true");

        // Get all the laptopList where price is null
        defaultLaptopShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllLaptopsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where price is greater than or equal to DEFAULT_PRICE
        defaultLaptopShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the laptopList where price is greater than or equal to UPDATED_PRICE
        defaultLaptopShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllLaptopsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where price is less than or equal to DEFAULT_PRICE
        defaultLaptopShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the laptopList where price is less than or equal to SMALLER_PRICE
        defaultLaptopShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllLaptopsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where price is less than DEFAULT_PRICE
        defaultLaptopShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the laptopList where price is less than UPDATED_PRICE
        defaultLaptopShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllLaptopsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        // Get all the laptopList where price is greater than DEFAULT_PRICE
        defaultLaptopShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the laptopList where price is greater than SMALLER_PRICE
        defaultLaptopShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLaptopShouldBeFound(String filter) throws Exception {
        restLaptopMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(laptop.getId().intValue())))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].modelName").value(hasItem(DEFAULT_MODEL_NAME)))
            .andExpect(jsonPath("$.[*].screenSize").value(hasItem(DEFAULT_SCREEN_SIZE.doubleValue())))
            .andExpect(jsonPath("$.[*].memory").value(hasItem(DEFAULT_MEMORY)))
            .andExpect(jsonPath("$.[*].storage").value(hasItem(DEFAULT_STORAGE.doubleValue())))
            .andExpect(jsonPath("$.[*].screenBrightness").value(hasItem(DEFAULT_SCREEN_BRIGHTNESS)))
            .andExpect(jsonPath("$.[*].screenRefreshHz").value(hasItem(DEFAULT_SCREEN_REFRESH_HZ)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));

        // Check, that the count call also returns 1
        restLaptopMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLaptopShouldNotBeFound(String filter) throws Exception {
        restLaptopMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLaptopMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLaptop() throws Exception {
        // Get the laptop
        restLaptopMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLaptop() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        int databaseSizeBeforeUpdate = laptopRepository.findAll().size();

        // Update the laptop
        Laptop updatedLaptop = laptopRepository.findById(laptop.getId()).get();
        // Disconnect from session so that the updates on updatedLaptop are not directly saved in db
        em.detach(updatedLaptop);
        updatedLaptop
            .brandName(UPDATED_BRAND_NAME)
            .modelName(UPDATED_MODEL_NAME)
            .screenSize(UPDATED_SCREEN_SIZE)
            .memory(UPDATED_MEMORY)
            .storage(UPDATED_STORAGE)
            .screenBrightness(UPDATED_SCREEN_BRIGHTNESS)
            .screenRefreshHz(UPDATED_SCREEN_REFRESH_HZ)
            .price(UPDATED_PRICE);

        restLaptopMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLaptop.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLaptop))
            )
            .andExpect(status().isOk());

        // Validate the Laptop in the database
        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeUpdate);
        Laptop testLaptop = laptopList.get(laptopList.size() - 1);
        assertThat(testLaptop.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testLaptop.getModelName()).isEqualTo(UPDATED_MODEL_NAME);
        assertThat(testLaptop.getScreenSize()).isEqualTo(UPDATED_SCREEN_SIZE);
        assertThat(testLaptop.getMemory()).isEqualTo(UPDATED_MEMORY);
        assertThat(testLaptop.getStorage()).isEqualTo(UPDATED_STORAGE);
        assertThat(testLaptop.getScreenBrightness()).isEqualTo(UPDATED_SCREEN_BRIGHTNESS);
        assertThat(testLaptop.getScreenRefreshHz()).isEqualTo(UPDATED_SCREEN_REFRESH_HZ);
        assertThat(testLaptop.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingLaptop() throws Exception {
        int databaseSizeBeforeUpdate = laptopRepository.findAll().size();
        laptop.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaptopMockMvc
            .perform(
                put(ENTITY_API_URL_ID, laptop.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laptop))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laptop in the database
        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLaptop() throws Exception {
        int databaseSizeBeforeUpdate = laptopRepository.findAll().size();
        laptop.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaptopMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(laptop))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laptop in the database
        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLaptop() throws Exception {
        int databaseSizeBeforeUpdate = laptopRepository.findAll().size();
        laptop.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaptopMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(laptop)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Laptop in the database
        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLaptopWithPatch() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        int databaseSizeBeforeUpdate = laptopRepository.findAll().size();

        // Update the laptop using partial update
        Laptop partialUpdatedLaptop = new Laptop();
        partialUpdatedLaptop.setId(laptop.getId());

        partialUpdatedLaptop.brandName(UPDATED_BRAND_NAME).screenSize(UPDATED_SCREEN_SIZE).screenBrightness(UPDATED_SCREEN_BRIGHTNESS);

        restLaptopMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLaptop.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLaptop))
            )
            .andExpect(status().isOk());

        // Validate the Laptop in the database
        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeUpdate);
        Laptop testLaptop = laptopList.get(laptopList.size() - 1);
        assertThat(testLaptop.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testLaptop.getModelName()).isEqualTo(DEFAULT_MODEL_NAME);
        assertThat(testLaptop.getScreenSize()).isEqualTo(UPDATED_SCREEN_SIZE);
        assertThat(testLaptop.getMemory()).isEqualTo(DEFAULT_MEMORY);
        assertThat(testLaptop.getStorage()).isEqualTo(DEFAULT_STORAGE);
        assertThat(testLaptop.getScreenBrightness()).isEqualTo(UPDATED_SCREEN_BRIGHTNESS);
        assertThat(testLaptop.getScreenRefreshHz()).isEqualTo(DEFAULT_SCREEN_REFRESH_HZ);
        assertThat(testLaptop.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateLaptopWithPatch() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        int databaseSizeBeforeUpdate = laptopRepository.findAll().size();

        // Update the laptop using partial update
        Laptop partialUpdatedLaptop = new Laptop();
        partialUpdatedLaptop.setId(laptop.getId());

        partialUpdatedLaptop
            .brandName(UPDATED_BRAND_NAME)
            .modelName(UPDATED_MODEL_NAME)
            .screenSize(UPDATED_SCREEN_SIZE)
            .memory(UPDATED_MEMORY)
            .storage(UPDATED_STORAGE)
            .screenBrightness(UPDATED_SCREEN_BRIGHTNESS)
            .screenRefreshHz(UPDATED_SCREEN_REFRESH_HZ)
            .price(UPDATED_PRICE);

        restLaptopMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLaptop.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLaptop))
            )
            .andExpect(status().isOk());

        // Validate the Laptop in the database
        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeUpdate);
        Laptop testLaptop = laptopList.get(laptopList.size() - 1);
        assertThat(testLaptop.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testLaptop.getModelName()).isEqualTo(UPDATED_MODEL_NAME);
        assertThat(testLaptop.getScreenSize()).isEqualTo(UPDATED_SCREEN_SIZE);
        assertThat(testLaptop.getMemory()).isEqualTo(UPDATED_MEMORY);
        assertThat(testLaptop.getStorage()).isEqualTo(UPDATED_STORAGE);
        assertThat(testLaptop.getScreenBrightness()).isEqualTo(UPDATED_SCREEN_BRIGHTNESS);
        assertThat(testLaptop.getScreenRefreshHz()).isEqualTo(UPDATED_SCREEN_REFRESH_HZ);
        assertThat(testLaptop.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingLaptop() throws Exception {
        int databaseSizeBeforeUpdate = laptopRepository.findAll().size();
        laptop.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLaptopMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, laptop.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(laptop))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laptop in the database
        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLaptop() throws Exception {
        int databaseSizeBeforeUpdate = laptopRepository.findAll().size();
        laptop.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaptopMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(laptop))
            )
            .andExpect(status().isBadRequest());

        // Validate the Laptop in the database
        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLaptop() throws Exception {
        int databaseSizeBeforeUpdate = laptopRepository.findAll().size();
        laptop.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLaptopMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(laptop)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Laptop in the database
        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLaptop() throws Exception {
        // Initialize the database
        laptopRepository.saveAndFlush(laptop);

        int databaseSizeBeforeDelete = laptopRepository.findAll().size();

        // Delete the laptop
        restLaptopMockMvc
            .perform(delete(ENTITY_API_URL_ID, laptop.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Laptop> laptopList = laptopRepository.findAll();
        assertThat(laptopList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
