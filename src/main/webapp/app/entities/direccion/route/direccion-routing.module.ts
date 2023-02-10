import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DireccionComponent } from '../list/direccion.component';
import { DireccionDetailComponent } from '../detail/direccion-detail.component';
import { DireccionUpdateComponent } from '../update/direccion-update.component';
import { DireccionRoutingResolveService } from './direccion-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const direccionRoute: Routes = [
  {
    path: '',
    component: DireccionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DireccionDetailComponent,
    resolve: {
      direccion: DireccionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DireccionUpdateComponent,
    resolve: {
      direccion: DireccionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DireccionUpdateComponent,
    resolve: {
      direccion: DireccionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(direccionRoute)],
  exports: [RouterModule],
})
export class DireccionRoutingModule {}
