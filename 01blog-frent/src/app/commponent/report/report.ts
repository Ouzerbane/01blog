import { Component, EventEmitter, Output, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardContent, MatCardActions, MatCardSubtitle, MatCardHeader, MatCardTitle, MatCard, MatCardModule } from "@angular/material/card";
import { MatDivider, MatDividerModule } from "@angular/material/divider";
import { MatFormField, MatFormFieldModule, MatLabel } from "@angular/material/form-field";
import { MatIcon, MatIconModule } from "@angular/material/icon";
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-report',
  imports: [  MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatDividerModule],
  templateUrl: './report.html',
  styleUrl: './report.css',
})
export class Report {
  reasons = signal("");

  @Output() reportSubmitted = new EventEmitter<{message: string, confirmed: boolean}>();
  constructor(private snackBar: MatSnackBar) { }

  submitReport() {
    if(this.reasons().trim() === "" ||this.reasons().trim().length < 5  || this.reasons().trim().length > 200) {
      this.snackBar.open('Please provide a reason for the report be between 5 and 200 characters.', 'Close', { duration: 3000 });
      return;
    }
    this.reasons.set(this.reasons().trim());
    this.reportSubmitted.emit({message: this.reasons(), confirmed: true});
  }

  cancelReport() {
    this.reportSubmitted.emit({message: '', confirmed: false});
  }


}
