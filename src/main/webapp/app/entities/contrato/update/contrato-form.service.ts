import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IContrato, NewContrato } from '../contrato.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContrato for edit and NewContratoFormGroupInput for create.
 */
type ContratoFormGroupInput = IContrato | PartialWithRequiredKeyOf<NewContrato>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IContrato | NewContrato> = Omit<T, 'fechaInicio' | 'fechaFin'> & {
  fechaInicio?: string | null;
  fechaFin?: string | null;
};

type ContratoFormRawValue = FormValueOf<IContrato>;

type NewContratoFormRawValue = FormValueOf<NewContrato>;

type ContratoFormDefaults = Pick<NewContrato, 'id' | 'fechaInicio' | 'fechaFin'>;

type ContratoFormGroupContent = {
  id: FormControl<ContratoFormRawValue['id'] | NewContrato['id']>;
  fechaInicio: FormControl<ContratoFormRawValue['fechaInicio']>;
  fechaFin: FormControl<ContratoFormRawValue['fechaFin']>;
  lenguaje: FormControl<ContratoFormRawValue['lenguaje']>;
  trabajo: FormControl<ContratoFormRawValue['trabajo']>;
  departamento: FormControl<ContratoFormRawValue['departamento']>;
  empleado: FormControl<ContratoFormRawValue['empleado']>;
};

export type ContratoFormGroup = FormGroup<ContratoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContratoFormService {
  createContratoFormGroup(contrato: ContratoFormGroupInput = { id: null }): ContratoFormGroup {
    const contratoRawValue = this.convertContratoToContratoRawValue({
      ...this.getFormDefaults(),
      ...contrato,
    });
    return new FormGroup<ContratoFormGroupContent>({
      id: new FormControl(
        { value: contratoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fechaInicio: new FormControl(contratoRawValue.fechaInicio),
      fechaFin: new FormControl(contratoRawValue.fechaFin),
      lenguaje: new FormControl(contratoRawValue.lenguaje),
      trabajo: new FormControl(contratoRawValue.trabajo),
      departamento: new FormControl(contratoRawValue.departamento),
      empleado: new FormControl(contratoRawValue.empleado),
    });
  }

  getContrato(form: ContratoFormGroup): IContrato | NewContrato {
    return this.convertContratoRawValueToContrato(form.getRawValue() as ContratoFormRawValue | NewContratoFormRawValue);
  }

  resetForm(form: ContratoFormGroup, contrato: ContratoFormGroupInput): void {
    const contratoRawValue = this.convertContratoToContratoRawValue({ ...this.getFormDefaults(), ...contrato });
    form.reset(
      {
        ...contratoRawValue,
        id: { value: contratoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ContratoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaInicio: currentTime,
      fechaFin: currentTime,
    };
  }

  private convertContratoRawValueToContrato(rawContrato: ContratoFormRawValue | NewContratoFormRawValue): IContrato | NewContrato {
    return {
      ...rawContrato,
      fechaInicio: dayjs(rawContrato.fechaInicio, DATE_TIME_FORMAT),
      fechaFin: dayjs(rawContrato.fechaFin, DATE_TIME_FORMAT),
    };
  }

  private convertContratoToContratoRawValue(
    contrato: IContrato | (Partial<NewContrato> & ContratoFormDefaults)
  ): ContratoFormRawValue | PartialWithRequiredKeyOf<NewContratoFormRawValue> {
    return {
      ...contrato,
      fechaInicio: contrato.fechaInicio ? contrato.fechaInicio.format(DATE_TIME_FORMAT) : undefined,
      fechaFin: contrato.fechaFin ? contrato.fechaFin.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
