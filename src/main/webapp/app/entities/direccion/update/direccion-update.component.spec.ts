import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DireccionFormService } from './direccion-form.service';
import { DireccionService } from '../service/direccion.service';
import { IDireccion } from '../direccion.model';
import { IPais } from 'app/entities/pais/pais.model';
import { PaisService } from 'app/entities/pais/service/pais.service';

import { DireccionUpdateComponent } from './direccion-update.component';

describe('Direccion Management Update Component', () => {
  let comp: DireccionUpdateComponent;
  let fixture: ComponentFixture<DireccionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let direccionFormService: DireccionFormService;
  let direccionService: DireccionService;
  let paisService: PaisService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DireccionUpdateComponent],
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
      .overrideTemplate(DireccionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DireccionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    direccionFormService = TestBed.inject(DireccionFormService);
    direccionService = TestBed.inject(DireccionService);
    paisService = TestBed.inject(PaisService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Pais query and add missing value', () => {
      const direccion: IDireccion = { id: 456 };
      const pais: IPais = { id: 50091 };
      direccion.pais = pais;

      const paisCollection: IPais[] = [{ id: 11888 }];
      jest.spyOn(paisService, 'query').mockReturnValue(of(new HttpResponse({ body: paisCollection })));
      const additionalPais = [pais];
      const expectedCollection: IPais[] = [...additionalPais, ...paisCollection];
      jest.spyOn(paisService, 'addPaisToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      expect(paisService.query).toHaveBeenCalled();
      expect(paisService.addPaisToCollectionIfMissing).toHaveBeenCalledWith(paisCollection, ...additionalPais.map(expect.objectContaining));
      expect(comp.paisSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const direccion: IDireccion = { id: 456 };
      const pais: IPais = { id: 68982 };
      direccion.pais = pais;

      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      expect(comp.paisSharedCollection).toContain(pais);
      expect(comp.direccion).toEqual(direccion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDireccion>>();
      const direccion = { id: 123 };
      jest.spyOn(direccionFormService, 'getDireccion').mockReturnValue(direccion);
      jest.spyOn(direccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: direccion }));
      saveSubject.complete();

      // THEN
      expect(direccionFormService.getDireccion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(direccionService.update).toHaveBeenCalledWith(expect.objectContaining(direccion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDireccion>>();
      const direccion = { id: 123 };
      jest.spyOn(direccionFormService, 'getDireccion').mockReturnValue({ id: null });
      jest.spyOn(direccionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direccion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: direccion }));
      saveSubject.complete();

      // THEN
      expect(direccionFormService.getDireccion).toHaveBeenCalled();
      expect(direccionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDireccion>>();
      const direccion = { id: 123 };
      jest.spyOn(direccionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ direccion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(direccionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePais', () => {
      it('Should forward to paisService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(paisService, 'comparePais');
        comp.comparePais(entity, entity2);
        expect(paisService.comparePais).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
