import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEntity2 } from 'app/shared/model/entity-2.model';
import { Entity2Service } from './entity-2.service';

@Component({
  templateUrl: './entity-2-delete-dialog.component.html'
})
export class Entity2DeleteDialogComponent {
  entity2?: IEntity2;

  constructor(protected entity2Service: Entity2Service, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.entity2Service.delete(id).subscribe(() => {
      this.eventManager.broadcast('entity2ListModification');
      this.activeModal.close();
    });
  }
}
