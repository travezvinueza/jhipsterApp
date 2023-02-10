import dayjs from 'dayjs/esm';

import { IEmpleado, NewEmpleado } from './empleado.model';

export const sampleWithRequiredData: IEmpleado = {
  id: 19124,
};

export const sampleWithPartialData: IEmpleado = {
  id: 22495,
  apellidos: 'payment Joyería',
  correo: 'Expandido',
  nroCelular: 'program Dinánmico',
  fechaContrato: dayjs('2023-02-10T02:35'),
};

export const sampleWithFullData: IEmpleado = {
  id: 65340,
  nombres: 'País niches Parafarmacia',
  apellidos: 'Gambia payment',
  correo: 'Ronda Cambridgeshire',
  nroCelular: 'Money payment Extrarradio',
  fechaContrato: dayjs('2023-02-10T17:20'),
  salario: 33706,
  comisionPorcentaje: 85706,
};

export const sampleWithNewData: NewEmpleado = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
