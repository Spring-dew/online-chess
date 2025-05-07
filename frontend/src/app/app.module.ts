import {importProvidersFrom, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {GameComponent} from './game/game.component';
import {HomeComponent} from './home/home.component';
import {LoginComponent} from './login/login.component';
import {ReactiveFormsModule} from '@angular/forms';
import {
  HTTP_INTERCEPTORS,
  HttpClientModule,
  // HttpClientXsrfModule,
  provideHttpClient, withInterceptors, withInterceptorsFromDi,
  withXsrfConfiguration
} from '@angular/common/http';
import {AuthInterceptor} from './AuthInterceptor';
import { ModeSelectComponent } from './mode-select/mode-select.component';

@NgModule({
  declarations: [
    AppComponent,
    GameComponent,
    HomeComponent,
    LoginComponent,
    ModeSelectComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    // HttpClientModule
  ],
  providers: [

    provideHttpClient(
      withInterceptorsFromDi(), withXsrfConfiguration({
        cookieName: 'XSRF-TOKEN',
        headerName: 'X-XSRF-TOKEN',
      }),
    ),
    {
      provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
