import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'entity-1',
        loadChildren: () => import('./entity-1/entity-1.module').then(m => m.AladinEntity1Module)
      },
      {
        path: 'entity-2',
        loadChildren: () => import('./entity-2/entity-2.module').then(m => m.AladinEntity2Module)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class AladinEntityModule {}
