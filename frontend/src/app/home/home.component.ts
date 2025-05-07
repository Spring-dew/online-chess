import { Component } from '@angular/core';
import {auth} from '../globals';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  title = 'Online Chess';

  notLogged()  {return auth.userID == null;}

}
