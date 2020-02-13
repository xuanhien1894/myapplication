import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEntity1 } from 'app/shared/model/entity-1.model';
import { Entity1Service } from './entity-1.service';
import { Entity1DeleteDialogComponent } from './entity-1-delete-dialog.component';

@Component({
  selector: 'jhi-entity-1',
  templateUrl: './entity-1.component.html'
})
export class Entity1Component implements OnInit, OnDestroy {
  entity1S?: IEntity1[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected entity1Service: Entity1Service,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.entity1Service
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IEntity1[]>) => (this.entity1S = res.body ? res.body : []));
      return;
    }
    this.entity1Service.query().subscribe((res: HttpResponse<IEntity1[]>) => {
      this.entity1S = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEntity1S();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEntity1): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEntity1S(): void {
    this.eventSubscriber = this.eventManager.subscribe('entity1ListModification', () => this.loadAll());
  }

  delete(entity1: IEntity1): void {
    const modalRef = this.modalService.open(Entity1DeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.entity1 = entity1;
  }
}
