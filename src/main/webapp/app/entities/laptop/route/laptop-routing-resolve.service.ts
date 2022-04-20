import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILaptop, Laptop } from '../laptop.model';
import { LaptopService } from '../service/laptop.service';

@Injectable({ providedIn: 'root' })
export class LaptopRoutingResolveService implements Resolve<ILaptop> {
  constructor(protected service: LaptopService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILaptop> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((laptop: HttpResponse<Laptop>) => {
          if (laptop.body) {
            return of(laptop.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Laptop());
  }
}
