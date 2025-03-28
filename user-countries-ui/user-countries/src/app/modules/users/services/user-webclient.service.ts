import {Injectable} from '@angular/core';
import {BaseWebclientService} from '../../shared/services/base-webclient.services';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../models/user';
import {Country} from "../models/country";

@Injectable({
  providedIn: 'root',
})
export class UserWebclientService extends BaseWebclientService {
  private readonly URI: string = 'user';

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  public getAllUsers(): Observable<User[]> {
    return this.get<User[]>(this.URI);
  }

  public getFavouriteCountries(id: string): Observable<Country[]> {
    return this.get<Country[]>(this.URI + '/' + id + '/favourite-countries')
  }
}
