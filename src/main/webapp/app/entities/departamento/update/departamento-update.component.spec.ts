import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DepartamentoFormService } from './departamento-form.service';
import { DepartamentoService } from '../service/departamento.service';
import { IDepartamento } from '../departamento.model';
import { IDireccion } from 'app/entities/direccion/direccion.model';
import { DireccionService } from 'app/entities/direccion/service/direccion.service';

import { DepartamentoUpdateComponent } from './departamento-update.component';

describe('Departamento Management Update Component', () => {
  let comp: DepartamentoUpdateComponent;
  let fixture: ComponentFixture<DepartamentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let departamentoFormService: DepartamentoFormService;
  let departamentoService: DepartamentoService;
  let direccionService: DireccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DepartamentoUpdateComponent],
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
      .overrideTemplate(DepartamentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepartamentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departamentoFormService = TestBed.inject(DepartamentoFormService);
    departamentoService = TestBed.inject(DepartamentoService);
    direccionService = TestBed.inject(DireccionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Direccion query and add missing value', () => {
      const departamento: IDepartamento = { id: 456 };
      const direccion: IDireccion = { id: 72042 };
      departamento.direccion = direccion;

      const direccionCollection: IDireccion[] = [{ id: 52050 }];
      jest.spyOn(direccionService, 'query').mockReturnValue(of(new HttpResponse({ body: direccionCollection })));
      const additionalDireccions = [direccion];
      const expectedCollection: IDireccion[] = [...additionalDireccions, ...direccionCollection];
      jest.spyOn(direccionService, 'addDireccionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ departamento });
      comp.ngOnInit();

      expect(direccionService.query).toHaveBeenCalled();
      expect(direccionService.addDireccionToCollectionIfMissing).toHaveBeenCalledWith(
        direccionCollection,
        ...additionalDireccions.map(expect.objectContaining)
      );
      expect(comp.direccionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const departamento: IDepartamento = { id: 456 };
      const direccion: IDireccion = { id: 16860 };
      departamento.direccion = direccion;

      activatedRoute.data = of({ departamento });
      comp.ngOnInit();

      expect(comp.direccionsSharedCollection).toContain(direccion);
      expect(comp.departamento).toEqual(departamento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartamento>>();
      const departamento = { id: 123 };
      jest.spyOn(departamentoFormService, 'getDepartamento').mockReturnValue(departamento);
      jest.spyOn(departamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departamento }));
      saveSubject.complete();

      // THEN
      expect(departamentoFormService.getDepartamento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(departamentoService.update).toHaveBeenCalledWith(expect.objectContaining(departamento));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartamento>>();
      const departamento = { id: 123 };
      jest.spyOn(departamentoFormService, 'getDepartamento').mockReturnValue({ id: null });
      jest.spyOn(departamentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departamento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: departamento }));
      saveSubject.complete();

      // THEN
      expect(departamentoFormService.getDepartamento).toHaveBeenCalled();
      expect(departamentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDepartamento>>();
      const departamento = { id: 123 };
      jest.spyOn(departamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ departamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departamentoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDireccion', () => {
      it('Should forward to direccionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(direccionService, 'compareDireccion');
        comp.compareDireccion(entity, entity2);
        expect(direccionService.compareDireccion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
