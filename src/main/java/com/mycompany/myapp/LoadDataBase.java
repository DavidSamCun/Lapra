package com.mycompany.myapp;

import com.mycompany.myapp.domain.Laptop;
import com.mycompany.myapp.repository.LaptopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(LaptopRepository repository) {

        return args -> {

            Laptop zephyrusG15 = new Laptop();
            zephyrusG15.setBrandName("Asus");
            zephyrusG15.setModelName("ZephyrusG15");
            zephyrusG15.setScreenSize(15F);
            zephyrusG15.setMemory(16);
            zephyrusG15.storage(1F);
            zephyrusG15.setScreenBrightness(300);
            zephyrusG15.setScreenRefreshHz(165);
            zephyrusG15.setPrice(1850F);

            Laptop zephyrusG14 = new Laptop();
            zephyrusG14.setBrandName("Asus");
            zephyrusG14.setModelName("ZephyrusG14");
            zephyrusG14.setScreenSize(14F);
            zephyrusG14.setMemory(16);
            zephyrusG14.storage(1F);
            zephyrusG14.setScreenBrightness(300);
            zephyrusG14.setScreenRefreshHz(120);
            zephyrusG14.setPrice(1550F);

//            Laptop dummyLaptop = new Laptop();
//            dummyLaptop.setBrandName("Asus");
//            dummyLaptop.setModelName("ZephyrusG15");
//            dummyLaptop.setScreenSize(14F);
//            dummyLaptop.setMemory(16);
//            dummyLaptop.storage(1F);
//            dummyLaptop.setScreenBrightness(300);
//            dummyLaptop.setScreenRefreshHz(165);
//            dummyLaptop.setPrice(100F);

            Laptop legion5i = new Laptop();
            legion5i.setBrandName("Lenovo");
            legion5i.setModelName("Legion5i");
            legion5i.setScreenSize(15F);
            legion5i.setMemory(16);
            legion5i.storage(1F);
            legion5i.setScreenBrightness(500);
            legion5i.setScreenRefreshHz(165);
            legion5i.setPrice(2460F);

            Laptop envyX360 = new Laptop();
            envyX360.setBrandName("HP");
            envyX360.setModelName("EnvyX360");
            envyX360.setScreenSize(15F);
            envyX360.setMemory(16);
            envyX360.storage(0.256F);
            envyX360.setScreenBrightness(400);
            envyX360.setScreenRefreshHz(60);
            envyX360.setPrice(1120F);


            log.info("Preloading " + repository.save(zephyrusG15));
            log.info("Preloading " + repository.save(zephyrusG14));
            log.info("Preloading " + repository.save(legion5i));
            log.info("Preloading " + repository.save(envyX360));


        };
    }
}
