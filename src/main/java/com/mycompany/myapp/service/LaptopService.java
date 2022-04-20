package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Laptop;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Laptop}.
 */
public interface LaptopService {
    /**
     * Save a laptop.
     *
     * @param laptop the entity to save.
     * @return the persisted entity.
     */
    Laptop save(Laptop laptop);

    /**
     * Updates a laptop.
     *
     * @param laptop the entity to update.
     * @return the persisted entity.
     */
    Laptop update(Laptop laptop);

    /**
     * Partially updates a laptop.
     *
     * @param laptop the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Laptop> partialUpdate(Laptop laptop);

    /**
     * Get all the laptops.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Laptop> findAll(Pageable pageable);

    /**
     * Get the "id" laptop.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Laptop> findOne(Long id);

    /**
     * Delete the "id" laptop.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
