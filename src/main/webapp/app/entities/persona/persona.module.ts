import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PersonaComponent } from './list/persona.component';
import { PersonaDetailComponent } from './detail/persona-detail.component';
import { PersonaUpdateComponent } from './update/persona-update.component';
import { PersonaDeleteDialogComponent } from './delete/persona-delete-dialog.component';
import { PersonaRoutingModule } from './route/persona-routing.module';

@NgModule({
  imports: [SharedModule, PersonaRoutingModule],
  declarations: [PersonaComponent, PersonaDetailComponent, PersonaUpdateComponent, PersonaDeleteDialogComponent],
})
export class PersonaModule {}
