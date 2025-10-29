import { Routes } from '@angular/router';
import { Register } from './modules/auth/register/register';
import { Login } from './modules/auth/login/login';

export const routes: Routes = [{ path: 'register', component: Register },
    { path: 'login', component: Login }
];
