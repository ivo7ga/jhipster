import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IExperiencia, Experiencia } from '../experiencia.model';

import { ExperienciaService } from './experiencia.service';

describe('Experiencia Service', () => {
  let service: ExperienciaService;
  let httpMock: HttpTestingController;
  let elemDefault: IExperiencia;
  let expectedResult: IExperiencia | IExperiencia[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExperienciaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      titulo: 'AAAAAAA',
      descripcion: 'AAAAAAA',
      localizacion: 'AAAAAAA',
      fecha: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fecha: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Experiencia', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fecha: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.create(new Experiencia()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Experiencia', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          titulo: 'BBBBBB',
          descripcion: 'BBBBBB',
          localizacion: 'BBBBBB',
          fecha: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Experiencia', () => {
      const patchObject = Object.assign(
        {
          fecha: currentDate.format(DATE_FORMAT),
        },
        new Experiencia()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Experiencia', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          titulo: 'BBBBBB',
          descripcion: 'BBBBBB',
          localizacion: 'BBBBBB',
          fecha: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Experiencia', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addExperienciaToCollectionIfMissing', () => {
      it('should add a Experiencia to an empty array', () => {
        const experiencia: IExperiencia = { id: 123 };
        expectedResult = service.addExperienciaToCollectionIfMissing([], experiencia);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(experiencia);
      });

      it('should not add a Experiencia to an array that contains it', () => {
        const experiencia: IExperiencia = { id: 123 };
        const experienciaCollection: IExperiencia[] = [
          {
            ...experiencia,
          },
          { id: 456 },
        ];
        expectedResult = service.addExperienciaToCollectionIfMissing(experienciaCollection, experiencia);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Experiencia to an array that doesn't contain it", () => {
        const experiencia: IExperiencia = { id: 123 };
        const experienciaCollection: IExperiencia[] = [{ id: 456 }];
        expectedResult = service.addExperienciaToCollectionIfMissing(experienciaCollection, experiencia);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(experiencia);
      });

      it('should add only unique Experiencia to an array', () => {
        const experienciaArray: IExperiencia[] = [{ id: 123 }, { id: 456 }, { id: 1101 }];
        const experienciaCollection: IExperiencia[] = [{ id: 123 }];
        expectedResult = service.addExperienciaToCollectionIfMissing(experienciaCollection, ...experienciaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const experiencia: IExperiencia = { id: 123 };
        const experiencia2: IExperiencia = { id: 456 };
        expectedResult = service.addExperienciaToCollectionIfMissing([], experiencia, experiencia2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(experiencia);
        expect(expectedResult).toContain(experiencia2);
      });

      it('should accept null and undefined values', () => {
        const experiencia: IExperiencia = { id: 123 };
        expectedResult = service.addExperienciaToCollectionIfMissing([], null, experiencia, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(experiencia);
      });

      it('should return initial array if no Experiencia is added', () => {
        const experienciaCollection: IExperiencia[] = [{ id: 123 }];
        expectedResult = service.addExperienciaToCollectionIfMissing(experienciaCollection, undefined, null);
        expect(expectedResult).toEqual(experienciaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
