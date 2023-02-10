import { ITrabajo } from 'app/entities/trabajo/trabajo.model';

export interface ITarea {
  id: number;
  titulo?: string | null;
  descripcion?: string | null;
  trabajos?: Pick<ITrabajo, 'id'>[] | null;
}

export type NewTarea = Omit<ITarea, 'id'> & { id: null };
