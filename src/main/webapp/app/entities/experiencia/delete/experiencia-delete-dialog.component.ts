import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExperiencia } from '../experiencia.model';
import { ExperienciaService } from '../service/experiencia.service';

@Component({
  templateUrl: './experiencia-delete-dialog.component.html',
})
export class ExperienciaDeleteDialogComponent {
  experiencia?: IExperiencia;

  constructor(protected experienciaService: ExperienciaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.experienciaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
