import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepartamentoComponent } from './list/departamento.component';
import { DepartamentoDetailComponent } from './detail/departamento-detail.component';
import { DepartamentoUpdateComponent } from './update/departamento-update.component';
import { DepartamentoDeleteDialogComponent } from './delete/departamento-delete-dialog.component';
import { DepartamentoRoutingModule } from './route/departamento-routing.module';

@NgModule({
  imports: [SharedModule, DepartamentoRoutingModule],
  declarations: [DepartamentoComponent, DepartamentoDetailComponent, DepartamentoUpdateComponent, DepartamentoDeleteDialogComponent],
})
export class DepartamentoModule {}
