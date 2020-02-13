import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IEntity1 } from 'app/shared/model/entity-1.model';

type EntityResponseType = HttpResponse<IEntity1>;
type EntityArrayResponseType = HttpResponse<IEntity1[]>;

@Injectable({ providedIn: 'root' })
export class Entity1Service {
  public resourceUrl = SERVER_API_URL + 'api/entity-1-s';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/entity-1-s';

  constructor(protected http: HttpClient) {}

  create(entity1: IEntity1): Observable<EntityResponseType> {
    return this.http.post<IEntity1>(this.resourceUrl, entity1, { observe: 'response' });
  }

  update(entity1: IEntity1): Observable<EntityResponseType> {
    return this.http.put<IEntity1>(this.resourceUrl, entity1, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEntity1>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEntity1[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEntity1[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
