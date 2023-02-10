import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DireccionComponent } from './list/direccion.component';
import { DireccionDetailComponent } from './detail/direccion-detail.component';
import { DireccionUpdateComponent } from './update/direccion-update.component';
import { DireccionDeleteDialogComponent } from './delete/direccion-delete-dialog.component';
import { DireccionRoutingModule } from './route/direccion-routing.module';

@NgModule({
  imports: [SharedModule, DireccionRoutingModule],
  declarations: [DireccionComponent, DireccionDetailComponent, DireccionUpdateComponent, DireccionDeleteDialogComponent],
})
export class DireccionModule {}
