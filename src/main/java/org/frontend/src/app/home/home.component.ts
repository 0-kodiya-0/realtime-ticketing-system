import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgForOf} from '@angular/common';
import {WebsocketService} from "../api/websocket.service";
import {SummaryPool, SummarySimulation} from "../dto/websocket.dto";
import {StompSubscription} from "@stomp/stompjs";

interface SummaryData {
  title: string;
  total: number;
  primaryLabel: string;
  secondaryLabel: string;
  primaryValue: number;
  secondaryValue: number;
}

@Component({
  selector: 'app-home',
  imports: [
    NgForOf
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit , OnDestroy {

  constructor(private websocketService: WebsocketService) {
  }
  private subscriptions: StompSubscription[] = [];
  private beforeTicketPoolSize: number = 0;
  private beforePurchasePoolSize: number = 0;

  summaries: SummaryData[] = [
    {
      title: 'Threads',
      total: 0,
      primaryLabel: 'Active Threads',
      secondaryLabel: 'Non-Active',
      primaryValue: 0,
      secondaryValue: 0
    },
    {
      title: 'Vendors',
      total: 0,
      primaryLabel: 'Active Vendors',
      secondaryLabel: 'Inactive',
      primaryValue: 0,
      secondaryValue: 0
    },
    {
      title: 'Customers',
      total: 0,
      primaryLabel: 'Active Customers',
      secondaryLabel: 'Inactive',
      primaryValue: 0,
      secondaryValue: 0
    },
    {
      title: 'Ticket Pool',
      total: 0,
      primaryLabel: 'Tickets',
      secondaryLabel: 'Removed',
      primaryValue: 0,
      secondaryValue: 0
    },
    {
      title: 'Purchase Pool',
      total: 0,
      primaryLabel: 'Purchases',
      secondaryLabel: 'Removed',
      primaryValue: 0,
      secondaryValue: 0
    }
  ];

  ngOnInit() {
    this.websocketService.connect().then(value => {
      this.subscriptions = this.subscribeWebSockets()
      this.websocketService.onConnectionChange().subscribe(value => {
        if (value && this.subscriptions.length <= 0) {
          this.subscriptions = this.subscribeWebSockets();
        } else {
          this.subscriptions.forEach(s => s.unsubscribe());
          this.subscriptions = [];
        }
      });
    });
  }

  subscribeWebSockets(): StompSubscription[] {
    const subscriptions: StompSubscription[] = [];
    const simulation = this.websocketService.subscribePathSummery("simulation", message => {
      if (message != null){
        const newMessage: SummarySimulation = message as unknown as SummarySimulation;
        this.updateSummarySimulation("Threads", newMessage);
        this.updateSummarySimulation("Vendors", newMessage);
        this.updateSummarySimulation("Customers", newMessage);
      }
    });
    const pool = this.websocketService.subscribePathSummery("pool", message => {
      if (message != null){
        const newMessage: SummaryPool = message as unknown as SummaryPool;
        this.updateSummaryPool("Ticket Pool", newMessage);
        this.updateSummaryPool("Purchase Pool", newMessage);
      }
    });
    subscriptions.push(simulation);
    subscriptions.push(pool);
    return subscriptions;
  }

  updateSummarySimulation(type: string, data: SummarySimulation) {
    const summary = this.summaries.find(s => s.title === type);
    if (summary) {
      switch (type) {
        case 'Threads':
          summary.primaryValue = data.activeCustomerCount + data.activeVendorCount;
          summary.secondaryValue = (data.customerCount + data.vendorCount) - (data.activeCustomerCount + data.activeVendorCount);
          summary.total = data.customerCount + data.vendorCount;
          break;
        case 'Vendors':
          summary.primaryValue = data.activeVendorCount;
          summary.secondaryValue = data.vendorCount - data.activeVendorCount;
          summary.total = data.vendorCount;
          break;
        case 'Customers':
          summary.primaryValue = data.activeCustomerCount;
          summary.secondaryValue = data.customerCount - data.activeCustomerCount;
          summary.total = data.customerCount;
          break;
      }
    }
  }
  updateSummaryPool(type: string, data: SummaryPool) {
    const summary = this.summaries.find(s => s.title === type);
    if (summary) {
      switch (type) {
        case 'Ticket Pool':
          if (this.beforeTicketPoolSize < data.ticketPoolSize){
            summary.primaryValue = data.ticketPoolSize - this.beforeTicketPoolSize;
            summary.secondaryValue = 0;
          } else {
            summary.primaryValue = 0;
            summary.secondaryValue = +(this.beforeTicketPoolSize - data.ticketPoolSize);
          }
          summary.total = data.ticketPoolSize;
          break;
        case 'Purchase Pool':
          if (this.beforePurchasePoolSize < data.purchasePoolSize){
            summary.primaryValue = data.purchasePoolSize - this.beforePurchasePoolSize;
            summary.secondaryValue = 0;
          } else {
            summary.primaryValue = 0;
            summary.secondaryValue = +(this.beforePurchasePoolSize - data.purchasePoolSize);
          }
          summary.total = data.purchasePoolSize;
          break;
      }
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
