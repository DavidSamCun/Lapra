package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LaptopTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Laptop.class);
        Laptop laptop1 = new Laptop();
        laptop1.setId(1L);
        Laptop laptop2 = new Laptop();
        laptop2.setId(laptop1.getId());
        assertThat(laptop1).isEqualTo(laptop2);
        laptop2.setId(2L);
        assertThat(laptop1).isNotEqualTo(laptop2);
        laptop1.setId(null);
        assertThat(laptop1).isNotEqualTo(laptop2);
    }
}
