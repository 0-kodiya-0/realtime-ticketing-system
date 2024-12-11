import {Component, OnInit} from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {SimulationService} from '../api/simulation.service';

@Component({
  selector: 'app-navbar',
  imports: [
    RouterLinkActive,
    RouterLink
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {
  isSimulationRunning = false;

  constructor(private simulationService: SimulationService) {
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
  }
}
