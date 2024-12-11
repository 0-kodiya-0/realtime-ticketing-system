import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DatePipe, NgForOf, NgIf } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { CustomerDto, VendorDto } from '../dto/models.dto';
import { FormsModule } from '@angular/forms';

interface RequestParams {
  limit: number;
  skip: number;
}

@Component({
  selector: 'app-simulation',
  templateUrl: './simulation.component.html',
  styleUrls: ['./simulation.component.css'],
  standalone: true,
  imports: [NgForOf, NgIf, FormsModule]
})
export class SimulationComponent implements OnInit {
  simulationType: 'customer' | 'vendor' = 'customer';
  selectedId: string | null = null;

  // Cached data arrays
  customerData: CustomerDto[][] = [];
  vendorData: VendorDto[][] = [];
  displayData: (CustomerDto | VendorDto)[] = [];

  currentPage = 0;
  itemsPerPage = 50;  // Match pool component's page size
  isLoading = false;

  showAddDialog = false;
  repetitionCount = 1;
  isVip = false;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.route.data.subscribe(data => {
      this.simulationType = data['type'];
      this.loadInitialData();
    });
  }

  loadInitialData() {
    this.currentPage = 0;
    this.customerData = [];
    this.vendorData = [];
    this.loadPageData(0);
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

  loadPageData(pageNumber: number) {
    if (this.isLoading) return;

    this.isLoading = true;
    const params: RequestParams = {
      limit: this.itemsPerPage,
      skip: pageNumber * this.itemsPerPage
    };

    const endpoint = this.simulationType === 'customer' ? 'customer/all' : 'vendor/all';


    this.http.get<CustomerDto[] | VendorDto[]>(`${environment.apiUrl}/simulation/${endpoint}`, { params: this.buildPaginationParams(params) })
      .subscribe({
        next: (data) => {
          try {
            if (this.simulationType === 'customer') {
              this.customerData[pageNumber] = data as unknown as CustomerDto[];
              this.displayData = this.customerData[pageNumber];
            } else {
              this.vendorData[pageNumber] = data as unknown as VendorDto[];
              this.displayData = this.vendorData[pageNumber];
            }
          } catch (error) {
            console.error('Error processing data:', error);
            // Reset data for this page
            if (this.simulationType === 'customer') {
              this.customerData[pageNumber] = [];
            } else {
              this.vendorData[pageNumber] = [];
            }
            this.displayData = [];
          }
        },
        error: (error) => {
          console.error('Error fetching data:', error);
          // Reset data for this page
          if (this.simulationType === 'customer') {
            this.customerData[pageNumber] = [];
          } else {
            this.vendorData[pageNumber] = [];
          }
          this.displayData = [];
        },
        complete: () => {
          this.isLoading = false;
        }
      });

  }

  getCurrentPageData(): (CustomerDto | VendorDto)[] {
    if (this.simulationType === 'customer') {
      return this.customerData[this.currentPage] || [];
    }
    return this.vendorData[this.currentPage] || [];
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

  openAddDialog() {
    this.showAddDialog = true;
  }

  closeAddDialog() {
    this.showAddDialog = false;
    this.repetitionCount = 1;
    this.isVip = false;
  }

  addEntity() {
    if (this.isLoading) return;

    this.isLoading = true;
    const endpoint = this.simulationType === 'customer' ? 'customer/add' : 'vendor/add';
    let params = new HttpParams()
      .set('repetitionCount', this.repetitionCount.toString());

    if (this.simulationType === 'customer') {
      params = params.set('isVip', this.isVip.toString());
    }

    this.http.post<number>(`${environment.apiUrl}/simulation/${endpoint}`, null, { params })
      .subscribe({
        next: () => {
          this.closeAddDialog();
          this.loadInitialData();
        },
        error: (error) => {
          console.error('Error adding entity:', error);
        },
        complete: () => {
          this.isLoading = false;
        }
      });
  }

  startEntity() {
    if (this.isLoading) return;

    this.isLoading = true;
    const endpoint = this.simulationType === 'customer' ? 'customer/start' : 'vendor/start';
    this.http.get<number>(`${environment.apiUrl}/simulation/${endpoint}`)
      .subscribe({
        next: () => this.loadInitialData(),
        error: (error) => {
          console.error('Error starting entity:', error);
        },
        complete: () => {
          this.isLoading = false;
        }
      });
  }

  stopEntity() {
    if (this.isLoading) return;

    this.isLoading = true;
    const endpoint = this.simulationType === 'customer' ? 'customer/stop' : 'vendor/stop';
    this.http.get<number>(`${environment.apiUrl}/simulation/${endpoint}`)
      .subscribe({
        next: () => this.loadInitialData(),
        error: (error) => {
          console.error('Error stopping entity:', error);
        },
        complete: () => {
          this.isLoading = false;
        }
      });
  }

  isCustomer(entity: CustomerDto | VendorDto): entity is CustomerDto {
    return 'type' in entity;
  }
}
