import { ITarea } from 'app/entities/tarea/tarea.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';

export interface ITrabajo {
  id: number;
  tituloTrabajo?: string | null;
  salarioMin?: number | null;
  salarioMax?: number | null;
  tareas?: Pick<ITarea, 'id' | 'titulo'>[] | null;
  empleado?: Pick<IEmpleado, 'id'> | null;
}

export type NewTrabajo = Omit<ITrabajo, 'id'> & { id: null };
