import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { AladinTestModule } from '../../../test.module';
import { Entity2Component } from 'app/entities/entity-2/entity-2.component';
import { Entity2Service } from 'app/entities/entity-2/entity-2.service';
import { Entity2 } from 'app/shared/model/entity-2.model';

describe('Component Tests', () => {
  describe('Entity2 Management Component', () => {
    let comp: Entity2Component;
    let fixture: ComponentFixture<Entity2Component>;
    let service: Entity2Service;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AladinTestModule],
        declarations: [Entity2Component],
        providers: []
      })
        .overrideTemplate(Entity2Component, '')
        .compileComponents();

      fixture = TestBed.createComponent(Entity2Component);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(Entity2Service);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Entity2(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.entity2S && comp.entity2S[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
