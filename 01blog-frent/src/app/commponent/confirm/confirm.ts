import { Component, EventEmitter, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-confirm',
  imports: [MatDialogModule, MatButtonModule, MatIconModule],
  templateUrl: './confirm.html',
  styleUrl: './confirm.css',
})
export class Confirm {
  @Output() Confirm = new EventEmitter<boolean>();


  confirm() {
    this.Confirm.emit(true);
  }
  cancel() {
    this.Confirm.emit(false);
  }
}
