import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEntity2 } from 'app/shared/model/entity-2.model';

@Component({
  selector: 'jhi-entity-2-detail',
  templateUrl: './entity-2-detail.component.html'
})
export class Entity2DetailComponent implements OnInit {
  entity2: IEntity2 | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entity2 }) => {
      this.entity2 = entity2;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
