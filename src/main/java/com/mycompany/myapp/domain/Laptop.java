package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Laptop.
 */
@Entity
@Table(name = "laptop")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Laptop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "brand_name", length = 30, nullable = false)
    private String brandName;

    @NotNull
    @Size(max = 30)
    @Column(name = "model_name", length = 30, nullable = false)
    private String modelName;

    @Column(name = "screen_size")
    private Float screenSize;

    @Column(name = "memory")
    private Integer memory;

    @Column(name = "storage")
    private Float storage;

    @Column(name = "screen_brightness")
    private Integer screenBrightness;

    @Column(name = "screen_refresh_hz")
    private Integer screenRefreshHz;

    @Column(name = "price")
    private Float price;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Laptop id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public Laptop brandName(String brandName) {
        this.setBrandName(brandName);
        return this;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelName() {
        return this.modelName;
    }

    public Laptop modelName(String modelName) {
        this.setModelName(modelName);
        return this;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Float getScreenSize() {
        return this.screenSize;
    }

    public Laptop screenSize(Float screenSize) {
        this.setScreenSize(screenSize);
        return this;
    }

    public void setScreenSize(Float screenSize) {
        this.screenSize = screenSize;
    }

    public Integer getMemory() {
        return this.memory;
    }

    public Laptop memory(Integer memory) {
        this.setMemory(memory);
        return this;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Float getStorage() {
        return this.storage;
    }

    public Laptop storage(Float storage) {
        this.setStorage(storage);
        return this;
    }

    public void setStorage(Float storage) {
        this.storage = storage;
    }

    public Integer getScreenBrightness() {
        return this.screenBrightness;
    }

    public Laptop screenBrightness(Integer screenBrightness) {
        this.setScreenBrightness(screenBrightness);
        return this;
    }

    public void setScreenBrightness(Integer screenBrightness) {
        this.screenBrightness = screenBrightness;
    }

    public Integer getScreenRefreshHz() {
        return this.screenRefreshHz;
    }

    public Laptop screenRefreshHz(Integer screenRefreshHz) {
        this.setScreenRefreshHz(screenRefreshHz);
        return this;
    }

    public void setScreenRefreshHz(Integer screenRefreshHz) {
        this.screenRefreshHz = screenRefreshHz;
    }

    public Float getPrice() {
        return this.price;
    }

    public Laptop price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Laptop)) {
            return false;
        }
        return id != null && id.equals(((Laptop) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Laptop{" +
            "id=" + getId() +
            ", brandName='" + getBrandName() + "'" +
            ", modelName='" + getModelName() + "'" +
            ", screenSize=" + getScreenSize() +
            ", memory=" + getMemory() +
            ", storage=" + getStorage() +
            ", screenBrightness=" + getScreenBrightness() +
            ", screenRefreshHz=" + getScreenRefreshHz() +
            ", price=" + getPrice() +
            "}";
    }
}
