import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LaptopDetailComponent } from './laptop-detail.component';

describe('Laptop Management Detail Component', () => {
  let comp: LaptopDetailComponent;
  let fixture: ComponentFixture<LaptopDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LaptopDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ laptop: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LaptopDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LaptopDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load laptop on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.laptop).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
