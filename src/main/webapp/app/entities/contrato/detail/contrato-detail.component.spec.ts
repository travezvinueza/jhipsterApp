import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContratoDetailComponent } from './contrato-detail.component';

describe('Contrato Management Detail Component', () => {
  let comp: ContratoDetailComponent;
  let fixture: ComponentFixture<ContratoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ContratoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ contrato: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ContratoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ContratoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load contrato on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.contrato).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
