export interface Link {
  shortCode: string;
  shortUrl: string;
  originalUrl: string;
  createdAt: Date;
  expiresAt?: Date;
  accessCount?: number;
  lastAccessedAt?: string;
  title?: string;
  isCustom?: boolean;
  disabled?: boolean;
}