import dayjs from 'dayjs/esm';
import { ITrabajo } from 'app/entities/trabajo/trabajo.model';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { Idioma } from 'app/entities/enumerations/idioma.model';

export interface IContrato {
  id: number;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  lenguaje?: Idioma | null;
  trabajo?: Pick<ITrabajo, 'id'> | null;
  departamento?: Pick<IDepartamento, 'id'> | null;
  empleado?: Pick<IEmpleado, 'id'> | null;
}

export type NewContrato = Omit<IContrato, 'id'> & { id: null };
