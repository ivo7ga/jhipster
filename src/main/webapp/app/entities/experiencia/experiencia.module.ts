import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExperienciaComponent } from './list/experiencia.component';
import { ExperienciaDetailComponent } from './detail/experiencia-detail.component';
import { ExperienciaUpdateComponent } from './update/experiencia-update.component';
import { ExperienciaDeleteDialogComponent } from './delete/experiencia-delete-dialog.component';
import { ExperienciaRoutingModule } from './route/experiencia-routing.module';

@NgModule({
  imports: [SharedModule, ExperienciaRoutingModule],
  declarations: [ExperienciaComponent, ExperienciaDetailComponent, ExperienciaUpdateComponent, ExperienciaDeleteDialogComponent],
  entryComponents: [ExperienciaDeleteDialogComponent],
})
export class ExperienciaModule {}
