import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILaptop, Laptop } from '../laptop.model';
import { LaptopService } from '../service/laptop.service';

@Component({
  selector: 'jhi-laptop-update',
  templateUrl: './laptop-update.component.html',
})
export class LaptopUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    brandName: [null, [Validators.required, Validators.maxLength(30)]],
    modelName: [null, [Validators.required, Validators.maxLength(30)]],
    screenSize: [],
    memory: [],
    storage: [],
    screenBrightness: [],
    screenRefreshHz: [],
    price: [],
  });

  constructor(protected laptopService: LaptopService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ laptop }) => {
      this.updateForm(laptop);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const laptop = this.createFromForm();
    if (laptop.id !== undefined) {
      this.subscribeToSaveResponse(this.laptopService.update(laptop));
    } else {
      this.subscribeToSaveResponse(this.laptopService.create(laptop));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILaptop>>): void {
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

  protected updateForm(laptop: ILaptop): void {
    this.editForm.patchValue({
      id: laptop.id,
      brandName: laptop.brandName,
      modelName: laptop.modelName,
      screenSize: laptop.screenSize,
      memory: laptop.memory,
      storage: laptop.storage,
      screenBrightness: laptop.screenBrightness,
      screenRefreshHz: laptop.screenRefreshHz,
      price: laptop.price,
    });
  }

  protected createFromForm(): ILaptop {
    return {
      ...new Laptop(),
      id: this.editForm.get(['id'])!.value,
      brandName: this.editForm.get(['brandName'])!.value,
      modelName: this.editForm.get(['modelName'])!.value,
      screenSize: this.editForm.get(['screenSize'])!.value,
      memory: this.editForm.get(['memory'])!.value,
      storage: this.editForm.get(['storage'])!.value,
      screenBrightness: this.editForm.get(['screenBrightness'])!.value,
      screenRefreshHz: this.editForm.get(['screenRefreshHz'])!.value,
      price: this.editForm.get(['price'])!.value,
    };
  }
}
