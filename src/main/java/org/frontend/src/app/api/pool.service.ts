import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";
import {RequestParams} from "../dto/request.dto";
import {PurchaseDto, TicketDto} from "../dto/models.dto";

@Injectable({
  providedIn: 'root'
})
export class PoolService {
  private baseUrl = `${environment.apiUrl}/pool`;

  constructor(private http: HttpClient) {}

  // Helper method to build pagination parameters
  private buildPaginationParams(params: RequestParams): HttpParams {
    let httpParams = new HttpParams();

    if (params.limit !== undefined) {
      httpParams = httpParams.set('limit', params.limit.toString());
    }

    if (params.skip !== undefined) {
      httpParams = httpParams.set('skip', params.skip.toString());
    }

    return httpParams;
  }

  // Ticket endpoints
  getNotFullTickets(): Observable<TicketDto[]> {
    return this.http.get<TicketDto[]>(`${this.baseUrl}/ticket/quantity-not-full`);
  }

  getAllTickets(params: RequestParams = { limit: 10, skip: 0 }): Observable<TicketDto[]> {
    const httpParams = this.buildPaginationParams(params);
    return this.http.get<TicketDto[]>(`${this.baseUrl}/ticket/all`, { params: httpParams });
  }

  // Purchase endpoints
  getAllPurchases(params: RequestParams = { limit: 10, skip: 0 }): Observable<PurchaseDto[]> {
    const httpParams = this.buildPaginationParams(params);
    return this.http.get<PurchaseDto[]>(`${this.baseUrl}/purchase/all`, { params: httpParams });
  }

  getPurchasedStatus(params: RequestParams = { limit: 10, skip: 0 }): Observable<PurchaseDto[]> {
    const httpParams = this.buildPaginationParams(params);
    return this.http.get<PurchaseDto[]>(`${this.baseUrl}/purchase/status-purchased`, { params: httpParams });
  }

  getPendingStatus(params: RequestParams = { limit: 10, skip: 0 }): Observable<PurchaseDto[]> {
    const httpParams = this.buildPaginationParams(params);
    return this.http.get<PurchaseDto[]>(`${this.baseUrl}/purchase/status-pending`, { params: httpParams });
  }
}
