import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LaptopComponent } from '../list/laptop.component';
import { LaptopDetailComponent } from '../detail/laptop-detail.component';
import { LaptopUpdateComponent } from '../update/laptop-update.component';
import { LaptopRoutingResolveService } from './laptop-routing-resolve.service';

const laptopRoute: Routes = [
  {
    path: '',
    component: LaptopComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LaptopDetailComponent,
    resolve: {
      laptop: LaptopRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LaptopUpdateComponent,
    resolve: {
      laptop: LaptopRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LaptopUpdateComponent,
    resolve: {
      laptop: LaptopRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(laptopRoute)],
  exports: [RouterModule],
})
export class LaptopRoutingModule {}
