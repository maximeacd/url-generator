import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ShortenFormComponent } from './components/shorten-form/shorten-form.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, ShortenFormComponent],
  template: `
    <div class="container mt-5">
      <app-shorten-form></app-shorten-form>
    </div>
  `
})
export class AppComponent {}