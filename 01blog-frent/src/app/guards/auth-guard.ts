import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Guard } from '../services/guard';
import { catchError, map, of } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const guardService = inject(Guard);

  return guardService.canActivate().pipe(
    map((res) => {
      router.navigate(['/']);
      return false;
    }),
    catchError((err) => {
      return of(true);
    })
  );
};
