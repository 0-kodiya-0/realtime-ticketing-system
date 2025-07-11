import { Routes } from '@angular/router';
import {PoolComponent} from './pool/pool.component';
import {SimulationComponent} from './simulation/simulation.component';
import {HomeComponent} from './home/home.component';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  {path: 'pool', component: PoolComponent},
  { path: 'simulation', component: SimulationComponent},
];
