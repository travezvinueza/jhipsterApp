import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TrabajoFormService } from './trabajo-form.service';
import { TrabajoService } from '../service/trabajo.service';
import { ITrabajo } from '../trabajo.model';
import { ITarea } from 'app/entities/tarea/tarea.model';
import { TareaService } from 'app/entities/tarea/service/tarea.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';

import { TrabajoUpdateComponent } from './trabajo-update.component';

describe('Trabajo Management Update Component', () => {
  let comp: TrabajoUpdateComponent;
  let fixture: ComponentFixture<TrabajoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trabajoFormService: TrabajoFormService;
  let trabajoService: TrabajoService;
  let tareaService: TareaService;
  let empleadoService: EmpleadoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TrabajoUpdateComponent],
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
      .overrideTemplate(TrabajoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrabajoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trabajoFormService = TestBed.inject(TrabajoFormService);
    trabajoService = TestBed.inject(TrabajoService);
    tareaService = TestBed.inject(TareaService);
    empleadoService = TestBed.inject(EmpleadoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tarea query and add missing value', () => {
      const trabajo: ITrabajo = { id: 456 };
      const tareas: ITarea[] = [{ id: 19922 }];
      trabajo.tareas = tareas;

      const tareaCollection: ITarea[] = [{ id: 9000 }];
      jest.spyOn(tareaService, 'query').mockReturnValue(of(new HttpResponse({ body: tareaCollection })));
      const additionalTareas = [...tareas];
      const expectedCollection: ITarea[] = [...additionalTareas, ...tareaCollection];
      jest.spyOn(tareaService, 'addTareaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trabajo });
      comp.ngOnInit();

      expect(tareaService.query).toHaveBeenCalled();
      expect(tareaService.addTareaToCollectionIfMissing).toHaveBeenCalledWith(
        tareaCollection,
        ...additionalTareas.map(expect.objectContaining)
      );
      expect(comp.tareasSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Empleado query and add missing value', () => {
      const trabajo: ITrabajo = { id: 456 };
      const empleado: IEmpleado = { id: 43809 };
      trabajo.empleado = empleado;

      const empleadoCollection: IEmpleado[] = [{ id: 65988 }];
      jest.spyOn(empleadoService, 'query').mockReturnValue(of(new HttpResponse({ body: empleadoCollection })));
      const additionalEmpleados = [empleado];
      const expectedCollection: IEmpleado[] = [...additionalEmpleados, ...empleadoCollection];
      jest.spyOn(empleadoService, 'addEmpleadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trabajo });
      comp.ngOnInit();

      expect(empleadoService.query).toHaveBeenCalled();
      expect(empleadoService.addEmpleadoToCollectionIfMissing).toHaveBeenCalledWith(
        empleadoCollection,
        ...additionalEmpleados.map(expect.objectContaining)
      );
      expect(comp.empleadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const trabajo: ITrabajo = { id: 456 };
      const tarea: ITarea = { id: 69586 };
      trabajo.tareas = [tarea];
      const empleado: IEmpleado = { id: 58420 };
      trabajo.empleado = empleado;

      activatedRoute.data = of({ trabajo });
      comp.ngOnInit();

      expect(comp.tareasSharedCollection).toContain(tarea);
      expect(comp.empleadosSharedCollection).toContain(empleado);
      expect(comp.trabajo).toEqual(trabajo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrabajo>>();
      const trabajo = { id: 123 };
      jest.spyOn(trabajoFormService, 'getTrabajo').mockReturnValue(trabajo);
      jest.spyOn(trabajoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trabajo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trabajo }));
      saveSubject.complete();

      // THEN
      expect(trabajoFormService.getTrabajo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(trabajoService.update).toHaveBeenCalledWith(expect.objectContaining(trabajo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrabajo>>();
      const trabajo = { id: 123 };
      jest.spyOn(trabajoFormService, 'getTrabajo').mockReturnValue({ id: null });
      jest.spyOn(trabajoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trabajo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trabajo }));
      saveSubject.complete();

      // THEN
      expect(trabajoFormService.getTrabajo).toHaveBeenCalled();
      expect(trabajoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrabajo>>();
      const trabajo = { id: 123 };
      jest.spyOn(trabajoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trabajo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trabajoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTarea', () => {
      it('Should forward to tareaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tareaService, 'compareTarea');
        comp.compareTarea(entity, entity2);
        expect(tareaService.compareTarea).toHaveBeenCalledWith(entity, entity2);
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
