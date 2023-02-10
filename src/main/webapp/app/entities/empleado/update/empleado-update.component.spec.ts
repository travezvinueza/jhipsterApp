import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmpleadoFormService } from './empleado-form.service';
import { EmpleadoService } from '../service/empleado.service';
import { IEmpleado } from '../empleado.model';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';

import { EmpleadoUpdateComponent } from './empleado-update.component';

describe('Empleado Management Update Component', () => {
  let comp: EmpleadoUpdateComponent;
  let fixture: ComponentFixture<EmpleadoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let empleadoFormService: EmpleadoFormService;
  let empleadoService: EmpleadoService;
  let departamentoService: DepartamentoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmpleadoUpdateComponent],
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
      .overrideTemplate(EmpleadoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmpleadoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    empleadoFormService = TestBed.inject(EmpleadoFormService);
    empleadoService = TestBed.inject(EmpleadoService);
    departamentoService = TestBed.inject(DepartamentoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Empleado query and add missing value', () => {
      const empleado: IEmpleado = { id: 456 };
      const inmediatosuperior: IEmpleado = { id: 6513 };
      empleado.inmediatosuperior = inmediatosuperior;

      const empleadoCollection: IEmpleado[] = [{ id: 36440 }];
      jest.spyOn(empleadoService, 'query').mockReturnValue(of(new HttpResponse({ body: empleadoCollection })));
      const additionalEmpleados = [inmediatosuperior];
      const expectedCollection: IEmpleado[] = [...additionalEmpleados, ...empleadoCollection];
      jest.spyOn(empleadoService, 'addEmpleadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(empleadoService.query).toHaveBeenCalled();
      expect(empleadoService.addEmpleadoToCollectionIfMissing).toHaveBeenCalledWith(
        empleadoCollection,
        ...additionalEmpleados.map(expect.objectContaining)
      );
      expect(comp.empleadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Departamento query and add missing value', () => {
      const empleado: IEmpleado = { id: 456 };
      const departamento: IDepartamento = { id: 65206 };
      empleado.departamento = departamento;

      const departamentoCollection: IDepartamento[] = [{ id: 21511 }];
      jest.spyOn(departamentoService, 'query').mockReturnValue(of(new HttpResponse({ body: departamentoCollection })));
      const additionalDepartamentos = [departamento];
      const expectedCollection: IDepartamento[] = [...additionalDepartamentos, ...departamentoCollection];
      jest.spyOn(departamentoService, 'addDepartamentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(departamentoService.query).toHaveBeenCalled();
      expect(departamentoService.addDepartamentoToCollectionIfMissing).toHaveBeenCalledWith(
        departamentoCollection,
        ...additionalDepartamentos.map(expect.objectContaining)
      );
      expect(comp.departamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const empleado: IEmpleado = { id: 456 };
      const inmediatosuperior: IEmpleado = { id: 75490 };
      empleado.inmediatosuperior = inmediatosuperior;
      const departamento: IDepartamento = { id: 6332 };
      empleado.departamento = departamento;

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(comp.empleadosSharedCollection).toContain(inmediatosuperior);
      expect(comp.departamentosSharedCollection).toContain(departamento);
      expect(comp.empleado).toEqual(empleado);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleado>>();
      const empleado = { id: 123 };
      jest.spyOn(empleadoFormService, 'getEmpleado').mockReturnValue(empleado);
      jest.spyOn(empleadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleado }));
      saveSubject.complete();

      // THEN
      expect(empleadoFormService.getEmpleado).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(empleadoService.update).toHaveBeenCalledWith(expect.objectContaining(empleado));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleado>>();
      const empleado = { id: 123 };
      jest.spyOn(empleadoFormService, 'getEmpleado').mockReturnValue({ id: null });
      jest.spyOn(empleadoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleado }));
      saveSubject.complete();

      // THEN
      expect(empleadoFormService.getEmpleado).toHaveBeenCalled();
      expect(empleadoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleado>>();
      const empleado = { id: 123 };
      jest.spyOn(empleadoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(empleadoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmpleado', () => {
      it('Should forward to empleadoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(empleadoService, 'compareEmpleado');
        comp.compareEmpleado(entity, entity2);
        expect(empleadoService.compareEmpleado).toHaveBeenCalledWith(entity, entity2);
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
  });
});
