import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrabajo, NewTrabajo } from '../trabajo.model';

export type PartialUpdateTrabajo = Partial<ITrabajo> & Pick<ITrabajo, 'id'>;

export type EntityResponseType = HttpResponse<ITrabajo>;
export type EntityArrayResponseType = HttpResponse<ITrabajo[]>;

@Injectable({ providedIn: 'root' })
export class TrabajoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trabajos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trabajo: NewTrabajo): Observable<EntityResponseType> {
    return this.http.post<ITrabajo>(this.resourceUrl, trabajo, { observe: 'response' });
  }

  update(trabajo: ITrabajo): Observable<EntityResponseType> {
    return this.http.put<ITrabajo>(`${this.resourceUrl}/${this.getTrabajoIdentifier(trabajo)}`, trabajo, { observe: 'response' });
  }

  partialUpdate(trabajo: PartialUpdateTrabajo): Observable<EntityResponseType> {
    return this.http.patch<ITrabajo>(`${this.resourceUrl}/${this.getTrabajoIdentifier(trabajo)}`, trabajo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITrabajo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrabajo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTrabajoIdentifier(trabajo: Pick<ITrabajo, 'id'>): number {
    return trabajo.id;
  }

  compareTrabajo(o1: Pick<ITrabajo, 'id'> | null, o2: Pick<ITrabajo, 'id'> | null): boolean {
    return o1 && o2 ? this.getTrabajoIdentifier(o1) === this.getTrabajoIdentifier(o2) : o1 === o2;
  }

  addTrabajoToCollectionIfMissing<Type extends Pick<ITrabajo, 'id'>>(
    trabajoCollection: Type[],
    ...trabajosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const trabajos: Type[] = trabajosToCheck.filter(isPresent);
    if (trabajos.length > 0) {
      const trabajoCollectionIdentifiers = trabajoCollection.map(trabajoItem => this.getTrabajoIdentifier(trabajoItem)!);
      const trabajosToAdd = trabajos.filter(trabajoItem => {
        const trabajoIdentifier = this.getTrabajoIdentifier(trabajoItem);
        if (trabajoCollectionIdentifiers.includes(trabajoIdentifier)) {
          return false;
        }
        trabajoCollectionIdentifiers.push(trabajoIdentifier);
        return true;
      });
      return [...trabajosToAdd, ...trabajoCollection];
    }
    return trabajoCollection;
  }
}
