import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DireccionService } from '../service/direccion.service';

import { DireccionComponent } from './direccion.component';

describe('Direccion Management Component', () => {
  let comp: DireccionComponent;
  let fixture: ComponentFixture<DireccionComponent>;
  let service: DireccionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'direccion', component: DireccionComponent }]), HttpClientTestingModule],
      declarations: [DireccionComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(DireccionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DireccionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DireccionService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.direccions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to direccionService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getDireccionIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getDireccionIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
