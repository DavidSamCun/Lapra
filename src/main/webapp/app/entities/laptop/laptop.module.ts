import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LaptopComponent } from './list/laptop.component';
import { LaptopDetailComponent } from './detail/laptop-detail.component';
import { LaptopUpdateComponent } from './update/laptop-update.component';
import { LaptopDeleteDialogComponent } from './delete/laptop-delete-dialog.component';
import { LaptopRoutingModule } from './route/laptop-routing.module';

@NgModule({
  imports: [SharedModule, LaptopRoutingModule],
  declarations: [LaptopComponent, LaptopDetailComponent, LaptopUpdateComponent, LaptopDeleteDialogComponent],
  entryComponents: [LaptopDeleteDialogComponent],
})
export class LaptopModule {}
