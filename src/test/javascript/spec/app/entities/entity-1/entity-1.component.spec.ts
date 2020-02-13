import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { AladinTestModule } from '../../../test.module';
import { Entity1Component } from 'app/entities/entity-1/entity-1.component';
import { Entity1Service } from 'app/entities/entity-1/entity-1.service';
import { Entity1 } from 'app/shared/model/entity-1.model';

describe('Component Tests', () => {
  describe('Entity1 Management Component', () => {
    let comp: Entity1Component;
    let fixture: ComponentFixture<Entity1Component>;
    let service: Entity1Service;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AladinTestModule],
        declarations: [Entity1Component],
        providers: []
      })
        .overrideTemplate(Entity1Component, '')
        .compileComponents();

      fixture = TestBed.createComponent(Entity1Component);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(Entity1Service);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Entity1(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.entity1S && comp.entity1S[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
