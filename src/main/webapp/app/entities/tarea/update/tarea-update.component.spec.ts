import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TareaFormService } from './tarea-form.service';
import { TareaService } from '../service/tarea.service';
import { ITarea } from '../tarea.model';

import { TareaUpdateComponent } from './tarea-update.component';

describe('Tarea Management Update Component', () => {
  let comp: TareaUpdateComponent;
  let fixture: ComponentFixture<TareaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tareaFormService: TareaFormService;
  let tareaService: TareaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TareaUpdateComponent],
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
      .overrideTemplate(TareaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TareaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tareaFormService = TestBed.inject(TareaFormService);
    tareaService = TestBed.inject(TareaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tarea: ITarea = { id: 456 };

      activatedRoute.data = of({ tarea });
      comp.ngOnInit();

      expect(comp.tarea).toEqual(tarea);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITarea>>();
      const tarea = { id: 123 };
      jest.spyOn(tareaFormService, 'getTarea').mockReturnValue(tarea);
      jest.spyOn(tareaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tarea });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tarea }));
      saveSubject.complete();

      // THEN
      expect(tareaFormService.getTarea).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tareaService.update).toHaveBeenCalledWith(expect.objectContaining(tarea));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITarea>>();
      const tarea = { id: 123 };
      jest.spyOn(tareaFormService, 'getTarea').mockReturnValue({ id: null });
      jest.spyOn(tareaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tarea: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tarea }));
      saveSubject.complete();

      // THEN
      expect(tareaFormService.getTarea).toHaveBeenCalled();
      expect(tareaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITarea>>();
      const tarea = { id: 123 };
      jest.spyOn(tareaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tarea });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tareaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
