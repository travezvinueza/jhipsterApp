import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContrato } from '../contrato.model';
import { ContratoService } from '../service/contrato.service';

@Injectable({ providedIn: 'root' })
export class ContratoRoutingResolveService implements Resolve<IContrato | null> {
  constructor(protected service: ContratoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContrato | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contrato: HttpResponse<IContrato>) => {
          if (contrato.body) {
            return of(contrato.body);
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
