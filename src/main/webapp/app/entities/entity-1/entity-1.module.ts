import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AladinSharedModule } from 'app/shared/shared.module';
import { Entity1Component } from './entity-1.component';
import { Entity1DetailComponent } from './entity-1-detail.component';
import { Entity1UpdateComponent } from './entity-1-update.component';
import { Entity1DeleteDialogComponent } from './entity-1-delete-dialog.component';
import { entity1Route } from './entity-1.route';

@NgModule({
  imports: [AladinSharedModule, RouterModule.forChild(entity1Route)],
  declarations: [Entity1Component, Entity1DetailComponent, Entity1UpdateComponent, Entity1DeleteDialogComponent],
  entryComponents: [Entity1DeleteDialogComponent]
})
export class AladinEntity1Module {}
