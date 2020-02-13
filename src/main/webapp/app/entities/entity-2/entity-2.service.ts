import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IEntity2 } from 'app/shared/model/entity-2.model';

type EntityResponseType = HttpResponse<IEntity2>;
type EntityArrayResponseType = HttpResponse<IEntity2[]>;

@Injectable({ providedIn: 'root' })
export class Entity2Service {
  public resourceUrl = SERVER_API_URL + 'api/entity-2-s';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/entity-2-s';

  constructor(protected http: HttpClient) {}

  create(entity2: IEntity2): Observable<EntityResponseType> {
    return this.http.post<IEntity2>(this.resourceUrl, entity2, { observe: 'response' });
  }

  update(entity2: IEntity2): Observable<EntityResponseType> {
    return this.http.put<IEntity2>(this.resourceUrl, entity2, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEntity2>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEntity2[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEntity2[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
