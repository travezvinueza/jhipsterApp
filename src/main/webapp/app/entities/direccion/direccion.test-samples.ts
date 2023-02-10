import { IDireccion, NewDireccion } from './direccion.model';

export const sampleWithRequiredData: IDireccion = {
  id: 2397,
};

export const sampleWithPartialData: IDireccion = {
  id: 71030,
  calle: 'repurpose benchmark',
  ciudad: 'Pequeño Eritrea artificial',
};

export const sampleWithFullData: IDireccion = {
  id: 89854,
  calle: 'coherente implement',
  codigoPostal: 'local mobile',
  ciudad: 'Ladrillo deliverables',
  provincia: 'Cambridgeshire Juguetería Ladrillo',
};

export const sampleWithNewData: NewDireccion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
