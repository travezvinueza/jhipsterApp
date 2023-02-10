import { IPais, NewPais } from './pais.model';

export const sampleWithRequiredData: IPais = {
  id: 80172,
};

export const sampleWithPartialData: IPais = {
  id: 71912,
  nombrePais: 'Gerente',
};

export const sampleWithFullData: IPais = {
  id: 36287,
  nombrePais: 'Nacional Andalucía datos',
};

export const sampleWithNewData: NewPais = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
