package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Laptop} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.LaptopResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /laptops?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class LaptopCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter brandName;

    private StringFilter modelName;

    private FloatFilter screenSize;

    private IntegerFilter memory;

    private FloatFilter storage;

    private IntegerFilter screenBrightness;

    private IntegerFilter screenRefreshHz;

    private FloatFilter price;

    private Boolean distinct;

    public LaptopCriteria() {}

    public LaptopCriteria(LaptopCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.brandName = other.brandName == null ? null : other.brandName.copy();
        this.modelName = other.modelName == null ? null : other.modelName.copy();
        this.screenSize = other.screenSize == null ? null : other.screenSize.copy();
        this.memory = other.memory == null ? null : other.memory.copy();
        this.storage = other.storage == null ? null : other.storage.copy();
        this.screenBrightness = other.screenBrightness == null ? null : other.screenBrightness.copy();
        this.screenRefreshHz = other.screenRefreshHz == null ? null : other.screenRefreshHz.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LaptopCriteria copy() {
        return new LaptopCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBrandName() {
        return brandName;
    }

    public StringFilter brandName() {
        if (brandName == null) {
            brandName = new StringFilter();
        }
        return brandName;
    }

    public void setBrandName(StringFilter brandName) {
        this.brandName = brandName;
    }

    public StringFilter getModelName() {
        return modelName;
    }

    public StringFilter modelName() {
        if (modelName == null) {
            modelName = new StringFilter();
        }
        return modelName;
    }

    public void setModelName(StringFilter modelName) {
        this.modelName = modelName;
    }

    public FloatFilter getScreenSize() {
        return screenSize;
    }

    public FloatFilter screenSize() {
        if (screenSize == null) {
            screenSize = new FloatFilter();
        }
        return screenSize;
    }

    public void setScreenSize(FloatFilter screenSize) {
        this.screenSize = screenSize;
    }

    public IntegerFilter getMemory() {
        return memory;
    }

    public IntegerFilter memory() {
        if (memory == null) {
            memory = new IntegerFilter();
        }
        return memory;
    }

    public void setMemory(IntegerFilter memory) {
        this.memory = memory;
    }

    public FloatFilter getStorage() {
        return storage;
    }

    public FloatFilter storage() {
        if (storage == null) {
            storage = new FloatFilter();
        }
        return storage;
    }

    public void setStorage(FloatFilter storage) {
        this.storage = storage;
    }

    public IntegerFilter getScreenBrightness() {
        return screenBrightness;
    }

    public IntegerFilter screenBrightness() {
        if (screenBrightness == null) {
            screenBrightness = new IntegerFilter();
        }
        return screenBrightness;
    }

    public void setScreenBrightness(IntegerFilter screenBrightness) {
        this.screenBrightness = screenBrightness;
    }

    public IntegerFilter getScreenRefreshHz() {
        return screenRefreshHz;
    }

    public IntegerFilter screenRefreshHz() {
        if (screenRefreshHz == null) {
            screenRefreshHz = new IntegerFilter();
        }
        return screenRefreshHz;
    }

    public void setScreenRefreshHz(IntegerFilter screenRefreshHz) {
        this.screenRefreshHz = screenRefreshHz;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public FloatFilter price() {
        if (price == null) {
            price = new FloatFilter();
        }
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LaptopCriteria that = (LaptopCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(brandName, that.brandName) &&
            Objects.equals(modelName, that.modelName) &&
            Objects.equals(screenSize, that.screenSize) &&
            Objects.equals(memory, that.memory) &&
            Objects.equals(storage, that.storage) &&
            Objects.equals(screenBrightness, that.screenBrightness) &&
            Objects.equals(screenRefreshHz, that.screenRefreshHz) &&
            Objects.equals(price, that.price) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brandName, modelName, screenSize, memory, storage, screenBrightness, screenRefreshHz, price, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LaptopCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (brandName != null ? "brandName=" + brandName + ", " : "") +
            (modelName != null ? "modelName=" + modelName + ", " : "") +
            (screenSize != null ? "screenSize=" + screenSize + ", " : "") +
            (memory != null ? "memory=" + memory + ", " : "") +
            (storage != null ? "storage=" + storage + ", " : "") +
            (screenBrightness != null ? "screenBrightness=" + screenBrightness + ", " : "") +
            (screenRefreshHz != null ? "screenRefreshHz=" + screenRefreshHz + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
