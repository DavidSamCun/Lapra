import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LaptopService } from '../service/laptop.service';
import { ILaptop, Laptop } from '../laptop.model';

import { LaptopUpdateComponent } from './laptop-update.component';

describe('Laptop Management Update Component', () => {
  let comp: LaptopUpdateComponent;
  let fixture: ComponentFixture<LaptopUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let laptopService: LaptopService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LaptopUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LaptopUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LaptopUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    laptopService = TestBed.inject(LaptopService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const laptop: ILaptop = { id: 456 };

      activatedRoute.data = of({ laptop });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(laptop));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Laptop>>();
      const laptop = { id: 123 };
      jest.spyOn(laptopService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ laptop });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: laptop }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(laptopService.update).toHaveBeenCalledWith(laptop);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Laptop>>();
      const laptop = new Laptop();
      jest.spyOn(laptopService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ laptop });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: laptop }));
      saveSubject.complete();

      // THEN
      expect(laptopService.create).toHaveBeenCalledWith(laptop);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Laptop>>();
      const laptop = { id: 123 };
      jest.spyOn(laptopService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ laptop });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(laptopService.update).toHaveBeenCalledWith(laptop);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
