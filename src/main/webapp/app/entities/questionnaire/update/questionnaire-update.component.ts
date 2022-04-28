import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestionnaire, Questionnaire } from '../questionnaire.model';
import { QuestionnaireService } from '../service/questionnaire.service';
import { ILaptop } from 'app/entities/laptop/laptop.model';
import { LaptopService } from 'app/entities/laptop/service/laptop.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-questionnaire-update',
  templateUrl: './questionnaire-update.component.html',
})
export class QuestionnaireUpdateComponent implements OnInit {
  isSaving = false;

  laptopsSharedCollection: ILaptop[] = [];
  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    answer1UsedAt: [],
    answer2Type: [],
    answer3Size: [],
    answer4Function: [],
    answer5Price: [],
    model: [],
    assignedTo: [],
  });

answer1Select:any
  answer1Options = [

    {
      answer: "Business"
    },
    {
      answer: "Student"
    },
    {
      answer: "Personal"
    },
    {
      answer: "I got Money, Gimme dat Mac"
    }
  ]

  answer2Select:any
  answer2Options = [
    {
      answer: "Desktop Replacement (Plugged in Mostly)"
    },
    {
      answer: "2 in 1 Tablet/Laptop"
    },
    {
      answer: "Laptop with Good Battery Life"
    }

  ]

  answer3Select:any
  answer3Options = [

    {
      answer: "14\" or smaller",
      size: 14
    },
    {
      answer: "15.6\"",
      size: 15.6
    },
    {
      answer: "16\"+",
      size: 16
    },
    {
      answer: "No Preference",
      size: 20
    }

  ]

  answer4Select:any
  answer4Options = [

    {
      answer: "Web Surfing",
    },
    {
      answer: "Video Streaming",
    },
    {
      answer: "Document Editing",
    },
    {
      answer: "Web Development",
    },
    {
      answer: "App Development",
    },
    {
      answer: "iOS/MacOS Development",
    },
    {
      answer: "Photo Editing",
    },
    {
      answer: "Video Editing",
    },
    {
      answer: "Virtual Machines",
    },
    {
      answer: "Casual Gaming",
    },
    {
      answer: "Heavy Gaming",
    },
    {
      answer: "Virtual Reality (VR)",
    }

  ]

  constructor(
    protected questionnaireService: QuestionnaireService,
    protected laptopService: LaptopService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionnaire }) => {
      this.updateForm(questionnaire);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionnaire = this.createFromForm();
    if (questionnaire.id !== undefined) {
      this.subscribeToSaveResponse(this.questionnaireService.update(questionnaire));
    } else {
      this.subscribeToSaveResponse(this.questionnaireService.create(questionnaire));
    }
  }

  trackLaptopById(_index: number, item: ILaptop): number {
    return item.id!;
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionnaire>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(questionnaire: IQuestionnaire): void {
    this.editForm.patchValue({
      id: questionnaire.id,
      answer1UsedAt: questionnaire.answer1UsedAt,
      answer2Type: questionnaire.answer2Type,
      answer3Size: questionnaire.answer3Size,
      answer4Function: questionnaire.answer4Function,
      answer5Price: questionnaire.answer5Price,
      model: questionnaire.model,
      assignedTo: questionnaire.assignedTo,
    });

    this.laptopsSharedCollection = this.laptopService.addLaptopToCollectionIfMissing(this.laptopsSharedCollection, questionnaire.model);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, questionnaire.assignedTo);
  }

  protected loadRelationshipsOptions(): void {
    this.laptopService
      .query()
      .pipe(map((res: HttpResponse<ILaptop[]>) => res.body ?? []))
      .pipe(map((laptops: ILaptop[]) => this.laptopService.addLaptopToCollectionIfMissing(laptops, this.editForm.get('model')!.value)))
      .subscribe((laptops: ILaptop[]) => (this.laptopsSharedCollection = laptops));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('assignedTo')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IQuestionnaire {
    return {
      ...new Questionnaire(),
      id: this.editForm.get(['id'])!.value,
      answer1UsedAt: this.editForm.get(['answer1UsedAt'])!.value,
      answer2Type: this.editForm.get(['answer2Type'])!.value,
      answer3Size: this.editForm.get(['answer3Size'])!.value,
      answer4Function: this.editForm.get(['answer4Function'])!.value,
      answer5Price: this.editForm.get(['answer5Price'])!.value,
      model: this.editForm.get(['model'])!.value,
      assignedTo: this.editForm.get(['assignedTo'])!.value,
    };
  }
}
