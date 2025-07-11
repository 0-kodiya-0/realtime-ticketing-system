import { Component, OnInit } from '@angular/core';
import { DatePipe, NgForOf, NgIf } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import {PoolService} from '../api/pool.service';
import {PurchaseDto, TicketDto} from '../dto/models.dto';
import {RequestParams} from '../dto/request.dto';

@Component({
  selector: 'app-pool',
  imports: [
    DatePipe,
    NgIf,
    NgForOf
  ],
  templateUrl: './pool.component.html',
  styleUrl: './pool.component.css'
})
export class PoolComponent implements OnInit {
  selectedPool: string = 'ticket';
  ticketPoolData: TicketDto[][] = [];
  purchasePoolData: PurchaseDto[][] = [];
  currentPage: number = 0;
  itemsPerPage: number = 50;
  isLoading: boolean = false;

  constructor(private http: HttpClient, private poolService: PoolService) {}

  ngOnInit() {
    this.loadInitialData();
  }

  loadInitialData() {
    this.currentPage = 0;
    this.ticketPoolData = [];
    this.purchasePoolData = [];
    this.loadPageData(0);
  }

  loadPageData(pageNumber: number) {
    if (this.isLoading) return;

    this.isLoading = true;
    const params: RequestParams = {
      limit: this.itemsPerPage,
      skip: pageNumber * this.itemsPerPage
    };

    if (this.selectedPool === 'ticket') {
      this.poolService.getAllTickets(params).subscribe({
        next: (data) => {
          this.ticketPoolData[pageNumber] = data;
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
        }
      });
    } else {
      this.poolService.getAllPurchases(params).subscribe({
        next: (data) => {
          this.purchasePoolData[pageNumber] = data;
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
        }
      });
    }
  }

  getCurrentPageData(): any[] {
    if (this.selectedPool === 'ticket') {
      return this.ticketPoolData[this.currentPage] || [];
    }
    return this.purchasePoolData[this.currentPage] || [];
  }

  canGoBack(): boolean {
    return this.currentPage > 0;
  }

  canGoForward(): boolean {
    const currentData = this.getCurrentPageData();
    return currentData.length === this.itemsPerPage;
  }

  goToNextPage() {
    if (this.canGoForward() && !this.isLoading) {
      this.currentPage++;
      // Always load next page data if it doesn't exist
      if (!this.getCurrentPageData().length) {
        this.loadPageData(this.currentPage);
      }
    }
  }

  goToPreviousPage() {
    if (this.canGoBack() && !this.isLoading) {
      this.currentPage--;
    }
  }

  refreshData() {
    this.loadInitialData();
  }
}
