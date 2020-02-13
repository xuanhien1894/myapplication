import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEntity1 } from 'app/shared/model/entity-1.model';
import { Entity1Service } from './entity-1.service';

@Component({
  templateUrl: './entity-1-delete-dialog.component.html'
})
export class Entity1DeleteDialogComponent {
  entity1?: IEntity1;

  constructor(protected entity1Service: Entity1Service, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.entity1Service.delete(id).subscribe(() => {
      this.eventManager.broadcast('entity1ListModification');
      this.activeModal.close();
    });
  }
}
