import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AladinTestModule } from '../../../test.module';
import { Entity1UpdateComponent } from 'app/entities/entity-1/entity-1-update.component';
import { Entity1Service } from 'app/entities/entity-1/entity-1.service';
import { Entity1 } from 'app/shared/model/entity-1.model';

describe('Component Tests', () => {
  describe('Entity1 Management Update Component', () => {
    let comp: Entity1UpdateComponent;
    let fixture: ComponentFixture<Entity1UpdateComponent>;
    let service: Entity1Service;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AladinTestModule],
        declarations: [Entity1UpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(Entity1UpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(Entity1UpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(Entity1Service);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Entity1(123);
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
        const entity = new Entity1();
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
