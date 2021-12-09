import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IExperiencia, getExperienciaIdentifier } from '../experiencia.model';

export type EntityResponseType = HttpResponse<IExperiencia>;
export type EntityArrayResponseType = HttpResponse<IExperiencia[]>;

@Injectable({ providedIn: 'root' })
export class ExperienciaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/experiencias');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/experiencias');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(experiencia: IExperiencia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(experiencia);
    return this.http
      .post<IExperiencia>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(experiencia: IExperiencia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(experiencia);
    return this.http
      .put<IExperiencia>(`${this.resourceUrl}/${getExperienciaIdentifier(experiencia) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(experiencia: IExperiencia): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(experiencia);
    return this.http
      .patch<IExperiencia>(`${this.resourceUrl}/${getExperienciaIdentifier(experiencia) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExperiencia>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExperiencia[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExperiencia[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addExperienciaToCollectionIfMissing(
    experienciaCollection: IExperiencia[],
    ...experienciasToCheck: (IExperiencia | null | undefined)[]
  ): IExperiencia[] {
    const experiencias: IExperiencia[] = experienciasToCheck.filter(isPresent);
    if (experiencias.length > 0) {
      const experienciaCollectionIdentifiers = experienciaCollection.map(experienciaItem => getExperienciaIdentifier(experienciaItem)!);
      const experienciasToAdd = experiencias.filter(experienciaItem => {
        const experienciaIdentifier = getExperienciaIdentifier(experienciaItem);
        if (experienciaIdentifier == null || experienciaCollectionIdentifiers.includes(experienciaIdentifier)) {
          return false;
        }
        experienciaCollectionIdentifiers.push(experienciaIdentifier);
        return true;
      });
      return [...experienciasToAdd, ...experienciaCollection];
    }
    return experienciaCollection;
  }

  protected convertDateFromClient(experiencia: IExperiencia): IExperiencia {
    return Object.assign({}, experiencia, {
      fecha: experiencia.fecha?.isValid() ? experiencia.fecha.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? dayjs(res.body.fecha) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((experiencia: IExperiencia) => {
        experiencia.fecha = experiencia.fecha ? dayjs(experiencia.fecha) : undefined;
      });
    }
    return res;
  }
}
