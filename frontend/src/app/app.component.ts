import { Component } from '@angular/core';
import {LoginService} from './services/login.service';
import {Router} from '@angular/router';
import {auth, gamedtls} from './globals';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Online Chess';

  constructor(private loginService: LoginService, private router: Router) {

  }

  logout():void {
    this.loginService.logout().subscribe(
      ()=>{
        sessionStorage.clear();
        auth.userID = null;
        auth.token = null;
        gamedtls.gameID = null;
        gamedtls.color = null;
        this.router.navigate(['/home']);}
    );
  }
}
