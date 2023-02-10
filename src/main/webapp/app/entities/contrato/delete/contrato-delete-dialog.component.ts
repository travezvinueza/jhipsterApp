import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContrato } from '../contrato.model';
import { ContratoService } from '../service/contrato.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './contrato-delete-dialog.component.html',
})
export class ContratoDeleteDialogComponent {
  contrato?: IContrato;

  constructor(protected contratoService: ContratoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contratoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
