import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PaisService } from '../service/pais.service';

import { PaisComponent } from './pais.component';

describe('Pais Management Component', () => {
  let comp: PaisComponent;
  let fixture: ComponentFixture<PaisComponent>;
  let service: PaisService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'pais', component: PaisComponent }]), HttpClientTestingModule],
      declarations: [PaisComponent],
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
      .overrideTemplate(PaisComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaisComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PaisService);

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
    expect(comp.pais?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to paisService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getPaisIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPaisIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
