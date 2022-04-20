import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILaptop, getLaptopIdentifier } from '../laptop.model';

export type EntityResponseType = HttpResponse<ILaptop>;
export type EntityArrayResponseType = HttpResponse<ILaptop[]>;

@Injectable({ providedIn: 'root' })
export class LaptopService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/laptops');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(laptop: ILaptop): Observable<EntityResponseType> {
    return this.http.post<ILaptop>(this.resourceUrl, laptop, { observe: 'response' });
  }

  update(laptop: ILaptop): Observable<EntityResponseType> {
    return this.http.put<ILaptop>(`${this.resourceUrl}/${getLaptopIdentifier(laptop) as number}`, laptop, { observe: 'response' });
  }

  partialUpdate(laptop: ILaptop): Observable<EntityResponseType> {
    return this.http.patch<ILaptop>(`${this.resourceUrl}/${getLaptopIdentifier(laptop) as number}`, laptop, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILaptop>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILaptop[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLaptopToCollectionIfMissing(laptopCollection: ILaptop[], ...laptopsToCheck: (ILaptop | null | undefined)[]): ILaptop[] {
    const laptops: ILaptop[] = laptopsToCheck.filter(isPresent);
    if (laptops.length > 0) {
      const laptopCollectionIdentifiers = laptopCollection.map(laptopItem => getLaptopIdentifier(laptopItem)!);
      const laptopsToAdd = laptops.filter(laptopItem => {
        const laptopIdentifier = getLaptopIdentifier(laptopItem);
        if (laptopIdentifier == null || laptopCollectionIdentifiers.includes(laptopIdentifier)) {
          return false;
        }
        laptopCollectionIdentifiers.push(laptopIdentifier);
        return true;
      });
      return [...laptopsToAdd, ...laptopCollection];
    }
    return laptopCollection;
  }
}
