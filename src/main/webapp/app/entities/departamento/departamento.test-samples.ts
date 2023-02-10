import { IDepartamento, NewDepartamento } from './departamento.model';

export const sampleWithRequiredData: IDepartamento = {
  id: 6660,
  nombreDepartamento: 'hack Rústico auxiliary',
};

export const sampleWithPartialData: IDepartamento = {
  id: 75316,
  nombreDepartamento: 'tolerante',
};

export const sampleWithFullData: IDepartamento = {
  id: 78169,
  nombreDepartamento: 'conglomeración B2C',
};

export const sampleWithNewData: NewDepartamento = {
  nombreDepartamento: 'holística hibrida',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
