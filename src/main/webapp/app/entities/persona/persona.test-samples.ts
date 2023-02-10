import { IPersona, NewPersona } from './persona.model';

export const sampleWithRequiredData: IPersona = {
  id: 56801,
  nombre: 'Borders compress Guapa',
  apellido: 'quantifying',
};

export const sampleWithPartialData: IPersona = {
  id: 68263,
  nombre: 'deposit mobile contexto',
  apellido: 'implementación Ladrillo Account',
};

export const sampleWithFullData: IPersona = {
  id: 15156,
  nombre: 'Bricolaje',
  apellido: 'Account Comunicaciones Toallas',
};

export const sampleWithNewData: NewPersona = {
  nombre: 'Guapa e-markets estrategia',
  apellido: 'Metal Investment granular',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
