import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILaptop } from '../laptop.model';
import { LaptopService } from '../service/laptop.service';

@Component({
  templateUrl: './laptop-delete-dialog.component.html',
})
export class LaptopDeleteDialogComponent {
  laptop?: ILaptop;

  constructor(protected laptopService: LaptopService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.laptopService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
