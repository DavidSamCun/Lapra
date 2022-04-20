import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILaptop, Laptop } from '../laptop.model';

import { LaptopService } from './laptop.service';

describe('Laptop Service', () => {
  let service: LaptopService;
  let httpMock: HttpTestingController;
  let elemDefault: ILaptop;
  let expectedResult: ILaptop | ILaptop[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LaptopService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      brandName: 'AAAAAAA',
      modelName: 'AAAAAAA',
      screenSize: 0,
      memory: 0,
      storage: 0,
      screenBrightness: 0,
      screenRefreshHz: 0,
      price: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Laptop', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Laptop()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Laptop', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          brandName: 'BBBBBB',
          modelName: 'BBBBBB',
          screenSize: 1,
          memory: 1,
          storage: 1,
          screenBrightness: 1,
          screenRefreshHz: 1,
          price: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Laptop', () => {
      const patchObject = Object.assign(
        {
          brandName: 'BBBBBB',
          modelName: 'BBBBBB',
          price: 1,
        },
        new Laptop()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Laptop', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          brandName: 'BBBBBB',
          modelName: 'BBBBBB',
          screenSize: 1,
          memory: 1,
          storage: 1,
          screenBrightness: 1,
          screenRefreshHz: 1,
          price: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Laptop', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLaptopToCollectionIfMissing', () => {
      it('should add a Laptop to an empty array', () => {
        const laptop: ILaptop = { id: 123 };
        expectedResult = service.addLaptopToCollectionIfMissing([], laptop);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(laptop);
      });

      it('should not add a Laptop to an array that contains it', () => {
        const laptop: ILaptop = { id: 123 };
        const laptopCollection: ILaptop[] = [
          {
            ...laptop,
          },
          { id: 456 },
        ];
        expectedResult = service.addLaptopToCollectionIfMissing(laptopCollection, laptop);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Laptop to an array that doesn't contain it", () => {
        const laptop: ILaptop = { id: 123 };
        const laptopCollection: ILaptop[] = [{ id: 456 }];
        expectedResult = service.addLaptopToCollectionIfMissing(laptopCollection, laptop);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(laptop);
      });

      it('should add only unique Laptop to an array', () => {
        const laptopArray: ILaptop[] = [{ id: 123 }, { id: 456 }, { id: 34225 }];
        const laptopCollection: ILaptop[] = [{ id: 123 }];
        expectedResult = service.addLaptopToCollectionIfMissing(laptopCollection, ...laptopArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const laptop: ILaptop = { id: 123 };
        const laptop2: ILaptop = { id: 456 };
        expectedResult = service.addLaptopToCollectionIfMissing([], laptop, laptop2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(laptop);
        expect(expectedResult).toContain(laptop2);
      });

      it('should accept null and undefined values', () => {
        const laptop: ILaptop = { id: 123 };
        expectedResult = service.addLaptopToCollectionIfMissing([], null, laptop, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(laptop);
      });

      it('should return initial array if no Laptop is added', () => {
        const laptopCollection: ILaptop[] = [{ id: 123 }];
        expectedResult = service.addLaptopToCollectionIfMissing(laptopCollection, undefined, null);
        expect(expectedResult).toEqual(laptopCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
