import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ContratoFormService } from './contrato-form.service';
import { ContratoService } from '../service/contrato.service';
import { IContrato } from '../contrato.model';
import { ITrabajo } from 'app/entities/trabajo/trabajo.model';
import { TrabajoService } from 'app/entities/trabajo/service/trabajo.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';

import { ContratoUpdateComponent } from './contrato-update.component';

describe('Contrato Management Update Component', () => {
  let comp: ContratoUpdateComponent;
  let fixture: ComponentFixture<ContratoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contratoFormService: ContratoFormService;
  let contratoService: ContratoService;
  let trabajoService: TrabajoService;
  let departamentoService: DepartamentoService;
  let empleadoService: EmpleadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ContratoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ContratoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContratoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contratoFormService = TestBed.inject(ContratoFormService);
    contratoService = TestBed.inject(ContratoService);
    trabajoService = TestBed.inject(TrabajoService);
    departamentoService = TestBed.inject(DepartamentoService);
    empleadoService = TestBed.inject(EmpleadoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Trabajo query and add missing value', () => {
      const contrato: IContrato = { id: 456 };
      const trabajo: ITrabajo = { id: 66385 };
      contrato.trabajo = trabajo;

      const trabajoCollection: ITrabajo[] = [{ id: 43866 }];
      jest.spyOn(trabajoService, 'query').mockReturnValue(of(new HttpResponse({ body: trabajoCollection })));
      const additionalTrabajos = [trabajo];
      const expectedCollection: ITrabajo[] = [...additionalTrabajos, ...trabajoCollection];
      jest.spyOn(trabajoService, 'addTrabajoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contrato });
      comp.ngOnInit();

      expect(trabajoService.query).toHaveBeenCalled();
      expect(trabajoService.addTrabajoToCollectionIfMissing).toHaveBeenCalledWith(
        trabajoCollection,
        ...additionalTrabajos.map(expect.objectContaining)
      );
      expect(comp.trabajosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Departamento query and add missing value', () => {
      const contrato: IContrato = { id: 456 };
      const departamento: IDepartamento = { id: 36755 };
      contrato.departamento = departamento;

      const departamentoCollection: IDepartamento[] = [{ id: 58411 }];
      jest.spyOn(departamentoService, 'query').mockReturnValue(of(new HttpResponse({ body: departamentoCollection })));
      const additionalDepartamentos = [departamento];
      const expectedCollection: IDepartamento[] = [...additionalDepartamentos, ...departamentoCollection];
      jest.spyOn(departamentoService, 'addDepartamentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contrato });
      comp.ngOnInit();

      expect(departamentoService.query).toHaveBeenCalled();
      expect(departamentoService.addDepartamentoToCollectionIfMissing).toHaveBeenCalledWith(
        departamentoCollection,
        ...additionalDepartamentos.map(expect.objectContaining)
      );
      expect(comp.departamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Empleado query and add missing value', () => {
      const contrato: IContrato = { id: 456 };
      const empleado: IEmpleado = { id: 38987 };
      contrato.empleado = empleado;

      const empleadoCollection: IEmpleado[] = [{ id: 28986 }];
      jest.spyOn(empleadoService, 'query').mockReturnValue(of(new HttpResponse({ body: empleadoCollection })));
      const additionalEmpleados = [empleado];
      const expectedCollection: IEmpleado[] = [...additionalEmpleados, ...empleadoCollection];
      jest.spyOn(empleadoService, 'addEmpleadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contrato });
      comp.ngOnInit();

      expect(empleadoService.query).toHaveBeenCalled();
      expect(empleadoService.addEmpleadoToCollectionIfMissing).toHaveBeenCalledWith(
        empleadoCollection,
        ...additionalEmpleados.map(expect.objectContaining)
      );
      expect(comp.empleadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contrato: IContrato = { id: 456 };
      const trabajo: ITrabajo = { id: 56185 };
      contrato.trabajo = trabajo;
      const departamento: IDepartamento = { id: 51850 };
      contrato.departamento = departamento;
      const empleado: IEmpleado = { id: 77563 };
      contrato.empleado = empleado;

      activatedRoute.data = of({ contrato });
      comp.ngOnInit();

      expect(comp.trabajosSharedCollection).toContain(trabajo);
      expect(comp.departamentosSharedCollection).toContain(departamento);
      expect(comp.empleadosSharedCollection).toContain(empleado);
      expect(comp.contrato).toEqual(contrato);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContrato>>();
      const contrato = { id: 123 };
      jest.spyOn(contratoFormService, 'getContrato').mockReturnValue(contrato);
      jest.spyOn(contratoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrato });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contrato }));
      saveSubject.complete();

      // THEN
      expect(contratoFormService.getContrato).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contratoService.update).toHaveBeenCalledWith(expect.objectContaining(contrato));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContrato>>();
      const contrato = { id: 123 };
      jest.spyOn(contratoFormService, 'getContrato').mockReturnValue({ id: null });
      jest.spyOn(contratoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrato: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contrato }));
      saveSubject.complete();

      // THEN
      expect(contratoFormService.getContrato).toHaveBeenCalled();
      expect(contratoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContrato>>();
      const contrato = { id: 123 };
      jest.spyOn(contratoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrato });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contratoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTrabajo', () => {
      it('Should forward to trabajoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(trabajoService, 'compareTrabajo');
        comp.compareTrabajo(entity, entity2);
        expect(trabajoService.compareTrabajo).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepartamento', () => {
      it('Should forward to departamentoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departamentoService, 'compareDepartamento');
        comp.compareDepartamento(entity, entity2);
        expect(departamentoService.compareDepartamento).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmpleado', () => {
      it('Should forward to empleadoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(empleadoService, 'compareEmpleado');
        comp.compareEmpleado(entity, entity2);
        expect(empleadoService.compareEmpleado).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
