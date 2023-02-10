export interface IRegion {
  id: number;
  nombreRegion?: string | null;
}

export type NewRegion = Omit<IRegion, 'id'> & { id: null };
