import { ITarea, NewTarea } from './tarea.model';

export const sampleWithRequiredData: ITarea = {
  id: 58845,
};

export const sampleWithPartialData: ITarea = {
  id: 24990,
};

export const sampleWithFullData: ITarea = {
  id: 36813,
  titulo: 'Agente connect Dram',
  descripcion: 'Marca Cliente productize',
};

export const sampleWithNewData: NewTarea = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
