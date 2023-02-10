import { IPais } from 'app/entities/pais/pais.model';

export interface IDireccion {
  id: number;
  calle?: string | null;
  codigoPostal?: string | null;
  ciudad?: string | null;
  provincia?: string | null;
  pais?: Pick<IPais, 'id'> | null;
}

export type NewDireccion = Omit<IDireccion, 'id'> & { id: null };
