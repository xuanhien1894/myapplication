import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEntity1, Entity1 } from 'app/shared/model/entity-1.model';
import { Entity1Service } from './entity-1.service';

@Component({
  selector: 'jhi-entity-1-update',
  templateUrl: './entity-1-update.component.html'
})
export class Entity1UpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    price: []
  });

  constructor(protected entity1Service: Entity1Service, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entity1 }) => {
      this.updateForm(entity1);
    });
  }

  updateForm(entity1: IEntity1): void {
    this.editForm.patchValue({
      id: entity1.id,
      name: entity1.name,
      price: entity1.price
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const entity1 = this.createFromForm();
    if (entity1.id !== undefined) {
      this.subscribeToSaveResponse(this.entity1Service.update(entity1));
    } else {
      this.subscribeToSaveResponse(this.entity1Service.create(entity1));
    }
  }

  private createFromForm(): IEntity1 {
    return {
      ...new Entity1(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      price: this.editForm.get(['price'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntity1>>): void {
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
