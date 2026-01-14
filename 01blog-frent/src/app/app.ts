import { Component, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Header } from './features/header/header';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,Header],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('01blog-frent');

  constructor(private router:Router){}

  isAuthPage(): boolean {
  const currentPath = this.router.url.split('?')[0];

  const allowedRoutes = this.router.config
    .filter(r =>
      r.path &&
      r.path !== 'login' &&
      r.path !== 'register' &&
      r.path !== '**'
    )
    .map(r => '/' + r.path);
    allowedRoutes.push("/")
    
  if (currentPath.startsWith('/profile/')) {
    return true;
  }

  return allowedRoutes.includes(currentPath);
}

}
