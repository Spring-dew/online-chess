import {Injectable} from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpResponse,
} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {auth} from './globals';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor() {
  }

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let authRequest = request;
    if (auth.token) {
      // Clone the request and attach the token
      authRequest = request.clone({
        setHeaders: {
          Authorization: `Bearer ${auth.token}`
        }
      });
    }
      return next.handle(authRequest);

  }
}
