import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {WebsocketService} from '../services/websocket.service';

@Component({
  selector: 'app-mode-select',
  standalone: false,
  templateUrl: './mode-select.component.html',
  styleUrl: './mode-select.component.css'
})
export class ModeSelectComponent implements OnInit {
  selectMode: boolean = true;
  findOpponent: boolean = false;
  selectedMode: number = 1;

  constructor(private router: Router, private websocketService: WebsocketService) {  }

  ngOnInit() {
    this.selectMode = true;
    this.findOpponent = false;
    this.selectedMode = 1;
  }


  findOpponents(mode : number) {
    this.selectedMode = mode;
    this.selectMode = false;
    this.findOpponent = true;
    this.websocketService.connect().subscribe();
    setTimeout(()=> {
      // this.messageSuccess = false;
      this.websocketService.publishUserIDForGameMode(this.selectedMode);
    }, 3000);
    // this.websocketService.publishUserIDForGameMode(this.selectedMode);
  }

  gotogametemp() {
    // TODO: to be deleted
    this.selectMode = false;
    this.findOpponent = false;
    this.router.navigate(['/game']);
  }
}

export enum GameMode {
  BLITZ,
  BULLET,
  RAPID,
  CLASSIC
}

export const GameModeMapping = {
  0 : 'blitz',
  1 : 'bullet',
  2 : 'rapid',
  3 : 'classic'
}
