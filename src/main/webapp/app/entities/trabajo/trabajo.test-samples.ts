import { ITrabajo, NewTrabajo } from './trabajo.model';

export const sampleWithRequiredData: ITrabajo = {
  id: 85961,
};

export const sampleWithPartialData: ITrabajo = {
  id: 78836,
  tituloTrabajo: 'pixel withdrawal Pataca',
};

export const sampleWithFullData: ITrabajo = {
  id: 52745,
  tituloTrabajo: 'Ladrillo',
  salarioMin: 55281,
  salarioMax: 21679,
};

export const sampleWithNewData: NewTrabajo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
