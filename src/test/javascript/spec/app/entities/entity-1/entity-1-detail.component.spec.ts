import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AladinTestModule } from '../../../test.module';
import { Entity1DetailComponent } from 'app/entities/entity-1/entity-1-detail.component';
import { Entity1 } from 'app/shared/model/entity-1.model';

describe('Component Tests', () => {
  describe('Entity1 Management Detail Component', () => {
    let comp: Entity1DetailComponent;
    let fixture: ComponentFixture<Entity1DetailComponent>;
    const route = ({ data: of({ entity1: new Entity1(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AladinTestModule],
        declarations: [Entity1DetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(Entity1DetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(Entity1DetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load entity1 on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.entity1).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
