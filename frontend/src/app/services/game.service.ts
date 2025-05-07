import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  private getMovesUrl = '/api/game/getMoves';
  private makeMoveUrl = '/api/game/makeMove';

  constructor(private http: HttpClient) {
  }

  getMoveList(data: string): Observable<Record<string, any>> {
    // return this.http.get(this.loginUrl, this.httpOptions);
    return this.http.post<Record<string, any>>(this.getMovesUrl, data);
  }


  makeMove(fen: string):Observable<Record<string, any>> {
    return this.http.post<Record<string, any>>(this.makeMoveUrl, fen);
  }
}
