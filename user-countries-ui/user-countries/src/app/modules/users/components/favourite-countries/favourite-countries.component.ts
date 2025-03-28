import {Component, Input, OnInit} from '@angular/core';
import {UserWebclientService} from "../../services/user-webclient.service";
import {Country} from "../../models/country";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-favourite-countries',
  templateUrl: './favourite-countries.component.html',
  styleUrls: ['./favourite-countries.component.scss']
})
export class FavouriteCountriesComponent implements OnInit {
  userId: any = '';
  countries: Country[] = [];
  loading: boolean = true;

  constructor(private readonly userService: UserWebclientService, private router: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.router.params.subscribe(params => {
      this.userId = params['id'];
    })
    this.loadCountries();
  }

  private loadCountries(): void {
    this.userService.getFavouriteCountries(this.userId).subscribe({
      next: (countries) => {
        this.countries = countries;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading countries:', error);
        this.loading = false;
      },
    });
  }
}
