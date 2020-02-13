import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEntity1, Entity1 } from 'app/shared/model/entity-1.model';
import { Entity1Service } from './entity-1.service';
import { Entity1Component } from './entity-1.component';
import { Entity1DetailComponent } from './entity-1-detail.component';
import { Entity1UpdateComponent } from './entity-1-update.component';

@Injectable({ providedIn: 'root' })
export class Entity1Resolve implements Resolve<IEntity1> {
  constructor(private service: Entity1Service, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEntity1> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((entity1: HttpResponse<Entity1>) => {
          if (entity1.body) {
            return of(entity1.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Entity1());
  }
}

export const entity1Route: Routes = [
  {
    path: '',
    component: Entity1Component,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'aladinApp.entity1.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: Entity1DetailComponent,
    resolve: {
      entity1: Entity1Resolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'aladinApp.entity1.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: Entity1UpdateComponent,
    resolve: {
      entity1: Entity1Resolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'aladinApp.entity1.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: Entity1UpdateComponent,
    resolve: {
      entity1: Entity1Resolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'aladinApp.entity1.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
