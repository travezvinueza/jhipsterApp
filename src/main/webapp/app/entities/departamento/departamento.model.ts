import { IDireccion } from 'app/entities/direccion/direccion.model';

export interface IDepartamento {
  id: number;
  nombreDepartamento?: string | null;
  direccion?: Pick<IDireccion, 'id'> | null;
}

export type NewDepartamento = Omit<IDepartamento, 'id'> & { id: null };
