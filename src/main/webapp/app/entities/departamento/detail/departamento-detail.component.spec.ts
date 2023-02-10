import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DepartamentoDetailComponent } from './departamento-detail.component';

describe('Departamento Management Detail Component', () => {
  let comp: DepartamentoDetailComponent;
  let fixture: ComponentFixture<DepartamentoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepartamentoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ departamento: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DepartamentoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DepartamentoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load departamento on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.departamento).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
