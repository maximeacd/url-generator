import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LinkService } from '../../services/link.service';
import { Link } from '../../models/link.model';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-shorten-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './shorten-form.component.html',
  styleUrls: ['./shorten-form.component.css']
})
export class ShortenFormComponent {
  url = '';
  customAlias?: string;
  expiresAt?: string;

  result: Link | null = null;
  loading = false;

  constructor(private linkService: LinkService, private cdr: ChangeDetectorRef) {}

  submit() {
    if (!this.url) {
      alert('URL is required');
      return;
    }

    this.loading = true;

    let expiresAtISO: string | undefined;
    if (this.expiresAt) {
      const d = new Date(this.expiresAt);
      expiresAtISO = d.toISOString();
    }

    this.linkService.shorten(this.url, this.customAlias, expiresAtISO)
      .pipe(
        map(link => ({
          ...link,
          createdAt: new Date(link.createdAt),
          expiresAt: link.expiresAt ? new Date(link.expiresAt) : undefined
        }))
      )
      .subscribe({
        next: (link) => {
          this.result = link;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error(err);
          this.loading = false;
          this.cdr.detectChanges();
          alert(err?.error?.message || 'Failed to shorten URL');
        }
      });
  }

  copyToClipboard(url?: string) {
    if (!url) return;
    navigator.clipboard.writeText(url)
      .then(() => alert('Copied to clipboard!'))
      .catch(() => alert('Failed to copy'));
  }

  backToForm() {
    this.result = null;
    this.url = '';
    this.customAlias = undefined;
    this.expiresAt = undefined;
  }
}