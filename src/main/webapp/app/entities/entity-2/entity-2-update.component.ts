import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEntity2, Entity2 } from 'app/shared/model/entity-2.model';
import { Entity2Service } from './entity-2.service';

@Component({
  selector: 'jhi-entity-2-update',
  templateUrl: './entity-2-update.component.html'
})
export class Entity2UpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    price: []
  });

  constructor(protected entity2Service: Entity2Service, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entity2 }) => {
      this.updateForm(entity2);
    });
  }

  updateForm(entity2: IEntity2): void {
    this.editForm.patchValue({
      id: entity2.id,
      name: entity2.name,
      price: entity2.price
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const entity2 = this.createFromForm();
    if (entity2.id !== undefined) {
      this.subscribeToSaveResponse(this.entity2Service.update(entity2));
    } else {
      this.subscribeToSaveResponse(this.entity2Service.create(entity2));
    }
  }

  private createFromForm(): IEntity2 {
    return {
      ...new Entity2(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      price: this.editForm.get(['price'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntity2>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
