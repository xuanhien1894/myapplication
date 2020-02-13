import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEntity1 } from 'app/shared/model/entity-1.model';

@Component({
  selector: 'jhi-entity-1-detail',
  templateUrl: './entity-1-detail.component.html'
})
export class Entity1DetailComponent implements OnInit {
  entity1: IEntity1 | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entity1 }) => {
      this.entity1 = entity1;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
