import dayjs from 'dayjs/esm';

import { Idioma } from 'app/entities/enumerations/idioma.model';

import { IContrato, NewContrato } from './contrato.model';

export const sampleWithRequiredData: IContrato = {
  id: 24858,
};

export const sampleWithPartialData: IContrato = {
  id: 9181,
  fechaInicio: dayjs('2023-02-10T00:36'),
  fechaFin: dayjs('2023-02-10T10:06'),
};

export const sampleWithFullData: IContrato = {
  id: 51721,
  fechaInicio: dayjs('2023-02-10T17:08'),
  fechaFin: dayjs('2023-02-10T04:48'),
  lenguaje: Idioma['INGLES'],
};

export const sampleWithNewData: NewContrato = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
