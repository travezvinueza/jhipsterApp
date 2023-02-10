import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DireccionDetailComponent } from './direccion-detail.component';

describe('Direccion Management Detail Component', () => {
  let comp: DireccionDetailComponent;
  let fixture: ComponentFixture<DireccionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DireccionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ direccion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DireccionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DireccionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load direccion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.direccion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
