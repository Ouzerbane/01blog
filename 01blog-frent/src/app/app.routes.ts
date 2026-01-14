import { Routes } from '@angular/router';
import { Login } from './features/login/login';
import { Register } from './features/register/register';
import { authGuard } from './guards/auth-guard';
import { CreatPost } from './commponent/creat-post/creat-post';
import { autherGuard } from './guards/auther-guard';
import { Homme } from './features/homme/homme';
import { Profile } from './features/profile/profile';
import { Admin } from './features/admin/admin';
import { adminGuard } from './guards/admin-guard';
import { NotFund } from './commponent/not-found/not-fund';

export const routes: Routes = [
    {path:"login",component:Login,canActivate: [authGuard]},
    {path:"register",component:Register,canActivate: [authGuard]},
    {path:"creat-post",component:CreatPost,canActivate: [autherGuard] },
    // {path:"home",loadChildren:()=>import('./modules/home/home-module').then(m=>m.HomeModule)}
    {path:"",component:Homme,canActivate: [autherGuard]},
     {path: 'profile/:id',component:Profile,canActivate: [autherGuard]},
     {path: 'profile',component:Profile,canActivate: [autherGuard]},
     {path: 'admin',component:Admin,canActivate: [autherGuard , adminGuard]},
     { path: '**', component: NotFund }


    // , canActivate: [autherGuard]
];
//   { path: '', redirectTo: 'login', pathMatch: 'full' },
