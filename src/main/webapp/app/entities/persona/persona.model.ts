export interface IPersona {
  id: number;
  nombre?: string | null;
  apellido?: string | null;
}

export type NewPersona = Omit<IPersona, 'id'> & { id: null };
