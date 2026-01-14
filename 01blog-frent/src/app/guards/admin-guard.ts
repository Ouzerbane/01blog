import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Guard } from '../services/guard';
import { catchError, map, of } from 'rxjs';

export const adminGuard: CanActivateFn = (route, state) => {
 const router = inject(Router);
  const guardService = inject(Guard);

  return guardService.canActivateAdmin().pipe(
      map((res) => {
        // router.navigate(['/']);
        return true;
      }),
      catchError((err) => {
        router.navigate(['/']);
        return of(false);
      })
    );
};
