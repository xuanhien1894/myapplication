import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AladinSharedModule } from 'app/shared/shared.module';
import { Entity2Component } from './entity-2.component';
import { Entity2DetailComponent } from './entity-2-detail.component';
import { Entity2UpdateComponent } from './entity-2-update.component';
import { Entity2DeleteDialogComponent } from './entity-2-delete-dialog.component';
import { entity2Route } from './entity-2.route';

@NgModule({
  imports: [AladinSharedModule, RouterModule.forChild(entity2Route)],
  declarations: [Entity2Component, Entity2DetailComponent, Entity2UpdateComponent, Entity2DeleteDialogComponent],
  entryComponents: [Entity2DeleteDialogComponent]
})
export class AladinEntity2Module {}
