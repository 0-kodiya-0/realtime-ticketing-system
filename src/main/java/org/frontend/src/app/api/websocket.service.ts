import {Injectable, OnDestroy, OnInit} from '@angular/core';
import {Client, Message, StompSubscription} from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {Observable, Subject} from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private stompClient?: Client;
  private socketRetryCount: number = 0;
  private readonly maxSocketRetries: number = 3;
  private readonly socketRetryDelay: number = 3000;

  // Connect to WebSocket server
  connect(): Promise<void> {
    return new Promise((resolve, reject) => {
      try {
        this.stompClient = new Client({
          webSocketFactory: () => new SockJS(`${environment.apiUrl}/ws`),
          debug: (str) => console.log(str),
          reconnectDelay: this.socketRetryDelay,
          heartbeatIncoming: 4000,
          heartbeatOutgoing: 4000
        });
        this.stompClient.onConnect = () => {
          console.log('WebSocket connection established');
          this.socketRetryCount = 0;
          resolve();
        };
        this.stompClient.onStompError = (frame) => {
          const error = new Error(`WebSocket error: ${frame.headers['message']}`);
          reject(error);
          this.handleConnectionError(error);
        };
        this.stompClient.activate();
      } catch (error) {
        reject(error);
        this.handleConnectionError(error as Error);
      }
    });
  }

  // Handle connection errors and implement retry logic
  private handleConnectionError(error: Error): void {
    console.error('WebSocket connection error:', error);
    if (this.socketRetryCount < this.maxSocketRetries) {
      this.socketRetryCount++;
      console.log(`Attempting to reconnect (${this.socketRetryCount}/${this.maxSocketRetries})`);
      setTimeout(() => {
        this.connect().catch(err => {
          console.error('Reconnection attempt failed:', err);
        });
      }, this.socketRetryDelay);
    } else {
      console.error('Maximum reconnection attempts reached');
    }
  }

  // Subscribe to a WebSocket topic
  subscribeWebSocketPath<T>(topic: string, callback: (message: Message) => void): StompSubscription {
    if (!this.stompClient?.connected) {
      throw new Error('WebSocket not connected');
    }
    return this.stompClient.subscribe(topic, callback);
  }

  subscribePathSummery<T>(event: string, callback: (message: JSON | null, error: any) => void): StompSubscription {
    return this.subscribeWebSocketPath(`/summery/${event}`, (message: Message) => {
      try {
        const data = JSON.parse(message.body);
        callback(data, null);
      } catch (error) {
        console.error('Error parsing message:', error);
        callback(null, error);
      }
    });
  }

  // Send message to a WebSocket topic
  send(topic: string, message: any): void {
    if (!this.stompClient?.connected) {
      throw new Error('WebSocket not connected');
    }
    try {
      this.stompClient.publish({
        destination: topic,
        body: JSON.stringify(message)
      });
    } catch (error) {
      console.error('Error sending message:', error);
      throw error;
    }
  }

  // Disconnect from WebSocket server
  disconnect(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (!this.stompClient?.connected) {
        resolve();
        return;
      }
      try {
        this.stompClient.deactivate()
          .then(() => {
            console.log('WebSocket connection closed');
            resolve();
          })
          .catch(error => {
            console.error('Error closing connection:', error);
            reject(error);
          });
      } catch (error) {
        reject(error);
      }
    });
  }

  // Check connection status
  isConnected(): boolean {
    return this.stompClient?.connected ?? false;
  }

  // Get connection state as observable
  onConnectionChange(): Observable<boolean> {
    const subject = new Subject<boolean>();
    if (this.stompClient) {
      this.stompClient.onConnect = () => {
        subject.next(true);
      };

      this.stompClient.onDisconnect = () => {
        subject.next(false);
      };
    }
    return subject.asObservable();
  }
}
