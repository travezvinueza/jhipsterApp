import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITrabajo, NewTrabajo } from '../trabajo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITrabajo for edit and NewTrabajoFormGroupInput for create.
 */
type TrabajoFormGroupInput = ITrabajo | PartialWithRequiredKeyOf<NewTrabajo>;

type TrabajoFormDefaults = Pick<NewTrabajo, 'id' | 'tareas'>;

type TrabajoFormGroupContent = {
  id: FormControl<ITrabajo['id'] | NewTrabajo['id']>;
  tituloTrabajo: FormControl<ITrabajo['tituloTrabajo']>;
  salarioMin: FormControl<ITrabajo['salarioMin']>;
  salarioMax: FormControl<ITrabajo['salarioMax']>;
  tareas: FormControl<ITrabajo['tareas']>;
  empleado: FormControl<ITrabajo['empleado']>;
};

export type TrabajoFormGroup = FormGroup<TrabajoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TrabajoFormService {
  createTrabajoFormGroup(trabajo: TrabajoFormGroupInput = { id: null }): TrabajoFormGroup {
    const trabajoRawValue = {
      ...this.getFormDefaults(),
      ...trabajo,
    };
    return new FormGroup<TrabajoFormGroupContent>({
      id: new FormControl(
        { value: trabajoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tituloTrabajo: new FormControl(trabajoRawValue.tituloTrabajo),
      salarioMin: new FormControl(trabajoRawValue.salarioMin),
      salarioMax: new FormControl(trabajoRawValue.salarioMax),
      tareas: new FormControl(trabajoRawValue.tareas ?? []),
      empleado: new FormControl(trabajoRawValue.empleado),
    });
  }

  getTrabajo(form: TrabajoFormGroup): ITrabajo | NewTrabajo {
    return form.getRawValue() as ITrabajo | NewTrabajo;
  }

  resetForm(form: TrabajoFormGroup, trabajo: TrabajoFormGroupInput): void {
    const trabajoRawValue = { ...this.getFormDefaults(), ...trabajo };
    form.reset(
      {
        ...trabajoRawValue,
        id: { value: trabajoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TrabajoFormDefaults {
    return {
      id: null,
      tareas: [],
    };
  }
}
