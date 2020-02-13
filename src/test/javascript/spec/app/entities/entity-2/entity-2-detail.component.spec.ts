import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AladinTestModule } from '../../../test.module';
import { Entity2DetailComponent } from 'app/entities/entity-2/entity-2-detail.component';
import { Entity2 } from 'app/shared/model/entity-2.model';

describe('Component Tests', () => {
  describe('Entity2 Management Detail Component', () => {
    let comp: Entity2DetailComponent;
    let fixture: ComponentFixture<Entity2DetailComponent>;
    const route = ({ data: of({ entity2: new Entity2(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AladinTestModule],
        declarations: [Entity2DetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(Entity2DetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(Entity2DetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load entity2 on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.entity2).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
