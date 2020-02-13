import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEntity2 } from 'app/shared/model/entity-2.model';
import { Entity2Service } from './entity-2.service';
import { Entity2DeleteDialogComponent } from './entity-2-delete-dialog.component';

@Component({
  selector: 'jhi-entity-2',
  templateUrl: './entity-2.component.html'
})
export class Entity2Component implements OnInit, OnDestroy {
  entity2S?: IEntity2[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected entity2Service: Entity2Service,
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
      this.entity2Service
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IEntity2[]>) => (this.entity2S = res.body ? res.body : []));
      return;
    }
    this.entity2Service.query().subscribe((res: HttpResponse<IEntity2[]>) => {
      this.entity2S = res.body ? res.body : [];
      this.currentSearch = '';
    });
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEntity2S();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEntity2): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEntity2S(): void {
    this.eventSubscriber = this.eventManager.subscribe('entity2ListModification', () => this.loadAll());
  }

  delete(entity2: IEntity2): void {
    const modalRef = this.modalService.open(Entity2DeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.entity2 = entity2;
  }
}
