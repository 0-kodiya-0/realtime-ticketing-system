import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

interface WebSocketData {
  message?: string;
  timestamp?: number;
  // Add other properties as needed
}

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient: Client;
  private serverUrl = 'http://localhost:8080/ws';
  private topic = '/topic/tickets';

  constructor() {
    this.stompClient = new Client();
  }

  connect(): void {
    this.stompClient = new Client({
      webSocketFactory: () => new SockJS(this.serverUrl),
      onConnect: () => {
        console.log('Connected to WebSocket');
      },
      onDisconnect: () => {
        console.log('Disconnected from WebSocket');
      }
    });

    this.stompClient.activate();
  }

  getData(): Observable<WebSocketData> {
    return new Observable(observer => {
      this.stompClient.subscribe(this.topic, (message: Message) => {
        try {
          const data: WebSocketData = JSON.parse(message.body);
          observer.next(data);
        } catch (error) {
          observer.error(error);
        }
      });
    });
  }

  disconnect(): void {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }
}
