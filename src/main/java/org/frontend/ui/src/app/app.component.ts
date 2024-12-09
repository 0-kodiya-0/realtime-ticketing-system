import {Component, OnDestroy, OnInit} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Subscription} from 'rxjs';
import {WebSocketService} from './websocket.service';
import {NgIf} from '@angular/common';

// Define an interface for your data structure
interface WebSocketData {
  // Add properties based on your actual data structure
  // For example:
  message?: string;
  timestamp?: number;
  // Add other properties as needed
}

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NgIf],
  providers: [WebSocketService],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy  {
  title = 'ui';
  receivedData: WebSocketData | null = null;
  private subscription: Subscription | undefined;

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {
    this.webSocketService.connect();
    this.subscription = this.webSocketService.getData().subscribe({
      next: (data: WebSocketData) => {
        this.receivedData = data;
      },
      error: (error: any) => {
        console.error('WebSocket error:', error);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.webSocketService.disconnect();
  }
}
