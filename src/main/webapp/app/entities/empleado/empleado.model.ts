import dayjs from 'dayjs/esm';
import { IDepartamento } from 'app/entities/departamento/departamento.model';

export interface IEmpleado {
  id: number;
  nombres?: string | null;
  apellidos?: string | null;
  correo?: string | null;
  nroCelular?: string | null;
  fechaContrato?: dayjs.Dayjs | null;
  salario?: number | null;
  comisionPorcentaje?: number | null;
  inmediatosuperior?: Pick<IEmpleado, 'id'> | null;
  departamento?: Pick<IDepartamento, 'id'> | null;
}

export type NewEmpleado = Omit<IEmpleado, 'id'> & { id: null };
