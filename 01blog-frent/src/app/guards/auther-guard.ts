import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Guard } from '../services/guard';
import { catchError, map, of } from 'rxjs';

export const autherGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const guardService = inject(Guard);

  return guardService.canActivateAuther().pipe(
    map((res) => {
      
      return true;
    }),
    catchError((err) => {
      router.navigate(['/login']);
      return of(false);
    })
  );
};
