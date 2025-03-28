import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsersRoutingModule } from './users-routing.module';
import { SharedModule } from '../shared/shared.module';
import { UserListComponent } from './components/user-list/user-list.component';
import {FavouriteCountriesComponent} from "./components/favourite-countries/favourite-countries.component";

@NgModule({
  declarations: [UserListComponent, FavouriteCountriesComponent],
  imports: [CommonModule, UsersRoutingModule, SharedModule],
})
export class UsersModule {}
