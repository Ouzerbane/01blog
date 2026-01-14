import { ErrorHandler, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class GlobalErrorHandler implements ErrorHandler {
  constructor(private snackBar: MatSnackBar, private router: Router) { }

  handleError(error: any): void {

    // HTTP ERROR
    // console.log("error =====", error);

    if (error.status) {

      switch (error.status) {
        case 0:
          this.snackBar.open('Network error: Please check your internet connection.', 'Close', { duration: 5000, panelClass: ['error-snackbar'] });
          break;

        case 400:
          this.snackBar.open(`Bad Request: ${error.error.errors[0].message}`, 'Close', { duration: 5000, panelClass: ['error-snackbar'] });
          break;

        case 401:
          this.snackBar.open(`Unauthorized: ${error.error.errors[0].message}`, 'Close', { duration: 5000, panelClass: ['error-snackbar'] });
          break;

        case 403:
          if (error.error.errors[0].message === 'User not found' || error.error.errors[0].message === "User is banned") {
            this.router.navigate(["/login"])
          }
          this.snackBar.open(`Forbidden: ${error.error.errors[0].message}`, 'Close', { duration: 5000, panelClass: ['error-snackbar'] });
          break;

        case 404:
          this.snackBar.open(`Not Found: ${error.error.errors[0].message}`, 'Close', { duration: 5000, panelClass: ['error-snackbar'] });
          break;

        case 500:
          this.snackBar.open('Internal Server Error: Please try again later.', 'Close', { duration: 5000, panelClass: ['error-snackbar'] });
          break;

        default:
          this.snackBar.open('Unexpected error', 'Close', { duration: 5000, panelClass: ['error-snackbar'] });
      }

    }
    // JS ERROR
    else {
      this.snackBar.open('An unexpected error occurred. Please try again.', 'Close', { duration: 5000, panelClass: ['error-snackbar'] });
    }
  }
}