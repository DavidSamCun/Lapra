package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Laptop;
import com.mycompany.myapp.repository.LaptopRepository;
import com.mycompany.myapp.service.criteria.LaptopCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Laptop} entities in the database.
 * The main input is a {@link LaptopCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Laptop} or a {@link Page} of {@link Laptop} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LaptopQueryService extends QueryService<Laptop> {

    private final Logger log = LoggerFactory.getLogger(LaptopQueryService.class);

    private final LaptopRepository laptopRepository;

    public LaptopQueryService(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    /**
     * Return a {@link List} of {@link Laptop} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Laptop> findByCriteria(LaptopCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Laptop> specification = createSpecification(criteria);
        return laptopRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Laptop} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Laptop> findByCriteria(LaptopCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Laptop> specification = createSpecification(criteria);
        return laptopRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LaptopCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Laptop> specification = createSpecification(criteria);
        return laptopRepository.count(specification);
    }

    /**
     * Function to convert {@link LaptopCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Laptop> createSpecification(LaptopCriteria criteria) {
        Specification<Laptop> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Laptop_.id));
            }
            if (criteria.getBrandName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBrandName(), Laptop_.brandName));
            }
            if (criteria.getModelName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelName(), Laptop_.modelName));
            }
            if (criteria.getScreenSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScreenSize(), Laptop_.screenSize));
            }
            if (criteria.getMemory() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMemory(), Laptop_.memory));
            }
            if (criteria.getStorage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStorage(), Laptop_.storage));
            }
            if (criteria.getScreenBrightness() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScreenBrightness(), Laptop_.screenBrightness));
            }
            if (criteria.getScreenRefreshHz() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScreenRefreshHz(), Laptop_.screenRefreshHz));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Laptop_.price));
            }
        }
        return specification;
    }
}
