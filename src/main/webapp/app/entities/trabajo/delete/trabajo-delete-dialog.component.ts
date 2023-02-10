import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrabajo } from '../trabajo.model';
import { TrabajoService } from '../service/trabajo.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './trabajo-delete-dialog.component.html',
})
export class TrabajoDeleteDialogComponent {
  trabajo?: ITrabajo;

  constructor(protected trabajoService: TrabajoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trabajoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
