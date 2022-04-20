package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Laptop;
import com.mycompany.myapp.repository.LaptopRepository;
import com.mycompany.myapp.service.LaptopQueryService;
import com.mycompany.myapp.service.LaptopService;
import com.mycompany.myapp.service.criteria.LaptopCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Laptop}.
 */
@RestController
@RequestMapping("/api")
public class LaptopResource {

    private final Logger log = LoggerFactory.getLogger(LaptopResource.class);

    private static final String ENTITY_NAME = "laptop";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LaptopService laptopService;

    private final LaptopRepository laptopRepository;

    private final LaptopQueryService laptopQueryService;

    public LaptopResource(LaptopService laptopService, LaptopRepository laptopRepository, LaptopQueryService laptopQueryService) {
        this.laptopService = laptopService;
        this.laptopRepository = laptopRepository;
        this.laptopQueryService = laptopQueryService;
    }

    /**
     * {@code POST  /laptops} : Create a new laptop.
     *
     * @param laptop the laptop to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new laptop, or with status {@code 400 (Bad Request)} if the laptop has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/laptops")
    public ResponseEntity<Laptop> createLaptop(@Valid @RequestBody Laptop laptop) throws URISyntaxException {
        log.debug("REST request to save Laptop : {}", laptop);
        if (laptop.getId() != null) {
            throw new BadRequestAlertException("A new laptop cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Laptop result = laptopService.save(laptop);
        return ResponseEntity
            .created(new URI("/api/laptops/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /laptops/:id} : Updates an existing laptop.
     *
     * @param id the id of the laptop to save.
     * @param laptop the laptop to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated laptop,
     * or with status {@code 400 (Bad Request)} if the laptop is not valid,
     * or with status {@code 500 (Internal Server Error)} if the laptop couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/laptops/{id}")
    public ResponseEntity<Laptop> updateLaptop(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Laptop laptop
    ) throws URISyntaxException {
        log.debug("REST request to update Laptop : {}, {}", id, laptop);
        if (laptop.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, laptop.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!laptopRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Laptop result = laptopService.update(laptop);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, laptop.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /laptops/:id} : Partial updates given fields of an existing laptop, field will ignore if it is null
     *
     * @param id the id of the laptop to save.
     * @param laptop the laptop to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated laptop,
     * or with status {@code 400 (Bad Request)} if the laptop is not valid,
     * or with status {@code 404 (Not Found)} if the laptop is not found,
     * or with status {@code 500 (Internal Server Error)} if the laptop couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/laptops/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Laptop> partialUpdateLaptop(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Laptop laptop
    ) throws URISyntaxException {
        log.debug("REST request to partial update Laptop partially : {}, {}", id, laptop);
        if (laptop.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, laptop.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!laptopRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Laptop> result = laptopService.partialUpdate(laptop);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, laptop.getId().toString())
        );
    }

    /**
     * {@code GET  /laptops} : get all the laptops.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of laptops in body.
     */
    @GetMapping("/laptops")
    public ResponseEntity<List<Laptop>> getAllLaptops(
        LaptopCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Laptops by criteria: {}", criteria);
        Page<Laptop> page = laptopQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /laptops/count} : count all the laptops.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/laptops/count")
    public ResponseEntity<Long> countLaptops(LaptopCriteria criteria) {
        log.debug("REST request to count Laptops by criteria: {}", criteria);
        return ResponseEntity.ok().body(laptopQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /laptops/:id} : get the "id" laptop.
     *
     * @param id the id of the laptop to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the laptop, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/laptops/{id}")
    public ResponseEntity<Laptop> getLaptop(@PathVariable Long id) {
        log.debug("REST request to get Laptop : {}", id);
        Optional<Laptop> laptop = laptopService.findOne(id);
        return ResponseUtil.wrapOrNotFound(laptop);
    }

    /**
     * {@code DELETE  /laptops/:id} : delete the "id" laptop.
     *
     * @param id the id of the laptop to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/laptops/{id}")
    public ResponseEntity<Void> deleteLaptop(@PathVariable Long id) {
        log.debug("REST request to delete Laptop : {}", id);
        laptopService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
