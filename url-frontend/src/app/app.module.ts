import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';

// For standalone components, import them instead of declaring
import { ShortenFormComponent } from './components/shorten-form/shorten-form.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppComponent,
    ShortenFormComponent,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }