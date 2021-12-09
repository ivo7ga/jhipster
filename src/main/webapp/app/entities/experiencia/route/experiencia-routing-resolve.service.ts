import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExperiencia, Experiencia } from '../experiencia.model';
import { ExperienciaService } from '../service/experiencia.service';

@Injectable({ providedIn: 'root' })
export class ExperienciaRoutingResolveService implements Resolve<IExperiencia> {
  constructor(protected service: ExperienciaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExperiencia> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((experiencia: HttpResponse<Experiencia>) => {
          if (experiencia.body) {
            return of(experiencia.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Experiencia());
  }
}
