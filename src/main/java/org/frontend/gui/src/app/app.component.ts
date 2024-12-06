import {Component} from '@angular/core';
import {NavbarComponent} from './navbar/navbar.component';
import {FootorComponent} from './footor/footor.component';
import {RouterModule} from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [
    NavbarComponent,
    FootorComponent,
    RouterModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'gui';
}
