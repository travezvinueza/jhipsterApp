import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../trabajo.test-samples';

import { TrabajoFormService } from './trabajo-form.service';

describe('Trabajo Form Service', () => {
  let service: TrabajoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TrabajoFormService);
  });

  describe('Service methods', () => {
    describe('createTrabajoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTrabajoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tituloTrabajo: expect.any(Object),
            salarioMin: expect.any(Object),
            salarioMax: expect.any(Object),
            tareas: expect.any(Object),
            empleado: expect.any(Object),
          })
        );
      });

      it('passing ITrabajo should create a new form with FormGroup', () => {
        const formGroup = service.createTrabajoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tituloTrabajo: expect.any(Object),
            salarioMin: expect.any(Object),
            salarioMax: expect.any(Object),
            tareas: expect.any(Object),
            empleado: expect.any(Object),
          })
        );
      });
    });

    describe('getTrabajo', () => {
      it('should return NewTrabajo for default Trabajo initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTrabajoFormGroup(sampleWithNewData);

        const trabajo = service.getTrabajo(formGroup) as any;

        expect(trabajo).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrabajo for empty Trabajo initial value', () => {
        const formGroup = service.createTrabajoFormGroup();

        const trabajo = service.getTrabajo(formGroup) as any;

        expect(trabajo).toMatchObject({});
      });

      it('should return ITrabajo', () => {
        const formGroup = service.createTrabajoFormGroup(sampleWithRequiredData);

        const trabajo = service.getTrabajo(formGroup) as any;

        expect(trabajo).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrabajo should not enable id FormControl', () => {
        const formGroup = service.createTrabajoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrabajo should disable id FormControl', () => {
        const formGroup = service.createTrabajoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
