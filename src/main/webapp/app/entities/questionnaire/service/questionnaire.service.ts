import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuestionnaire, getQuestionnaireIdentifier } from '../questionnaire.model';

export type EntityResponseType = HttpResponse<IQuestionnaire>;
export type EntityArrayResponseType = HttpResponse<IQuestionnaire[]>;

@Injectable({ providedIn: 'root' })
export class QuestionnaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/questionnaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(questionnaire: IQuestionnaire): Observable<EntityResponseType> {
    return this.http.post<IQuestionnaire>(this.resourceUrl, questionnaire, { observe: 'response' });
  }

  update(questionnaire: IQuestionnaire): Observable<EntityResponseType> {
    return this.http.put<IQuestionnaire>(`${this.resourceUrl}/${getQuestionnaireIdentifier(questionnaire) as number}`, questionnaire, {
      observe: 'response',
    });
  }

  partialUpdate(questionnaire: IQuestionnaire): Observable<EntityResponseType> {
    return this.http.patch<IQuestionnaire>(`${this.resourceUrl}/${getQuestionnaireIdentifier(questionnaire) as number}`, questionnaire, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuestionnaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuestionnaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuestionnaireToCollectionIfMissing(
    questionnaireCollection: IQuestionnaire[],
    ...questionnairesToCheck: (IQuestionnaire | null | undefined)[]
  ): IQuestionnaire[] {
    const questionnaires: IQuestionnaire[] = questionnairesToCheck.filter(isPresent);
    if (questionnaires.length > 0) {
      const questionnaireCollectionIdentifiers = questionnaireCollection.map(
        questionnaireItem => getQuestionnaireIdentifier(questionnaireItem)!
      );
      const questionnairesToAdd = questionnaires.filter(questionnaireItem => {
        const questionnaireIdentifier = getQuestionnaireIdentifier(questionnaireItem);
        if (questionnaireIdentifier == null || questionnaireCollectionIdentifiers.includes(questionnaireIdentifier)) {
          return false;
        }
        questionnaireCollectionIdentifiers.push(questionnaireIdentifier);
        return true;
      });
      return [...questionnairesToAdd, ...questionnaireCollection];
    }
    return questionnaireCollection;
  }
}
