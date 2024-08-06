import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideHttpClient, withInterceptorsFromDi} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient(withInterceptorsFromDi())
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

/*Possible error might use HttpModuleClient*/
