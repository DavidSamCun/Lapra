import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IQuestionnaire, Questionnaire } from '../questionnaire.model';

import { QuestionnaireService } from './questionnaire.service';

describe('Questionnaire Service', () => {
  let service: QuestionnaireService;
  let httpMock: HttpTestingController;
  let elemDefault: IQuestionnaire;
  let expectedResult: IQuestionnaire | IQuestionnaire[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(QuestionnaireService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      answer1UsedAt: 'AAAAAAA',
      answer2Type: 'AAAAAAA',
      answer3Size: 0,
      answer4Function: 'AAAAAAA',
      answer5Price: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Questionnaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Questionnaire()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Questionnaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          answer1UsedAt: 'BBBBBB',
          answer2Type: 'BBBBBB',
          answer3Size: 1,
          answer4Function: 'BBBBBB',
          answer5Price: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Questionnaire', () => {
      const patchObject = Object.assign(
        {
          answer5Price: 1,
        },
        new Questionnaire()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Questionnaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          answer1UsedAt: 'BBBBBB',
          answer2Type: 'BBBBBB',
          answer3Size: 1,
          answer4Function: 'BBBBBB',
          answer5Price: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Questionnaire', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addQuestionnaireToCollectionIfMissing', () => {
      it('should add a Questionnaire to an empty array', () => {
        const questionnaire: IQuestionnaire = { id: 123 };
        expectedResult = service.addQuestionnaireToCollectionIfMissing([], questionnaire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionnaire);
      });

      it('should not add a Questionnaire to an array that contains it', () => {
        const questionnaire: IQuestionnaire = { id: 123 };
        const questionnaireCollection: IQuestionnaire[] = [
          {
            ...questionnaire,
          },
          { id: 456 },
        ];
        expectedResult = service.addQuestionnaireToCollectionIfMissing(questionnaireCollection, questionnaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Questionnaire to an array that doesn't contain it", () => {
        const questionnaire: IQuestionnaire = { id: 123 };
        const questionnaireCollection: IQuestionnaire[] = [{ id: 456 }];
        expectedResult = service.addQuestionnaireToCollectionIfMissing(questionnaireCollection, questionnaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionnaire);
      });

      it('should add only unique Questionnaire to an array', () => {
        const questionnaireArray: IQuestionnaire[] = [{ id: 123 }, { id: 456 }, { id: 97843 }];
        const questionnaireCollection: IQuestionnaire[] = [{ id: 123 }];
        expectedResult = service.addQuestionnaireToCollectionIfMissing(questionnaireCollection, ...questionnaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const questionnaire: IQuestionnaire = { id: 123 };
        const questionnaire2: IQuestionnaire = { id: 456 };
        expectedResult = service.addQuestionnaireToCollectionIfMissing([], questionnaire, questionnaire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(questionnaire);
        expect(expectedResult).toContain(questionnaire2);
      });

      it('should accept null and undefined values', () => {
        const questionnaire: IQuestionnaire = { id: 123 };
        expectedResult = service.addQuestionnaireToCollectionIfMissing([], null, questionnaire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(questionnaire);
      });

      it('should return initial array if no Questionnaire is added', () => {
        const questionnaireCollection: IQuestionnaire[] = [{ id: 123 }];
        expectedResult = service.addQuestionnaireToCollectionIfMissing(questionnaireCollection, undefined, null);
        expect(expectedResult).toEqual(questionnaireCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
