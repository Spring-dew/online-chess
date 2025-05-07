import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private loginUrl = '/api/auth/userlogin';  // URL to web api
  private signupUrl = '/api/auth/signup';
  private logoutUrl = '/api/auth/logout';

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'}),
    observe: "response" as 'body'
  };

  constructor(private http: HttpClient) {
  }


  login(data: any): Observable<any> {
    // return this.http.get(this.loginUrl, this.httpOptions);
    return this.http.post(this.loginUrl, data, this.httpOptions);
  }

  signup(data: any): Observable<any> {
    // return this.http.get(this.signupUrl, this.httpOptions);
    return this.http.post(this.signupUrl, data, this.httpOptions);

  }

  logout(): Observable<any> {
    return this.http.get(this.logoutUrl, {
      responseType: 'text'
    });
  }
}
