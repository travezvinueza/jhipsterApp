import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITarea, NewTarea } from '../tarea.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITarea for edit and NewTareaFormGroupInput for create.
 */
type TareaFormGroupInput = ITarea | PartialWithRequiredKeyOf<NewTarea>;

type TareaFormDefaults = Pick<NewTarea, 'id' | 'trabajos'>;

type TareaFormGroupContent = {
  id: FormControl<ITarea['id'] | NewTarea['id']>;
  titulo: FormControl<ITarea['titulo']>;
  descripcion: FormControl<ITarea['descripcion']>;
  trabajos: FormControl<ITarea['trabajos']>;
};

export type TareaFormGroup = FormGroup<TareaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TareaFormService {
  createTareaFormGroup(tarea: TareaFormGroupInput = { id: null }): TareaFormGroup {
    const tareaRawValue = {
      ...this.getFormDefaults(),
      ...tarea,
    };
    return new FormGroup<TareaFormGroupContent>({
      id: new FormControl(
        { value: tareaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      titulo: new FormControl(tareaRawValue.titulo),
      descripcion: new FormControl(tareaRawValue.descripcion),
      trabajos: new FormControl(tareaRawValue.trabajos ?? []),
    });
  }

  getTarea(form: TareaFormGroup): ITarea | NewTarea {
    return form.getRawValue() as ITarea | NewTarea;
  }

  resetForm(form: TareaFormGroup, tarea: TareaFormGroupInput): void {
    const tareaRawValue = { ...this.getFormDefaults(), ...tarea };
    form.reset(
      {
        ...tareaRawValue,
        id: { value: tareaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TareaFormDefaults {
    return {
      id: null,
      trabajos: [],
    };
  }
}
