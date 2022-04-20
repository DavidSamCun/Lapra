package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Laptop;
import com.mycompany.myapp.repository.LaptopRepository;
import com.mycompany.myapp.service.LaptopService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Laptop}.
 */
@Service
@Transactional
public class LaptopServiceImpl implements LaptopService {

    private final Logger log = LoggerFactory.getLogger(LaptopServiceImpl.class);

    private final LaptopRepository laptopRepository;

    public LaptopServiceImpl(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    @Override
    public Laptop save(Laptop laptop) {
        log.debug("Request to save Laptop : {}", laptop);
        return laptopRepository.save(laptop);
    }

    @Override
    public Laptop update(Laptop laptop) {
        log.debug("Request to save Laptop : {}", laptop);
        return laptopRepository.save(laptop);
    }

    @Override
    public Optional<Laptop> partialUpdate(Laptop laptop) {
        log.debug("Request to partially update Laptop : {}", laptop);

        return laptopRepository
            .findById(laptop.getId())
            .map(existingLaptop -> {
                if (laptop.getBrandName() != null) {
                    existingLaptop.setBrandName(laptop.getBrandName());
                }
                if (laptop.getModelName() != null) {
                    existingLaptop.setModelName(laptop.getModelName());
                }
                if (laptop.getScreenSize() != null) {
                    existingLaptop.setScreenSize(laptop.getScreenSize());
                }
                if (laptop.getMemory() != null) {
                    existingLaptop.setMemory(laptop.getMemory());
                }
                if (laptop.getStorage() != null) {
                    existingLaptop.setStorage(laptop.getStorage());
                }
                if (laptop.getScreenBrightness() != null) {
                    existingLaptop.setScreenBrightness(laptop.getScreenBrightness());
                }
                if (laptop.getScreenRefreshHz() != null) {
                    existingLaptop.setScreenRefreshHz(laptop.getScreenRefreshHz());
                }
                if (laptop.getPrice() != null) {
                    existingLaptop.setPrice(laptop.getPrice());
                }

                return existingLaptop;
            })
            .map(laptopRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Laptop> findAll(Pageable pageable) {
        log.debug("Request to get all Laptops");
        return laptopRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Laptop> findOne(Long id) {
        log.debug("Request to get Laptop : {}", id);
        return laptopRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Laptop : {}", id);
        laptopRepository.deleteById(id);
    }
}
