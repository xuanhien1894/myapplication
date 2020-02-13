import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AladinTestModule } from '../../../test.module';
import { Entity2UpdateComponent } from 'app/entities/entity-2/entity-2-update.component';
import { Entity2Service } from 'app/entities/entity-2/entity-2.service';
import { Entity2 } from 'app/shared/model/entity-2.model';

describe('Component Tests', () => {
  describe('Entity2 Management Update Component', () => {
    let comp: Entity2UpdateComponent;
    let fixture: ComponentFixture<Entity2UpdateComponent>;
    let service: Entity2Service;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AladinTestModule],
        declarations: [Entity2UpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(Entity2UpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(Entity2UpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(Entity2Service);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Entity2(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Entity2();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
