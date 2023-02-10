import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IContrato, NewContrato } from '../contrato.model';

export type PartialUpdateContrato = Partial<IContrato> & Pick<IContrato, 'id'>;

type RestOf<T extends IContrato | NewContrato> = Omit<T, 'fechaInicio' | 'fechaFin'> & {
  fechaInicio?: string | null;
  fechaFin?: string | null;
};

export type RestContrato = RestOf<IContrato>;

export type NewRestContrato = RestOf<NewContrato>;

export type PartialUpdateRestContrato = RestOf<PartialUpdateContrato>;

export type EntityResponseType = HttpResponse<IContrato>;
export type EntityArrayResponseType = HttpResponse<IContrato[]>;

@Injectable({ providedIn: 'root' })
export class ContratoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/contratoes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(contrato: NewContrato): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contrato);
    return this.http
      .post<RestContrato>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contrato: IContrato): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contrato);
    return this.http
      .put<RestContrato>(`${this.resourceUrl}/${this.getContratoIdentifier(contrato)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contrato: PartialUpdateContrato): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(contrato);
    return this.http
      .patch<RestContrato>(`${this.resourceUrl}/${this.getContratoIdentifier(contrato)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestContrato>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContrato[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getContratoIdentifier(contrato: Pick<IContrato, 'id'>): number {
    return contrato.id;
  }

  compareContrato(o1: Pick<IContrato, 'id'> | null, o2: Pick<IContrato, 'id'> | null): boolean {
    return o1 && o2 ? this.getContratoIdentifier(o1) === this.getContratoIdentifier(o2) : o1 === o2;
  }

  addContratoToCollectionIfMissing<Type extends Pick<IContrato, 'id'>>(
    contratoCollection: Type[],
    ...contratoesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contratoes: Type[] = contratoesToCheck.filter(isPresent);
    if (contratoes.length > 0) {
      const contratoCollectionIdentifiers = contratoCollection.map(contratoItem => this.getContratoIdentifier(contratoItem)!);
      const contratoesToAdd = contratoes.filter(contratoItem => {
        const contratoIdentifier = this.getContratoIdentifier(contratoItem);
        if (contratoCollectionIdentifiers.includes(contratoIdentifier)) {
          return false;
        }
        contratoCollectionIdentifiers.push(contratoIdentifier);
        return true;
      });
      return [...contratoesToAdd, ...contratoCollection];
    }
    return contratoCollection;
  }

  protected convertDateFromClient<T extends IContrato | NewContrato | PartialUpdateContrato>(contrato: T): RestOf<T> {
    return {
      ...contrato,
      fechaInicio: contrato.fechaInicio?.toJSON() ?? null,
      fechaFin: contrato.fechaFin?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restContrato: RestContrato): IContrato {
    return {
      ...restContrato,
      fechaInicio: restContrato.fechaInicio ? dayjs(restContrato.fechaInicio) : undefined,
      fechaFin: restContrato.fechaFin ? dayjs(restContrato.fechaFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestContrato>): HttpResponse<IContrato> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestContrato[]>): HttpResponse<IContrato[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
