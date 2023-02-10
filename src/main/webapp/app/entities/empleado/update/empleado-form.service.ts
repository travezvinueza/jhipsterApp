import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmpleado, NewEmpleado } from '../empleado.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmpleado for edit and NewEmpleadoFormGroupInput for create.
 */
type EmpleadoFormGroupInput = IEmpleado | PartialWithRequiredKeyOf<NewEmpleado>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmpleado | NewEmpleado> = Omit<T, 'fechaContrato'> & {
  fechaContrato?: string | null;
};

type EmpleadoFormRawValue = FormValueOf<IEmpleado>;

type NewEmpleadoFormRawValue = FormValueOf<NewEmpleado>;

type EmpleadoFormDefaults = Pick<NewEmpleado, 'id' | 'fechaContrato'>;

type EmpleadoFormGroupContent = {
  id: FormControl<EmpleadoFormRawValue['id'] | NewEmpleado['id']>;
  nombres: FormControl<EmpleadoFormRawValue['nombres']>;
  apellidos: FormControl<EmpleadoFormRawValue['apellidos']>;
  correo: FormControl<EmpleadoFormRawValue['correo']>;
  nroCelular: FormControl<EmpleadoFormRawValue['nroCelular']>;
  fechaContrato: FormControl<EmpleadoFormRawValue['fechaContrato']>;
  salario: FormControl<EmpleadoFormRawValue['salario']>;
  comisionPorcentaje: FormControl<EmpleadoFormRawValue['comisionPorcentaje']>;
  inmediatosuperior: FormControl<EmpleadoFormRawValue['inmediatosuperior']>;
  departamento: FormControl<EmpleadoFormRawValue['departamento']>;
};

export type EmpleadoFormGroup = FormGroup<EmpleadoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmpleadoFormService {
  createEmpleadoFormGroup(empleado: EmpleadoFormGroupInput = { id: null }): EmpleadoFormGroup {
    const empleadoRawValue = this.convertEmpleadoToEmpleadoRawValue({
      ...this.getFormDefaults(),
      ...empleado,
    });
    return new FormGroup<EmpleadoFormGroupContent>({
      id: new FormControl(
        { value: empleadoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombres: new FormControl(empleadoRawValue.nombres),
      apellidos: new FormControl(empleadoRawValue.apellidos),
      correo: new FormControl(empleadoRawValue.correo),
      nroCelular: new FormControl(empleadoRawValue.nroCelular),
      fechaContrato: new FormControl(empleadoRawValue.fechaContrato),
      salario: new FormControl(empleadoRawValue.salario),
      comisionPorcentaje: new FormControl(empleadoRawValue.comisionPorcentaje),
      inmediatosuperior: new FormControl(empleadoRawValue.inmediatosuperior),
      departamento: new FormControl(empleadoRawValue.departamento),
    });
  }

  getEmpleado(form: EmpleadoFormGroup): IEmpleado | NewEmpleado {
    return this.convertEmpleadoRawValueToEmpleado(form.getRawValue() as EmpleadoFormRawValue | NewEmpleadoFormRawValue);
  }

  resetForm(form: EmpleadoFormGroup, empleado: EmpleadoFormGroupInput): void {
    const empleadoRawValue = this.convertEmpleadoToEmpleadoRawValue({ ...this.getFormDefaults(), ...empleado });
    form.reset(
      {
        ...empleadoRawValue,
        id: { value: empleadoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmpleadoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaContrato: currentTime,
    };
  }

  private convertEmpleadoRawValueToEmpleado(rawEmpleado: EmpleadoFormRawValue | NewEmpleadoFormRawValue): IEmpleado | NewEmpleado {
    return {
      ...rawEmpleado,
      fechaContrato: dayjs(rawEmpleado.fechaContrato, DATE_TIME_FORMAT),
    };
  }

  private convertEmpleadoToEmpleadoRawValue(
    empleado: IEmpleado | (Partial<NewEmpleado> & EmpleadoFormDefaults)
  ): EmpleadoFormRawValue | PartialWithRequiredKeyOf<NewEmpleadoFormRawValue> {
    return {
      ...empleado,
      fechaContrato: empleado.fechaContrato ? empleado.fechaContrato.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
