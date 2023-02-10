import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrabajo } from '../trabajo.model';
import { TrabajoService } from '../service/trabajo.service';

@Injectable({ providedIn: 'root' })
export class TrabajoRoutingResolveService implements Resolve<ITrabajo | null> {
  constructor(protected service: TrabajoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrabajo | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trabajo: HttpResponse<ITrabajo>) => {
          if (trabajo.body) {
            return of(trabajo.body);
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
