import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ContratoComponent } from './list/contrato.component';
import { ContratoDetailComponent } from './detail/contrato-detail.component';
import { ContratoUpdateComponent } from './update/contrato-update.component';
import { ContratoDeleteDialogComponent } from './delete/contrato-delete-dialog.component';
import { ContratoRoutingModule } from './route/contrato-routing.module';

@NgModule({
  imports: [SharedModule, ContratoRoutingModule],
  declarations: [ContratoComponent, ContratoDetailComponent, ContratoUpdateComponent, ContratoDeleteDialogComponent],
})
export class ContratoModule {}
