import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {CustomerDto, VendorDto} from '../dto/models.dto';
import {FormsModule} from '@angular/forms';
import {SimulationService} from '../api/simulation.service';
import {RequestParams} from '../dto/request.dto';

@Component({
  selector: 'app-simulation',
  templateUrl: './simulation.component.html',
  styleUrls: ['./simulation.component.css'],
  standalone: true,
  imports: [NgForOf, NgIf, FormsModule]
})
export class SimulationComponent implements OnInit {
  simulationType: 'customer' | 'vendor' = 'customer';

  customerData: CustomerDto[][] = [];
  vendorData: VendorDto[][] = [];

  currentPage = 0;
  itemsPerPage = 50;
  isLoading = false;

  showAddDialog = false;
  repetitionCount = 1;
  isVip = false;

  constructor(
    private simulationService: SimulationService,
  ) {
  }

  ngOnInit() {
    this.loadInitialData();
  }

  loadInitialData() {
    this.currentPage = 0;
    this.customerData = [];
    this.vendorData = [];
    this.loadPageData(0);
  }

  loadPageData(pageNumber: number) {
    if (this.isLoading) return;

    this.isLoading = true;
    const params: RequestParams = {
      limit: this.itemsPerPage,
      skip: pageNumber * this.itemsPerPage
    };

    if (this.simulationType === 'customer') {
      this.simulationService.getAllCustomers(params).subscribe({
        next: (data) => {
          this.customerData[pageNumber] = data;
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
        }
      });
    } else {
      this.simulationService.getAllVendors(params).subscribe({
        next: (data) => {
          this.vendorData[pageNumber] = data;
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
        }
      });
    }

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

    if (this.simulationType === 'customer') {
      this.simulationService.addCustomers(this.isVip, this.repetitionCount).subscribe({
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

    } else {
      this.simulationService.addVendors(this.repetitionCount).subscribe({
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
  }

  startEntity() {
    if (this.isLoading) return;

    this.isLoading = true;

    if (this.simulationType === 'customer') {
      this.simulationService.startCustomer().subscribe({
        next: () => this.loadInitialData(),
        error: (error) => {
          console.error('Error starting entity:', error);
        },
        complete: () => {
          this.isLoading = false;
        }
      });
    } else {
      this.simulationService.startVendor().subscribe({
        next: () => this.loadInitialData(),
        error: (error) => {
          console.error('Error starting entity:', error);
        },
        complete: () => {
          this.isLoading = false;
        }
      });
    }
  }

  stopEntity() {
    if (this.isLoading) return;

    this.isLoading = true;

    if (this.simulationType === 'customer') {
      this.simulationService.stopCustomer().subscribe({
        next: () => this.loadInitialData(),
        error: (error) => {
          console.error('Error stopping entity:', error);
        },
        complete: () => {
          this.isLoading = false;
        }
      });
    } else {
      this.simulationService.stopVendor().subscribe({
        next: () => this.loadInitialData(),
        error: (error) => {
          console.error('Error stopping entity:', error);
        },
        complete: () => {
          this.isLoading = false;
        }
      });
    }
  }

  isCustomer(entity: CustomerDto | VendorDto): entity is CustomerDto {
    return 'type' in entity;
  }
}
