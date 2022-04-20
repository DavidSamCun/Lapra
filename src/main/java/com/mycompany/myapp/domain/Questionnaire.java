package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Questionnaire.
 */
@Entity
@Table(name = "questionnaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Questionnaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "answer_1_used_at")
    private String answer1UsedAt;

    @Column(name = "answer_2_type")
    private String answer2Type;

    @Column(name = "answer_3_size")
    private Integer answer3Size;

    @Column(name = "answer_4_function")
    private String answer4Function;

    @Column(name = "answer_5_price")
    private Integer answer5Price;

    @ManyToOne
    private Laptop model;

    @ManyToOne
    private User assignedTo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Questionnaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer1UsedAt() {
        return this.answer1UsedAt;
    }

    public Questionnaire answer1UsedAt(String answer1UsedAt) {
        this.setAnswer1UsedAt(answer1UsedAt);
        return this;
    }

    public void setAnswer1UsedAt(String answer1UsedAt) {
        this.answer1UsedAt = answer1UsedAt;
    }

    public String getAnswer2Type() {
        return this.answer2Type;
    }

    public Questionnaire answer2Type(String answer2Type) {
        this.setAnswer2Type(answer2Type);
        return this;
    }

    public void setAnswer2Type(String answer2Type) {
        this.answer2Type = answer2Type;
    }

    public Integer getAnswer3Size() {
        return this.answer3Size;
    }

    public Questionnaire answer3Size(Integer answer3Size) {
        this.setAnswer3Size(answer3Size);
        return this;
    }

    public void setAnswer3Size(Integer answer3Size) {
        this.answer3Size = answer3Size;
    }

    public String getAnswer4Function() {
        return this.answer4Function;
    }

    public Questionnaire answer4Function(String answer4Function) {
        this.setAnswer4Function(answer4Function);
        return this;
    }

    public void setAnswer4Function(String answer4Function) {
        this.answer4Function = answer4Function;
    }

    public Integer getAnswer5Price() {
        return this.answer5Price;
    }

    public Questionnaire answer5Price(Integer answer5Price) {
        this.setAnswer5Price(answer5Price);
        return this;
    }

    public void setAnswer5Price(Integer answer5Price) {
        this.answer5Price = answer5Price;
    }

    public Laptop getModel() {
        return this.model;
    }

    public void setModel(Laptop laptop) {
        this.model = laptop;
    }

    public Questionnaire model(Laptop laptop) {
        this.setModel(laptop);
        return this;
    }

    public User getAssignedTo() {
        return this.assignedTo;
    }

    public void setAssignedTo(User user) {
        this.assignedTo = user;
    }

    public Questionnaire assignedTo(User user) {
        this.setAssignedTo(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Questionnaire)) {
            return false;
        }
        return id != null && id.equals(((Questionnaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Questionnaire{" +
            "id=" + getId() +
            ", answer1UsedAt='" + getAnswer1UsedAt() + "'" +
            ", answer2Type='" + getAnswer2Type() + "'" +
            ", answer3Size=" + getAnswer3Size() +
            ", answer4Function='" + getAnswer4Function() + "'" +
            ", answer5Price=" + getAnswer5Price() +
            "}";
    }
}
