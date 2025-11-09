import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Regester } from './regester/regester';
import { Dashboard } from './dashboard/dashboard';
import { Posts } from './posts/posts';

export const routes: Routes = [{path:"",component:Dashboard},{ path: "", redirectTo: "", pathMatch: "full"},
{path:"login",component:Login},{path:"regester",component:Regester},{path:"post",component:Posts},
{ path: "**", redirectTo: "/login" }];
