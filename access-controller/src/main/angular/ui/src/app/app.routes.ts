import {Routes} from '@angular/router';
import {UsersComponent} from "./users/users.component";
import {LoggedOutComponent} from "./logged-out/logged-out.component";

export const routes: Routes = [
  {path: 'users', component: UsersComponent},
  {path: 'logged-out', component: LoggedOutComponent},
  {path: '', redirectTo: '/users', pathMatch: 'full'},
];
