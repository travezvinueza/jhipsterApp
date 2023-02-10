import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDepartamento } from '../departamento.model';
import { DepartamentoService } from '../service/departamento.service';

@Injectable({ providedIn: 'root' })
export class DepartamentoRoutingResolveService implements Resolve<IDepartamento | null> {
  constructor(protected service: DepartamentoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDepartamento | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((departamento: HttpResponse<IDepartamento>) => {
          if (departamento.body) {
            return of(departamento.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
