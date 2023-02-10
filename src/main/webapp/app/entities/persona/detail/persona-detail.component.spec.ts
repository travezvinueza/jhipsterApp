import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonaDetailComponent } from './persona-detail.component';

describe('Persona Management Detail Component', () => {
  let comp: PersonaDetailComponent;
  let fixture: ComponentFixture<PersonaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PersonaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ persona: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PersonaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PersonaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load persona on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.persona).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
