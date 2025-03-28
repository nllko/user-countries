import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserListComponent} from './components/user-list/user-list.component';
import {FavouriteCountriesComponent} from "./components/favourite-countries/favourite-countries.component";

const routes: Routes = [
  {
    path: '',
    component: UserListComponent
  },
  {
    path: ':id/favourite-countries',
    component: FavouriteCountriesComponent
  }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class UsersRoutingModule {
}
