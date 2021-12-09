import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExperienciaDetailComponent } from './experiencia-detail.component';

describe('Component Tests', () => {
  describe('Experiencia Management Detail Component', () => {
    let comp: ExperienciaDetailComponent;
    let fixture: ComponentFixture<ExperienciaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ExperienciaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ experiencia: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ExperienciaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExperienciaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load experiencia on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.experiencia).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
