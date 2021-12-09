import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExperienciaComponent } from '../list/experiencia.component';
import { ExperienciaDetailComponent } from '../detail/experiencia-detail.component';
import { ExperienciaUpdateComponent } from '../update/experiencia-update.component';
import { ExperienciaRoutingResolveService } from './experiencia-routing-resolve.service';

const experienciaRoute: Routes = [
  {
    path: '',
    component: ExperienciaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExperienciaDetailComponent,
    resolve: {
      experiencia: ExperienciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExperienciaUpdateComponent,
    resolve: {
      experiencia: ExperienciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExperienciaUpdateComponent,
    resolve: {
      experiencia: ExperienciaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(experienciaRoute)],
  exports: [RouterModule],
})
export class ExperienciaRoutingModule {}
