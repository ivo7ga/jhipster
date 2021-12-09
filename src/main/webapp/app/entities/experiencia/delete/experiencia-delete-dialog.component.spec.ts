jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ExperienciaService } from '../service/experiencia.service';

import { ExperienciaDeleteDialogComponent } from './experiencia-delete-dialog.component';

describe('Component Tests', () => {
  describe('Experiencia Management Delete Component', () => {
    let comp: ExperienciaDeleteDialogComponent;
    let fixture: ComponentFixture<ExperienciaDeleteDialogComponent>;
    let service: ExperienciaService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExperienciaDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ExperienciaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExperienciaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ExperienciaService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
