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
 * Criteria class for the {@link com.mycompany.myapp.domain.Questionnaire} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.QuestionnaireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /questionnaires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class QuestionnaireCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter answer1UsedAt;

    private StringFilter answer2Type;

    private IntegerFilter answer3Size;

    private StringFilter answer4Function;

    private IntegerFilter answer5Price;

    private LongFilter modelId;

    private LongFilter assignedToId;

    private Boolean distinct;

    public QuestionnaireCriteria() {}

    public QuestionnaireCriteria(QuestionnaireCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.answer1UsedAt = other.answer1UsedAt == null ? null : other.answer1UsedAt.copy();
        this.answer2Type = other.answer2Type == null ? null : other.answer2Type.copy();
        this.answer3Size = other.answer3Size == null ? null : other.answer3Size.copy();
        this.answer4Function = other.answer4Function == null ? null : other.answer4Function.copy();
        this.answer5Price = other.answer5Price == null ? null : other.answer5Price.copy();
        this.modelId = other.modelId == null ? null : other.modelId.copy();
        this.assignedToId = other.assignedToId == null ? null : other.assignedToId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public QuestionnaireCriteria copy() {
        return new QuestionnaireCriteria(this);
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

    public StringFilter getAnswer1UsedAt() {
        return answer1UsedAt;
    }

    public StringFilter answer1UsedAt() {
        if (answer1UsedAt == null) {
            answer1UsedAt = new StringFilter();
        }
        return answer1UsedAt;
    }

    public void setAnswer1UsedAt(StringFilter answer1UsedAt) {
        this.answer1UsedAt = answer1UsedAt;
    }

    public StringFilter getAnswer2Type() {
        return answer2Type;
    }

    public StringFilter answer2Type() {
        if (answer2Type == null) {
            answer2Type = new StringFilter();
        }
        return answer2Type;
    }

    public void setAnswer2Type(StringFilter answer2Type) {
        this.answer2Type = answer2Type;
    }

    public IntegerFilter getAnswer3Size() {
        return answer3Size;
    }

    public IntegerFilter answer3Size() {
        if (answer3Size == null) {
            answer3Size = new IntegerFilter();
        }
        return answer3Size;
    }

    public void setAnswer3Size(IntegerFilter answer3Size) {
        this.answer3Size = answer3Size;
    }

    public StringFilter getAnswer4Function() {
        return answer4Function;
    }

    public StringFilter answer4Function() {
        if (answer4Function == null) {
            answer4Function = new StringFilter();
        }
        return answer4Function;
    }

    public void setAnswer4Function(StringFilter answer4Function) {
        this.answer4Function = answer4Function;
    }

    public IntegerFilter getAnswer5Price() {
        return answer5Price;
    }

    public IntegerFilter answer5Price() {
        if (answer5Price == null) {
            answer5Price = new IntegerFilter();
        }
        return answer5Price;
    }

    public void setAnswer5Price(IntegerFilter answer5Price) {
        this.answer5Price = answer5Price;
    }

    public LongFilter getModelId() {
        return modelId;
    }

    public LongFilter modelId() {
        if (modelId == null) {
            modelId = new LongFilter();
        }
        return modelId;
    }

    public void setModelId(LongFilter modelId) {
        this.modelId = modelId;
    }

    public LongFilter getAssignedToId() {
        return assignedToId;
    }

    public LongFilter assignedToId() {
        if (assignedToId == null) {
            assignedToId = new LongFilter();
        }
        return assignedToId;
    }

    public void setAssignedToId(LongFilter assignedToId) {
        this.assignedToId = assignedToId;
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
        final QuestionnaireCriteria that = (QuestionnaireCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(answer1UsedAt, that.answer1UsedAt) &&
            Objects.equals(answer2Type, that.answer2Type) &&
            Objects.equals(answer3Size, that.answer3Size) &&
            Objects.equals(answer4Function, that.answer4Function) &&
            Objects.equals(answer5Price, that.answer5Price) &&
            Objects.equals(modelId, that.modelId) &&
            Objects.equals(assignedToId, that.assignedToId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer1UsedAt, answer2Type, answer3Size, answer4Function, answer5Price, modelId, assignedToId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuestionnaireCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (answer1UsedAt != null ? "answer1UsedAt=" + answer1UsedAt + ", " : "") +
            (answer2Type != null ? "answer2Type=" + answer2Type + ", " : "") +
            (answer3Size != null ? "answer3Size=" + answer3Size + ", " : "") +
            (answer4Function != null ? "answer4Function=" + answer4Function + ", " : "") +
            (answer5Price != null ? "answer5Price=" + answer5Price + ", " : "") +
            (modelId != null ? "modelId=" + modelId + ", " : "") +
            (assignedToId != null ? "assignedToId=" + assignedToId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
