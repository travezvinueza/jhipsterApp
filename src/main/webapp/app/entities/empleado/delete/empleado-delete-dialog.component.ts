import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmpleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './empleado-delete-dialog.component.html',
})
export class EmpleadoDeleteDialogComponent {
  empleado?: IEmpleado;

  constructor(protected empleadoService: EmpleadoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.empleadoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
