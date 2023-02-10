import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDireccion, NewDireccion } from '../direccion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDireccion for edit and NewDireccionFormGroupInput for create.
 */
type DireccionFormGroupInput = IDireccion | PartialWithRequiredKeyOf<NewDireccion>;

type DireccionFormDefaults = Pick<NewDireccion, 'id'>;

type DireccionFormGroupContent = {
  id: FormControl<IDireccion['id'] | NewDireccion['id']>;
  calle: FormControl<IDireccion['calle']>;
  codigoPostal: FormControl<IDireccion['codigoPostal']>;
  ciudad: FormControl<IDireccion['ciudad']>;
  provincia: FormControl<IDireccion['provincia']>;
  pais: FormControl<IDireccion['pais']>;
};

export type DireccionFormGroup = FormGroup<DireccionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DireccionFormService {
  createDireccionFormGroup(direccion: DireccionFormGroupInput = { id: null }): DireccionFormGroup {
    const direccionRawValue = {
      ...this.getFormDefaults(),
      ...direccion,
    };
    return new FormGroup<DireccionFormGroupContent>({
      id: new FormControl(
        { value: direccionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      calle: new FormControl(direccionRawValue.calle),
      codigoPostal: new FormControl(direccionRawValue.codigoPostal),
      ciudad: new FormControl(direccionRawValue.ciudad),
      provincia: new FormControl(direccionRawValue.provincia),
      pais: new FormControl(direccionRawValue.pais),
    });
  }

  getDireccion(form: DireccionFormGroup): IDireccion | NewDireccion {
    return form.getRawValue() as IDireccion | NewDireccion;
  }

  resetForm(form: DireccionFormGroup, direccion: DireccionFormGroupInput): void {
    const direccionRawValue = { ...this.getFormDefaults(), ...direccion };
    form.reset(
      {
        ...direccionRawValue,
        id: { value: direccionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DireccionFormDefaults {
    return {
      id: null,
    };
  }
}
