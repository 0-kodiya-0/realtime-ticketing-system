import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";
import {CustomerDto, PurchaseDto, TicketDto, VendorDto} from "../dto/models.dto";
import {RequestParams} from "../dto/request.dto";

@Injectable({
    providedIn: 'root'
})
export class SimulationService {
    private baseUrl = `${environment.apiUrl}/simulation`;

    constructor(private http: HttpClient) {}

    isRunning(): Observable<boolean> {
        return this.http.get<boolean>(`${this.baseUrl}/is-running`);
    }

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

    // Simulation controls
    startSimulation(): Observable<number> {
        return this.http.get<number>(`${this.baseUrl}/start`);
    }

    stopSimulation(): Observable<number> {
        return this.http.get<number>(`${this.baseUrl}/stop`);
    }

    // Customer operations
    getAllCustomers(params: RequestParams = { limit: 10, skip: 0 }): Observable<CustomerDto[]> {
        const httpParams = this.buildPaginationParams(params);
        return this.http.get<CustomerDto[]>(`${this.baseUrl}/customer/all`, { params: httpParams });
    }

    startCustomer(): Observable<number> {
        return this.http.get<number>(`${this.baseUrl}/customer/start`);
    }

    stopCustomer(): Observable<number> {
        return this.http.get<number>(`${this.baseUrl}/customer/stop`);
    }

    getCustomerPurchases(id: string): Observable<PurchaseDto[]> {
        return this.http.get<PurchaseDto[]>(`${this.baseUrl}/customer/purchase/${id}`);
    }

    addCustomers(isVip: boolean, repetitionCount: number): Observable<number> {
        const params = new HttpParams()
            .set('isVip', isVip.toString())
            .set('repetitionCount', repetitionCount.toString());

        return this.http.post<number>(`${this.baseUrl}/customer/add`, null, { params });
    }



    // Vendor operations
    getAllVendors(params: RequestParams = { limit: 10, skip: 0 }): Observable<VendorDto[]> {
        const httpParams = this.buildPaginationParams(params);
        return this.http.get<VendorDto[]>(`${this.baseUrl}/vendor/all`, { params:httpParams });
    }

    startVendor(): Observable<number> {
        return this.http.get<number>(`${this.baseUrl}/vendor/start`);
    }

    stopVendor(): Observable<number> {
        return this.http.get<number>(`${this.baseUrl}/vendor/stop`);
    }

    getVendorActiveTickets(id: string, params: RequestParams = { limit: 10, skip: 0 }): Observable<TicketDto[]> {
        const httpParams = this.buildPaginationParams(params);
        return this.http.get<TicketDto[]>(`${this.baseUrl}/vendor/ticket-active/${id}`, { params:httpParams });
    }

    addVendors(repetitionCount: number): Observable<number> {
        const params = new HttpParams()
            .set('repetitionCount', repetitionCount.toString());

        return this.http.post<number>(`${this.baseUrl}/vendor/add`, null, { params });
    }

}
