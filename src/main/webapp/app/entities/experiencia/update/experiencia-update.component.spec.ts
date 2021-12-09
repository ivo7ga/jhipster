jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExperienciaService } from '../service/experiencia.service';
import { IExperiencia, Experiencia } from '../experiencia.model';

import { ExperienciaUpdateComponent } from './experiencia-update.component';

describe('Component Tests', () => {
  describe('Experiencia Management Update Component', () => {
    let comp: ExperienciaUpdateComponent;
    let fixture: ComponentFixture<ExperienciaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let experienciaService: ExperienciaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExperienciaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExperienciaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExperienciaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      experienciaService = TestBed.inject(ExperienciaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const experiencia: IExperiencia = { id: 456 };

        activatedRoute.data = of({ experiencia });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(experiencia));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Experiencia>>();
        const experiencia = { id: 123 };
        jest.spyOn(experienciaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ experiencia });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: experiencia }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(experienciaService.update).toHaveBeenCalledWith(experiencia);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Experiencia>>();
        const experiencia = new Experiencia();
        jest.spyOn(experienciaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ experiencia });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: experiencia }));
        saveSubject.complete();

        // THEN
        expect(experienciaService.create).toHaveBeenCalledWith(experiencia);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Experiencia>>();
        const experiencia = { id: 123 };
        jest.spyOn(experienciaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ experiencia });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(experienciaService.update).toHaveBeenCalledWith(experiencia);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
