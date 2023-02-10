import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITrabajo } from '../trabajo.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../trabajo.test-samples';

import { TrabajoService } from './trabajo.service';

const requireRestSample: ITrabajo = {
  ...sampleWithRequiredData,
};

describe('Trabajo Service', () => {
  let service: TrabajoService;
  let httpMock: HttpTestingController;
  let expectedResult: ITrabajo | ITrabajo[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrabajoService);
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

    it('should create a Trabajo', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const trabajo = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(trabajo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Trabajo', () => {
      const trabajo = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(trabajo).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Trabajo', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Trabajo', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Trabajo', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTrabajoToCollectionIfMissing', () => {
      it('should add a Trabajo to an empty array', () => {
        const trabajo: ITrabajo = sampleWithRequiredData;
        expectedResult = service.addTrabajoToCollectionIfMissing([], trabajo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trabajo);
      });

      it('should not add a Trabajo to an array that contains it', () => {
        const trabajo: ITrabajo = sampleWithRequiredData;
        const trabajoCollection: ITrabajo[] = [
          {
            ...trabajo,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTrabajoToCollectionIfMissing(trabajoCollection, trabajo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Trabajo to an array that doesn't contain it", () => {
        const trabajo: ITrabajo = sampleWithRequiredData;
        const trabajoCollection: ITrabajo[] = [sampleWithPartialData];
        expectedResult = service.addTrabajoToCollectionIfMissing(trabajoCollection, trabajo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trabajo);
      });

      it('should add only unique Trabajo to an array', () => {
        const trabajoArray: ITrabajo[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const trabajoCollection: ITrabajo[] = [sampleWithRequiredData];
        expectedResult = service.addTrabajoToCollectionIfMissing(trabajoCollection, ...trabajoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trabajo: ITrabajo = sampleWithRequiredData;
        const trabajo2: ITrabajo = sampleWithPartialData;
        expectedResult = service.addTrabajoToCollectionIfMissing([], trabajo, trabajo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trabajo);
        expect(expectedResult).toContain(trabajo2);
      });

      it('should accept null and undefined values', () => {
        const trabajo: ITrabajo = sampleWithRequiredData;
        expectedResult = service.addTrabajoToCollectionIfMissing([], null, trabajo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trabajo);
      });

      it('should return initial array if no Trabajo is added', () => {
        const trabajoCollection: ITrabajo[] = [sampleWithRequiredData];
        expectedResult = service.addTrabajoToCollectionIfMissing(trabajoCollection, undefined, null);
        expect(expectedResult).toEqual(trabajoCollection);
      });
    });

    describe('compareTrabajo', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTrabajo(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTrabajo(entity1, entity2);
        const compareResult2 = service.compareTrabajo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTrabajo(entity1, entity2);
        const compareResult2 = service.compareTrabajo(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTrabajo(entity1, entity2);
        const compareResult2 = service.compareTrabajo(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
