import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEntity2, Entity2 } from 'app/shared/model/entity-2.model';
import { Entity2Service } from './entity-2.service';
import { Entity2Component } from './entity-2.component';
import { Entity2DetailComponent } from './entity-2-detail.component';
import { Entity2UpdateComponent } from './entity-2-update.component';

@Injectable({ providedIn: 'root' })
export class Entity2Resolve implements Resolve<IEntity2> {
  constructor(private service: Entity2Service, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEntity2> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((entity2: HttpResponse<Entity2>) => {
          if (entity2.body) {
            return of(entity2.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Entity2());
  }
}

export const entity2Route: Routes = [
  {
    path: '',
    component: Entity2Component,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'aladinApp.entity2.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: Entity2DetailComponent,
    resolve: {
      entity2: Entity2Resolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'aladinApp.entity2.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: Entity2UpdateComponent,
    resolve: {
      entity2: Entity2Resolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'aladinApp.entity2.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: Entity2UpdateComponent,
    resolve: {
      entity2: Entity2Resolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'aladinApp.entity2.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
