package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Laptop;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Laptop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LaptopRepository extends JpaRepository<Laptop, Long>, JpaSpecificationExecutor<Laptop> {}
