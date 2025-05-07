import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import SockJS from 'sockjs-client';
import Stomp, {Client, Subscription} from 'stompjs';
import {auth, gamedtls} from '../globals';
import {GameMode, GameModeMapping} from '../mode-select/mode-select.component';
import {Observable, Observer, Subject} from 'rxjs';
import {Router} from '@angular/router';
// import * as Stomp from 'stompjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  protected stompClient: Client | null = null;
  protected socket: WebSocket | null = null;
  private connectedWebSockets: boolean = false;
  private foundGameOpponent: boolean = false;
  private currentGame: Subscription | undefined;
  private gameStateFromBackend!: Observable<Record<string, any>>;

  constructor(private http: HttpClient, private router: Router) {
  }

  getXSRFTOKEN(): Record<string, string> {
    let token = "";
    let name = "XSRF-TOKEN" + "=";
    let ca = document.cookie.split(';');
    for (let i = 0; i < ca.length; i++) {
      let c = ca[i];
      while (c.charAt(0) == ' ') {
        c = c.substring(1);
      }
      if (c.indexOf(name) == 0) {
        token = c.substring(name.length, c.length);
      }
    }
    return {"X-XSRF-TOKEN": token};
  }

  connect(): Observable<any> {

    return new Observable(observer => {
      this.socket = new SockJS('/online-chess-game');
      this.stompClient = Stomp.over(this.socket);
      this.stompClient.connect(this.getXSRFTOKEN(), (frame) => {
        this.connectedWebSockets = true;
        // subscribe to topic and create the callback function that handles updates from the server
        // subscribe to self for updates about Game ID and then subscribe to Game ID to start game.
        this.stompClient?.subscribe(`/topic/user/${auth.userID}`, (game) => {
          let gameID: any = JSON.parse(game.body).gameID as string;
          let returnData: any = JSON.parse(game.body) as string;
          gamedtls.gameID = gameID;
          gamedtls.color = returnData[auth.userID!];
          this.foundGameOpponent = true;
          this.router.navigate(['/game']);
        }, this.getXSRFTOKEN());

        // subscribe to all game modes
        this.stompClient?.subscribe(`/topic/game-mode/blitz`, (data) => {
        }, this.getXSRFTOKEN());

        this.stompClient?.subscribe(`/topic/game-mode/bullet`, (data) => {
        }, this.getXSRFTOKEN());

        this.stompClient?.subscribe(`/topic/game-mode/rapid`, (data) => {
        }, this.getXSRFTOKEN());

        this.stompClient?.subscribe(`/topic/game-mode/classic`, (data) => {
        }, this.getXSRFTOKEN());

        // subscribe to error
        this.stompClient?.subscribe('/topic/errors', (data) => {
        }, this.getXSRFTOKEN());

      }, (data) => {
      });

    });
  }

  gameConnect() {
    this.currentGame = this.stompClient?.subscribe(`/topic/game/${gamedtls.gameID}`, (game) => {
    }, this.getXSRFTOKEN());
  }

  getStompClient() {
    return this.stompClient;
  }

  gameDisconnect() {
    this.currentGame?.unsubscribe();
    this.foundGameOpponent = false;
  }

  isConnected(): boolean {
    return this.stompClient?.connected == true;
  }

  disconnect(): void {
    if (this.stompClient !== null) {
      this.stompClient.disconnect(() => {
      }, this.getXSRFTOKEN());
    }
    this.connectedWebSockets = false;
  }

  makeMove(fen: string): void {
    this.stompClient?.send(`/app/game/${gamedtls.gameID}`, {}, JSON.stringify({'fen': fen}));
  }

  // publish userID for in Game Mode channel to find opponent
  // once found Game ID will be returned in user channel
  publishUserIDForGameMode(mode: GameMode): void {
    this.stompClient?.send(`/app/game-mode/${GameModeMapping[mode]}`, {},
      JSON.stringify({'userID': auth.userID}));
  }

}
