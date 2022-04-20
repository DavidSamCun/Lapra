import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QuestionnaireService } from '../service/questionnaire.service';
import { IQuestionnaire, Questionnaire } from '../questionnaire.model';
import { ILaptop } from 'app/entities/laptop/laptop.model';
import { LaptopService } from 'app/entities/laptop/service/laptop.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import { QuestionnaireUpdateComponent } from './questionnaire-update.component';

describe('Questionnaire Management Update Component', () => {
  let comp: QuestionnaireUpdateComponent;
  let fixture: ComponentFixture<QuestionnaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let questionnaireService: QuestionnaireService;
  let laptopService: LaptopService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [QuestionnaireUpdateComponent],
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
      .overrideTemplate(QuestionnaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QuestionnaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    questionnaireService = TestBed.inject(QuestionnaireService);
    laptopService = TestBed.inject(LaptopService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Laptop query and add missing value', () => {
      const questionnaire: IQuestionnaire = { id: 456 };
      const model: ILaptop = { id: 58090 };
      questionnaire.model = model;

      const laptopCollection: ILaptop[] = [{ id: 42568 }];
      jest.spyOn(laptopService, 'query').mockReturnValue(of(new HttpResponse({ body: laptopCollection })));
      const additionalLaptops = [model];
      const expectedCollection: ILaptop[] = [...additionalLaptops, ...laptopCollection];
      jest.spyOn(laptopService, 'addLaptopToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionnaire });
      comp.ngOnInit();

      expect(laptopService.query).toHaveBeenCalled();
      expect(laptopService.addLaptopToCollectionIfMissing).toHaveBeenCalledWith(laptopCollection, ...additionalLaptops);
      expect(comp.laptopsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const questionnaire: IQuestionnaire = { id: 456 };
      const assignedTo: IUser = { id: 81310 };
      questionnaire.assignedTo = assignedTo;

      const userCollection: IUser[] = [{ id: 9797 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [assignedTo];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ questionnaire });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const questionnaire: IQuestionnaire = { id: 456 };
      const model: ILaptop = { id: 78248 };
      questionnaire.model = model;
      const assignedTo: IUser = { id: 75317 };
      questionnaire.assignedTo = assignedTo;

      activatedRoute.data = of({ questionnaire });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(questionnaire));
      expect(comp.laptopsSharedCollection).toContain(model);
      expect(comp.usersSharedCollection).toContain(assignedTo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Questionnaire>>();
      const questionnaire = { id: 123 };
      jest.spyOn(questionnaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionnaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionnaire }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(questionnaireService.update).toHaveBeenCalledWith(questionnaire);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Questionnaire>>();
      const questionnaire = new Questionnaire();
      jest.spyOn(questionnaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionnaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: questionnaire }));
      saveSubject.complete();

      // THEN
      expect(questionnaireService.create).toHaveBeenCalledWith(questionnaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Questionnaire>>();
      const questionnaire = { id: 123 };
      jest.spyOn(questionnaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ questionnaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(questionnaireService.update).toHaveBeenCalledWith(questionnaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLaptopById', () => {
      it('Should return tracked Laptop primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLaptopById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
