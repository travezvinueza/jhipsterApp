import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersona, NewPersona } from '../persona.model';

export type PartialUpdatePersona = Partial<IPersona> & Pick<IPersona, 'id'>;

export type EntityResponseType = HttpResponse<IPersona>;
export type EntityArrayResponseType = HttpResponse<IPersona[]>;

@Injectable({ providedIn: 'root' })
export class PersonaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(persona: NewPersona): Observable<EntityResponseType> {
    return this.http.post<IPersona>(this.resourceUrl, persona, { observe: 'response' });
  }

  update(persona: IPersona): Observable<EntityResponseType> {
    return this.http.put<IPersona>(`${this.resourceUrl}/${this.getPersonaIdentifier(persona)}`, persona, { observe: 'response' });
  }

  partialUpdate(persona: PartialUpdatePersona): Observable<EntityResponseType> {
    return this.http.patch<IPersona>(`${this.resourceUrl}/${this.getPersonaIdentifier(persona)}`, persona, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersona>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersona[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPersonaIdentifier(persona: Pick<IPersona, 'id'>): number {
    return persona.id;
  }

  comparePersona(o1: Pick<IPersona, 'id'> | null, o2: Pick<IPersona, 'id'> | null): boolean {
    return o1 && o2 ? this.getPersonaIdentifier(o1) === this.getPersonaIdentifier(o2) : o1 === o2;
  }

  addPersonaToCollectionIfMissing<Type extends Pick<IPersona, 'id'>>(
    personaCollection: Type[],
    ...personasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const personas: Type[] = personasToCheck.filter(isPresent);
    if (personas.length > 0) {
      const personaCollectionIdentifiers = personaCollection.map(personaItem => this.getPersonaIdentifier(personaItem)!);
      const personasToAdd = personas.filter(personaItem => {
        const personaIdentifier = this.getPersonaIdentifier(personaItem);
        if (personaCollectionIdentifiers.includes(personaIdentifier)) {
          return false;
        }
        personaCollectionIdentifiers.push(personaIdentifier);
        return true;
      });
      return [...personasToAdd, ...personaCollection];
    }
    return personaCollection;
  }
}
