import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonaComponent } from '../list/persona.component';
import { PersonaDetailComponent } from '../detail/persona-detail.component';
import { PersonaUpdateComponent } from '../update/persona-update.component';
import { PersonaRoutingResolveService } from './persona-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const personaRoute: Routes = [
  {
    path: '',
    component: PersonaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonaDetailComponent,
    resolve: {
      persona: PersonaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonaUpdateComponent,
    resolve: {
      persona: PersonaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonaUpdateComponent,
    resolve: {
      persona: PersonaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personaRoute)],
  exports: [RouterModule],
})
export class PersonaRoutingModule {}
