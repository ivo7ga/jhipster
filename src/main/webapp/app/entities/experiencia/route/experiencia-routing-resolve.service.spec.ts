jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExperiencia, Experiencia } from '../experiencia.model';
import { ExperienciaService } from '../service/experiencia.service';

import { ExperienciaRoutingResolveService } from './experiencia-routing-resolve.service';

describe('Experiencia routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ExperienciaRoutingResolveService;
  let service: ExperienciaService;
  let resultExperiencia: IExperiencia | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ExperienciaRoutingResolveService);
    service = TestBed.inject(ExperienciaService);
    resultExperiencia = undefined;
  });

  describe('resolve', () => {
    it('should return IExperiencia returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExperiencia = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultExperiencia).toEqual({ id: 123 });
    });

    it('should return new IExperiencia if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExperiencia = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultExperiencia).toEqual(new Experiencia());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Experiencia })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExperiencia = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultExperiencia).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
