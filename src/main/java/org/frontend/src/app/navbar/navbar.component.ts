import {Component, OnDestroy, OnInit} from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {SimulationService} from '../api/simulation.service';
import {WebsocketService} from '../api/websocket.service';
import {SummarySimulation} from '../dto/websocket.dto';
import {StompSubscription} from '@stomp/stompjs';

@Component({
  selector: 'app-navbar',
  imports: [
    RouterLinkActive,
    RouterLink
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit, OnDestroy {
  isSimulationRunning = false;
  private webSocketSubscription: StompSubscription | undefined;

  constructor(private simulationService: SimulationService, private websocketService: WebsocketService) {
    this.websocketService.connect().then(value => {
      this.subscribeWebSocket()
      this.websocketService.onConnectionChange().subscribe(value => {
        if (value && this.webSocketSubscription == null) {
          this.subscribeWebSocket();
        } else {
          if (this.webSocketSubscription !== undefined) {
            this.webSocketSubscription.unsubscribe();
          }
        }
      });
    });
  }

  subscribeWebSocket() {
    this.webSocketSubscription = this.websocketService.subscribePathSummery("simulation", message => {
      if (message != null) {
        const newMessage: SummarySimulation = message as unknown as SummarySimulation;
        if (newMessage.activeCustomerCount > 0 && newMessage.activeVendorCount > 0) {
          this.isSimulationRunning = true;
        }
      }
    });
  }

  isRunning() {
    this.simulationService.isRunning().subscribe({
        next: (response) => {
          this.isSimulationRunning = response;
          console.log('Simulation is running');
        },
        error: (error) => {
          console.log('Simulation is not running');
        }
      }
    );
  }

  async startSimulation() {
    if (!this.isSimulationRunning) {
      this.simulationService.startSimulation().subscribe({
        next: (response) => {
          if (response > 0) {
            this.isSimulationRunning = true;
          } else {
            this.isRunning();
          }
          console.log('Simulation started:', response);
        },
        error: (error) => {
          this.isRunning();
        }
      });
    }
  }

  async stopSimulation() {
    if (this.isSimulationRunning) {
      this.simulationService.stopSimulation().subscribe({
        next: (response) => {
          if (response > 0) {
            this.isSimulationRunning = false;
          }
          console.log('Simulation stopped:', response);
        },
        error: (error) => {
          console.error('Error stopping simulation:', error);
        }
      });
    }
  }

  ngOnInit(): void {
    this.isRunning();
    if (this.webSocketSubscription === undefined) {
      this.subscribeWebSocket();
    }
  }

  ngOnDestroy(): void {
    if (this.webSocketSubscription !== undefined) {
      this.webSocketSubscription.unsubscribe();
    }
  }
}
