import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../environments/environment";
import {CustomerDto, PurchaseDto, TicketDto, VendorDto} from "../dto/models.dto";

@Injectable({
    providedIn: 'root'
})
export class SimulationService {
    private baseUrl = `${environment.apiUrl}/simulation`;

    constructor(private http: HttpClient) {}

    isRunning(): Observable<boolean> {
        return this.http.get<boolean>(`${this.baseUrl}/is-running`);
    }

    // Simulation controls
    startSimulation(): Observable<number> {
        return this.http.get<number>(`${this.baseUrl}/start`);
    }

    stopSimulation(): Observable<number> {
        return this.http.get<number>(`${this.baseUrl}/stop`);
    }

    // Customer operations
    getAllCustomers(limit: number = 10, skip: number = 0): Observable<CustomerDto[]> {
        const params = new HttpParams()
            .set('limit', limit.toString())
            .set('skip', skip.toString());

        return this.http.get<CustomerDto[]>(`${this.baseUrl}/customer/all`, { params });
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
    getAllVendors(limit: number = 10, skip: number = 0): Observable<VendorDto[]> {
        const params = new HttpParams()
            .set('limit', limit.toString())
            .set('skip', skip.toString());

        return this.http.get<VendorDto[]>(`${this.baseUrl}/vendor/all`, { params });
    }

    startVendor(): Observable<number> {
        return this.http.get<number>(`${this.baseUrl}/vendor/start`);
    }

    stopVendor(): Observable<number> {
        return this.http.get<number>(`${this.baseUrl}/vendor/stop`);
    }

    getVendorActiveTickets(id: string): Observable<TicketDto[]> {
        return this.http.get<TicketDto[]>(`${this.baseUrl}/vendor/ticket-active/${id}`);
    }

    getVendorRemovedTickets(id: string): Observable<TicketDto[]> {
        return this.http.get<TicketDto[]>(`${this.baseUrl}/vendor/ticket-removed/${id}`);
    }
    addVendors(repetitionCount: number): Observable<number> {
        const params = new HttpParams()
            .set('repetitionCount', repetitionCount.toString());

        return this.http.post<number>(`${this.baseUrl}/vendor/add`, null, { params });
    }

}
