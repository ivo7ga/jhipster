import * as dayjs from 'dayjs';

export interface IExperiencia {
  id?: number;
  titulo?: string;
  descripcion?: string | null;
  localizacion?: string | null;
  fecha?: dayjs.Dayjs | null;
}

export class Experiencia implements IExperiencia {
  constructor(
    public id?: number,
    public titulo?: string,
    public descripcion?: string | null,
    public localizacion?: string | null,
    public fecha?: dayjs.Dayjs | null
  ) {}
}

export function getExperienciaIdentifier(experiencia: IExperiencia): number | undefined {
  return experiencia.id;
}
