import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'experiencia',
        data: { pageTitle: 'Experiencias' },
        loadChildren: () => import('./experiencia/experiencia.module').then(m => m.ExperienciaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
