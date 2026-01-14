// header.component.ts
import { CommonModule } from '@angular/common';
import { Component, OnInit, Output, Signal, signal } from '@angular/core';
import { MatBadgeModule } from '@angular/material/badge';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { HeaderService } from '../../services/header-service';
import { Router, RouterLink } from '@angular/router';
import { NotificationModul } from '../../modules/hreader-moudel/hreader-moudel-module';
import { GlobalInfo } from '../../services/global-info';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatMenuModule,
    MatBadgeModule,
    RouterLink
  ],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit {
  notificationCount = signal(0);
  showNotifacation = signal(false);
  notifications = signal<NotificationModul[]>([]);

  userType= signal(false);

  @Output() notificationRead: Signal<string> = signal('');

  constructor(private hreaderService: HeaderService,
     private router: Router,public globalInfo: GlobalInfo) { }


  ngOnInit(): void {
    this.GetuserType();
    this.getCurnuser();
    this.CountNotification();
  }

  logout() {
    this.hreaderService.logout().subscribe({
      next: () => this.router.navigate(['/login']),
      error: (err) => this.router.navigate(["/login"])
    })
  }

  CountNotification() {
    this.hreaderService.CountNotification().subscribe({
      next: (req) => {
        this.notificationCount.set(req.data)
      }
    })
  }

  OpenNotifination() {
    this.showNotifacation.set(!this.showNotifacation());
    if (this.showNotifacation()) {
      this.GetNotification()
    }
  }

  GetNotification() {
    this.hreaderService.GetNotification().subscribe({
      next: (res) => {
        this.notifications.set(res.data)
      }
    })
  }


  NotificationRead(id: string) {
    this.hreaderService.NotificationRead(id).subscribe({
      next: (res) => {
        this.notifications.update(list =>
          list.map(item =>
            item.id === id ? res.data : item
          )
        );
        this.CountNotification()
      }
    })
  }

  getCurnuser() {
    this.hreaderService.getCurrentUserProfile().subscribe({
      next: (req) => {
        this.globalInfo.idUser = req.data.id;
      }
    })
    
  }

    GetuserType() {
    this.hreaderService.GetuserTypeService().subscribe({
      next: (res) => {
        this.userType.set(res.data)
        console.log(res);
        
      }
    })
  }


}