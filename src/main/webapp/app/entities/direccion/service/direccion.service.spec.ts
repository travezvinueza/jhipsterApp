import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDireccion } from '../direccion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../direccion.test-samples';

import { DireccionService } from './direccion.service';

const requireRestSample: IDireccion = {
  ...sampleWithRequiredData,
};

describe('Direccion Service', () => {
  let service: DireccionService;
  let httpMock: HttpTestingController;
  let expectedResult: IDireccion | IDireccion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DireccionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Direccion', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const direccion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(direccion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Direccion', () => {
      const direccion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(direccion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Direccion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Direccion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Direccion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDireccionToCollectionIfMissing', () => {
      it('should add a Direccion to an empty array', () => {
        const direccion: IDireccion = sampleWithRequiredData;
        expectedResult = service.addDireccionToCollectionIfMissing([], direccion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(direccion);
      });

      it('should not add a Direccion to an array that contains it', () => {
        const direccion: IDireccion = sampleWithRequiredData;
        const direccionCollection: IDireccion[] = [
          {
            ...direccion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDireccionToCollectionIfMissing(direccionCollection, direccion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Direccion to an array that doesn't contain it", () => {
        const direccion: IDireccion = sampleWithRequiredData;
        const direccionCollection: IDireccion[] = [sampleWithPartialData];
        expectedResult = service.addDireccionToCollectionIfMissing(direccionCollection, direccion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(direccion);
      });

      it('should add only unique Direccion to an array', () => {
        const direccionArray: IDireccion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const direccionCollection: IDireccion[] = [sampleWithRequiredData];
        expectedResult = service.addDireccionToCollectionIfMissing(direccionCollection, ...direccionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const direccion: IDireccion = sampleWithRequiredData;
        const direccion2: IDireccion = sampleWithPartialData;
        expectedResult = service.addDireccionToCollectionIfMissing([], direccion, direccion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(direccion);
        expect(expectedResult).toContain(direccion2);
      });

      it('should accept null and undefined values', () => {
        const direccion: IDireccion = sampleWithRequiredData;
        expectedResult = service.addDireccionToCollectionIfMissing([], null, direccion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(direccion);
      });

      it('should return initial array if no Direccion is added', () => {
        const direccionCollection: IDireccion[] = [sampleWithRequiredData];
        expectedResult = service.addDireccionToCollectionIfMissing(direccionCollection, undefined, null);
        expect(expectedResult).toEqual(direccionCollection);
      });
    });

    describe('compareDireccion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDireccion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDireccion(entity1, entity2);
        const compareResult2 = service.compareDireccion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDireccion(entity1, entity2);
        const compareResult2 = service.compareDireccion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDireccion(entity1, entity2);
        const compareResult2 = service.compareDireccion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
