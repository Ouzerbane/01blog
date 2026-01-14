import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatCard } from "@angular/material/card";

@Component({
  selector: 'app-confirm-profile',
  imports: [MatCard],
  templateUrl: './confirm-profile.html',
  styleUrl: './confirm-profile.css',
})
export class ConfirmProfile {
  @Output() Confirm = new EventEmitter<{ obj: any; confirmed: boolean }>();

  @Input() message!: string;
  @Input() obj!: any;

  confirm() {
    this.Confirm.emit({ obj: this.obj, confirmed: true });
  }
  cancel() {
    this.Confirm.emit({ obj: this.obj, confirmed: false });
  }
}
//  <div class="modal-overlay">
//           <mat-card class="confirm-modal">
//             <h3>Confirmation</h3>
//             <p>Are you sure you want to remove this post?</p>
//             <div class="modal-actions">
//               <button mat-button (click)="cancelRemoveProfile(report)">Cancel</button>
//               <button mat-raised-button color="warn" (click)="activRemoveProfile(report)">
//                 Confirm
//               </button>
//             </div>
//           </mat-card>
//         </div>