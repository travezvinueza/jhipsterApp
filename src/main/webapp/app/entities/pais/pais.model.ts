import { IRegion } from 'app/entities/region/region.model';

export interface IPais {
  id: number;
  nombrePais?: string | null;
  region?: Pick<IRegion, 'id'> | null;
}

export type NewPais = Omit<IPais, 'id'> & { id: null };
