import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExperiencia } from '../experiencia.model';

@Component({
  selector: 'jhi-experiencia-detail',
  templateUrl: './experiencia-detail.component.html',
})
export class ExperienciaDetailComponent implements OnInit {
  experiencia: IExperiencia | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ experiencia }) => {
      this.experiencia = experiencia;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
