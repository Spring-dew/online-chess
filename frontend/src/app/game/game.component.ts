import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {GameService} from '../services/game.service';
import SockJS from 'sockjs-client';
import Stomp, {Message} from 'stompjs';
import {WebsocketService} from '../services/websocket.service';
import {Observable} from 'rxjs';
import {gamedtls} from '../globals';
import {Router} from '@angular/router';

@Component({
  selector: 'app-game',
  standalone: false,
  templateUrl: './game.component.html',
  styleUrl: './game.component.css',
  encapsulation: ViewEncapsulation.None
})
export class GameComponent implements OnInit {
  protected board: HTMLTableCellElement[][] = [];
  protected initFen?: string;
  protected gameState: GameState = new GameState();
  protected movelist!: Record<string, any>;

  constructor(private gameService: GameService, private websocketService: WebsocketService, private router: Router) {
  }

  ngOnInit() {
    this.board = [];
    this.initFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    // this.initFen = "rnbQ3r/pppkb2p/6p1/8/8/2N5/PPPq1P1P/R4KNR b KQkq - 0 1";
  }

  fenParser(fen: string) {
    let i: number = 0;
    let n: number = fen.length;
    let row: number = 0;
    let col: number = 0;
    let s: number = 0;
    let hm: String = "";
    let fm: String = "";
    this.gameState.castleWhite = "";
    this.gameState.castleBlack = "";

    while (i < n) {
      if (s == 0 && fen[i] == '/') {
        row++;
        i++;
        col = 0;
        continue;
      }
      if (fen[i] == " ") {
        i++;
        s++;
        continue;
      }
      if (s == 0) {
        let piece: number;
        switch (fen[i]) {
          case 'r':
            piece = Piece.ROOK_BLACK;
            break;
          case 'n':
            piece = Piece.KNIGHT_BLACK;
            break;
          case 'b':
            piece = Piece.BISHOP_BLACK;
            break;
          case 'q':
            piece = Piece.QUEEN_BLACK;
            break;
          case 'k':
            piece = Piece.KING_BLACK;
            break;
          case 'p':
            piece = Piece.PAWN_BLACK;
            break;
          case 'P':
            piece = Piece.PAWN_WHITE;
            break;
          case 'R':
            piece = Piece.ROOK_WHITE;
            break;
          case 'N':
            piece = Piece.KNIGHT_WHITE;
            break;
          case 'B':
            piece = Piece.BISHOP_WHITE;
            break;
          case 'Q':
            piece = Piece.QUEEN_WHITE;
            break;
          case 'K':
            piece = Piece.KING_WHITE;
            break;
          default:
            piece = -Number(fen[i]).valueOf();
        }
        if (piece >= 0) {
          this.gameState.boardCells[row][col++] = piece.valueOf();
        } else {
          for (let j = 0; j < -1 * piece; j++) {
            this.gameState.boardCells[row][col++] = Piece.SPACE;
          }
        }
        i++;
      } else if (s == 1) {
        if (fen[i] == 'w') {
          this.gameState.activeColor = "w";
        } else {
          this.gameState.activeColor = "b";
        }
        i++;
      } else if (s == 2) {
        switch (fen[i]) {
          case 'K':
            this.gameState.castleWhite += "K";
            break;
          case 'Q':
            this.gameState.castleWhite += "Q";
            break;
          case 'k':
            this.gameState.castleBlack += "k";
            break;
          case 'q':
            this.gameState.castleBlack += "q";
            break;
          default:
            break;
        }
        i++;
      } else if (s == 3) {
        if (fen[i] !== '-') {
          this.gameState.enpassant += fen[i];
        }
        i++;
      } else if (s == 4) {
        hm += fen[i];
        i++;
      } else if (s == 5) {
        fm += fen[i];
        i++;
      }
    }

    this.gameState.halfMoveCount = Number(hm).valueOf();
    this.gameState.fullMoveCount = Number(fm).valueOf();
  }

  buildFen(): string {
    let fen: string = "";
    let spaceCount: number = 0;
    for (let row: number = 0; row < this.gameState.boardCells.length; row++) {
      for (let col: number = 0; col < this.gameState.boardCells.length; col++) {
        let piece: number = this.gameState.boardCells[row][col];
        switch (piece) {
          case Piece.ROOK_BLACK:
            if (spaceCount > 0) {
              fen += spaceCount;
              spaceCount = 0;
            }
            fen += 'r';
            break;
          case Piece.KNIGHT_BLACK:
            if (spaceCount > 0) {
              fen += spaceCount;
              spaceCount = 0;
            }
            fen += 'n';
            break;
          case Piece.BISHOP_BLACK:
            if (spaceCount > 0) {
              fen += spaceCount;
              spaceCount = 0;
            }
            fen += 'b';
            break;
          case Piece.KING_BLACK:
            if (spaceCount > 0) {
              fen += spaceCount;
              spaceCount = 0;
            }
            fen += 'k';
            break;
          case Piece.QUEEN_BLACK:
            if (spaceCount > 0) {
              fen += spaceCount;
              spaceCount = 0;
            }
            fen += 'q'
            break;
          case Piece.PAWN_BLACK:
            if (spaceCount > 0) {
              fen += spaceCount;
              spaceCount = 0;
            }
            fen += 'p';
            break;

          case Piece.ROOK_WHITE:
            if (spaceCount > 0) {
              fen += spaceCount;
              spaceCount = 0;
            }
            fen += 'R';
            break;
          case Piece.KNIGHT_WHITE:
            if (spaceCount > 0) {
              fen += spaceCount;
              spaceCount = 0;
            }
            fen += 'N';
            break;
          case Piece.BISHOP_WHITE:
            if (spaceCount > 0) {
              fen += spaceCount;
              spaceCount = 0;
            }
            fen += 'B';
            break;
          case Piece.KING_WHITE:
            if (spaceCount > 0) {
              fen += spaceCount;
              spaceCount = 0;
            }
            fen += 'K'
            break;
          case Piece.QUEEN_WHITE:
            if (spaceCount > 0) {
              fen += spaceCount;
              spaceCount = 0;
            }
            fen += 'Q';
            break;
          case Piece.PAWN_WHITE:
            if (spaceCount > 0) {
              fen += spaceCount;
              spaceCount = 0;
            }
            fen += 'P';
            break;

          default:
            spaceCount++;
        }
      }
      if (row < this.gameState.boardCells.length) {
        if (spaceCount > 0) {
          fen += spaceCount;
          spaceCount = 0;
        }
        if (row < this.gameState.boardCells.length - 1) fen += '/';
      }
    }
    fen += " ";
    fen += this.gameState.activeColor;
    fen += " ";
    fen += this.gameState.castleWhite;
    fen += this.gameState.castleBlack;
    if (this.gameState.castleBlack == "" && this.gameState.castleWhite == "") {
      fen += "-";
    }
    fen += " ";
    fen += this.gameState.enpassant == "" ? "-" : this.gameState.enpassant;
    fen += " ";
    fen += this.gameState.halfMoveCount;
    fen += " ";
    fen += this.gameState.fullMoveCount;
    return fen;
  }

  ngAfterViewInit() {
    // this.getMoveList();
    this.gameState.playerColor = gamedtls.color!;
    this.createBoard();
    if (gamedtls.color == "w") {
      this.fenParser(this.initFen!);
      this.gameState.activeCell = {x: 3, y: 3};
      this.gameState.activeColor = "b";
      this.makeMove(4, 4); // to get list of moves and to start the game
    }
    this.observeMakeMove();
    this.updateBoard();
    // this.method2();
  }

  convertPieceToString(p: number): string {
    let pieceString = "";
    switch (p) {
      case Piece.ROOK_BLACK:
        pieceString = '♜';
        break;
      case Piece.KNIGHT_BLACK:
        pieceString = '♞';
        break;
      case Piece.BISHOP_BLACK:
        pieceString = '♝';
        break;
      case Piece.KING_BLACK:
        pieceString = '♚';
        break;
      case Piece.QUEEN_BLACK:
        pieceString = '♛';
        break;
      case Piece.PAWN_BLACK:
        pieceString = '♟';
        break;

      case Piece.ROOK_WHITE:
        pieceString = '♖';
        break;
      case Piece.KNIGHT_WHITE:
        pieceString = '♘';
        break;
      case Piece.BISHOP_WHITE:
        pieceString = '♗';
        break;
      case Piece.KING_WHITE:
        pieceString = '♔';
        break;
      case Piece.QUEEN_WHITE:
        pieceString = '♕';
        break;
      case Piece.PAWN_WHITE:
        pieceString = '♙';
        break;

      default:
        pieceString = '&nbsp;';
    }
    return pieceString;
  }

  getMoveList() {
    let fen: string = this.buildFen();
    this.gameService.getMoveList(fen).subscribe(moves => {
      this.movelist = moves;
    });
  }

  updateBoard(): void {
    for (let i = 0; i < 9; i++) {
      for (let j = 0; j < 9; j++) {
        let cell: HTMLTableCellElement = this.board[i][j];
        let flip: boolean = this.gameState.playerColor == "w";
        if (this.gameState.playerColor == "w" || i == 0 || j == 0) {
          cell = this.board[i][j];
        } else {
          cell = this.board[9 - i][9 - j];
        }

        cell.innerHTML = "&nbsp;";
        cell.classList.remove('black-pawn');
        if (i === 0) {
          cell.classList.add('file-marker');
          if (j > 0) {
            if (flip) {
              cell.innerHTML = String.fromCharCode("a".charCodeAt(0) + j - 1);
            } else {
              cell.innerHTML = String.fromCharCode("h".charCodeAt(0) - j + 1);
            }
          }
        } else if (j === 0) {
          cell.classList.add('rank-marker');
          if (i > 0) {
            if (flip) {
              cell.innerHTML = String(9 - i);
            } else {
              cell.innerHTML = String(i);
            }
          }
        } else {
          cell.innerHTML = this.convertPieceToString(this.gameState.boardCells[i - 1][j - 1]);
          if (this.gameState.boardCells[i - 1][j - 1] == Piece.PAWN_BLACK) {
            cell.classList.add('black-pawn');
          }
          if ((this.gameState.boardCells[i - 1][j - 1] == Piece.KING_WHITE && this.gameState.isWhiteKingUnderCheck)
            || (this.gameState.boardCells[i - 1][j - 1] == Piece.KING_BLACK && this.gameState.isBlackKingUnderCheck)) {
            cell.classList.add('highlight-check');
          } else {
            cell.classList.remove('highlight-check');
          }
          if (this.gameState.isGameCompleted) {
            if (this.gameState.winnerColor == "w" && this.gameState.boardCells[i - 1][j - 1] == Piece.KING_BLACK) {
              cell.classList.remove('highlight-check');
              cell.classList.add('checkmate');
            }
            if (this.gameState.winnerColor == "b" && this.gameState.boardCells[i - 1][j - 1] == Piece.KING_WHITE) {
              cell.classList.remove('highlight-check');
              cell.classList.add('checkmate');
            }
          }

          cell.classList.add('piece');
          if ((i + j) % 2 == 0) {
            cell.classList.add('light');
          } else {
            cell.classList.add('dark');
          }
        }
      }
    }
  }

  createBoard(): void {
    let gameTable: HTMLTableElement = document.createElement('table');
    let tableBody = document.createElement('tbody');
    gameTable.appendChild(tableBody);
    for (let i = 0; i < 9; i++) {
      // let row = document.createElement('tr');
      let arr: HTMLTableCellElement[] = [];
      for (let j = 0; j < 9; j++) {
        let cell = document.createElement('td');
        cell.innerHTML = "&nbsp;"

        let a: number = i - 1;
        let b: number = j - 1;
        cell.id = `${a}-${b}`;
        cell.addEventListener('click', (e: MouseEvent): void => {
          if ((this.gameState.playerColor == "w" && this.gameState.activeColor == "w" && this.isWhite(this.gameState.boardCells[a][b]))
            || (this.gameState.playerColor == "b" && this.gameState.activeColor == "b" && this.isBlack(this.gameState.boardCells[a][b]))) {
            this.gameState.activeCell = {x: a, y: b};
          } else if (this.gameState.activeCell == null) {
            return;
          } else {
            let targetElement: HTMLElement | null = document.getElementById(`${a}-${b}`);
            if (targetElement && targetElement.classList.contains('highlight-target')) {
              targetElement.classList.remove('highlight-target');
              targetElement.classList.remove('highlight-self');
              targetElement.classList.add('highlight-move');
              this.makeMove(a, b);
            }
            this.gameState.activeCell = null;
            this.cleanup();
            return;
          }

          let pieceAvailableMoves = this.movelist['availableMoves'][a * 8 + b];
          this.cleanup();
          let selfElement: HTMLElement | null = document.getElementById(`${a}-${b}`);
          if (selfElement && i != 0 && j != 0) {
            selfElement.classList.add('highlight-self');
          }
          let targetMoves: Array<any> = pieceAvailableMoves['to'];
          for (let t3: number = 0; t3 < targetMoves.length; t3++) {
            let targetElement: HTMLElement | null = document.getElementById(`${targetMoves[t3].x}-${targetMoves[t3].y}`);
            if (targetElement) {
              if ((this.gameState.activeColor == "w" && this.isBlack(this.gameState.boardCells[targetMoves[t3].x][targetMoves[t3].y])) ||
                (this.gameState.activeColor == "b" && this.isWhite(this.gameState.boardCells[targetMoves[t3].x][targetMoves[t3].y]))) {
                targetElement.classList.add('highlight-target-possible-capture');
              }
              targetElement.classList.add('highlight-target');
            }

          }


        });
        // row.appendChild(cell);
        arr.push(cell);
      }
      this.board.push(arr);
      // tableBody.appendChild(row);
    }
    if (this.gameState.playerColor == "b") {
      let boardTemp: HTMLTableCellElement[][] = [];
      boardTemp.push(this.board[0]);
      for (let i = 1; i < 9; i++) {
        let arr: HTMLTableCellElement[] = [];
        arr.push(this.board[i][0]);
        for (let j = 1; j < 9; j++) {
          arr.push(this.board[9 - i][9 - j]);
        }
        boardTemp.push(arr);
      }
      this.board = boardTemp;
    }

    for (let i = 0; i < 9; i++) {
      let row = document.createElement('tr');
      for (let j = 0; j < 9; j++) {
        row.appendChild(this.board[i][j]);
      }
      tableBody.appendChild(row);
    }

    let gameContainer = document.getElementById('game-container');
    if (gameContainer !== null) {
      gameContainer.appendChild(gameTable);
    }
  }

  isBlack(p: Piece): boolean {
    return p > 6;
  }

  isWhite(p: Piece): boolean {
    return p < 6;
  }

  private makeMove(a: number, b: number): boolean {
    let piece: Piece = this.gameState.boardCells[this.gameState.activeCell!['x']][this.gameState.activeCell!['y']];

    // PAWN PROMOTION
    if (piece == Piece.PAWN_BLACK && this.gameState.activeColor == "b" && a == 7) piece = Piece.QUEEN_BLACK;
    if (piece == Piece.PAWN_WHITE && this.gameState.activeColor == "w" && a == 0) piece = Piece.QUEEN_WHITE;

    // castling rights
    if (piece == Piece.ROOK_WHITE && this.gameState.activeColor == "w" && this.gameState.activeCell!['x'] == 7 && this.gameState.activeCell!['y'] == 0) {
      this.gameState.castleWhite = this.gameState.castleWhite.replace("Q", "");
    } else if (piece == Piece.ROOK_WHITE && this.gameState.activeColor == "w" && this.gameState.activeCell!['x'] == 7 && this.gameState.activeCell!['y'] == 7) {
      this.gameState.castleWhite = this.gameState.castleWhite.replace("K", "");
    } else if (piece == Piece.ROOK_BLACK && this.gameState.activeColor == "b" && this.gameState.activeCell!['x'] == 0 && this.gameState.activeCell!['y'] == 0) {
      this.gameState.castleBlack = this.gameState.castleBlack.replace("q", "");
    } else if (piece == Piece.ROOK_BLACK && this.gameState.activeColor == "b" && this.gameState.activeCell!['x'] == 0 && this.gameState.activeCell!['y'] == 7) {
      this.gameState.castleBlack = this.gameState.castleBlack.replace("k", "");
    } else if (piece == Piece.KING_WHITE && this.gameState.activeColor == "w" && this.gameState.activeCell!['x'] == 7 && this.gameState.activeCell!['y'] == 4) {
      this.gameState.castleWhite = "";
    } else if (piece == Piece.KING_BLACK && this.gameState.activeColor == "b" && this.gameState.activeCell!['x'] == 0 && this.gameState.activeCell!['y'] == 4) {
      this.gameState.castleBlack = "";
    }

    let pieceRook: Piece;
    if (piece == Piece.KING_WHITE && this.gameState.activeColor == "w" && Math.abs(this.gameState.activeCell!['y'] - b) == 2) {
      if (this.gameState.activeCell!['y'] > b) { // Queen Side Castling
        pieceRook = this.gameState.boardCells[7][0];
        this.gameState.boardCells[7][3] = pieceRook;
        this.gameState.boardCells[7][0] = Piece.SPACE;
      } else { // King Side Castling
        pieceRook = this.gameState.boardCells[7][7];
        this.gameState.boardCells[7][5] = pieceRook;
        this.gameState.boardCells[7][7] = Piece.SPACE;
      }
    } else if (piece == Piece.KING_BLACK && this.gameState.activeColor == "b" && Math.abs(this.gameState.activeCell!['y'] - b) == 2) {
      if (this.gameState.activeCell!['y'] > b) { // Queen Side Castling
        pieceRook = this.gameState.boardCells[0][0];
        this.gameState.boardCells[0][3] = pieceRook;
        this.gameState.boardCells[0][0] = Piece.SPACE;
      } else { // King Side Castling
        pieceRook = this.gameState.boardCells[0][7];
        this.gameState.boardCells[0][5] = pieceRook;
        this.gameState.boardCells[0][7] = Piece.SPACE;
      }
    }

    this.gameState.boardCells[this.gameState.activeCell!['x']][this.gameState.activeCell!['y']] = Piece.SPACE;
    this.gameState.boardCells[a][b] = piece;
    this.gameState.activeColor = this.gameState.activeColor == "w" ? "b" : "w";
    this.gameState.fen = this.buildFen();
    this.websocketService.makeMove(this.gameState.fen);
    return true;
  }

  private observeMakeMove() {
    this.websocketService.getStompClient()?.subscribe(`/topic/game/${gamedtls.gameID}`, (frame) => {
      let body = JSON.parse(frame['body']);
      this.movelist = body;
      this.fenParser(this.movelist['fen']);
      this.gameState.isGameCompleted = body['gameEnded'];
      this.gameState.isWhiteKingUnderCheck = body['whiteKingUnderCheck'];
      this.gameState.isBlackKingUnderCheck = body['blackKingUnderCheck'];
      if (this.gameState.isGameCompleted) {
        this.gameState.winnerColor = body['winnerColor'];
        let modal = document.getElementById("game-complete-modal-trigger");
        if (modal) modal.click();
        this.websocketService.disconnect();
      }
      this.updateBoard();
    }, this.websocketService.getXSRFTOKEN());


  }

  private cleanup(): void {
    for (let t1: number = 0; t1 < this.gameState.boardCells.length; t1++) {
      for (let t2: number = 0; t2 < this.gameState.boardCells.length; t2++) {
        let targetElement: HTMLElement | null = document.getElementById(`${t1}-${t2}`);
        if (targetElement) {
          targetElement.classList.remove('highlight-target');
          targetElement.classList.remove('highlight-self');
          targetElement.classList.remove('highlight-move');
          targetElement.classList.remove('highlight-target-possible-capture');
        }
      }
    }
  }

  newGame(): void {
    this.websocketService.disconnect();
    let modalClose = document.getElementById("modal-close");
    if (modalClose) modalClose.click();
    this.router.navigate(['/mode-select']);
  }
}

enum Piece {
  KING_WHITE,
  QUEEN_WHITE,
  ROOK_WHITE,
  KNIGHT_WHITE,
  BISHOP_WHITE,
  PAWN_WHITE,
  SPACE,
  KING_BLACK,
  QUEEN_BLACK,
  ROOK_BLACK,
  KNIGHT_BLACK,
  BISHOP_BLACK,
  PAWN_BLACK
}

// export class GameStateFromBackend {
//   public gameID: string | null = null;
//   public fen: string | null = null;
//   public availableMoves: string | null = null;
//   public isGameEnded: string | null = null;
//   public winnerColor: string | null = null;
//   public activeColor: string | null = null;
//
// };

class GameState {
  public boardCells: number[][];
  public fen: string = "";
  public isGameCompleted: boolean = false;
  public winnerColor: string | undefined = undefined;
  public activeColor: string = "w"; // "w" for white; "b" for black;
  public playerColor: string = "w"; // "w" for white; "b" for black;
  public castleWhite: string = ""; // "K" for King Side castling; "Q" for Queen Side castling; "" for no castling;
  public castleBlack: string = ""; // "k" for King Side castling; "q" for Queen Side castling; "" for no castling;
  public enpassant: string = ""; // enpassant square or empty string
  public halfMoveCount: number = 0;
  public fullMoveCount: number = 0;
  public isWhiteKingUnderCheck: boolean = false;
  public isBlackKingUnderCheck: boolean = false;
  public activeCell: Record<string, number> | null = null;
  public gameCompleteModal: boolean = false;

  constructor() {
    this.boardCells = [];
    for (let i = 0; i < 8; i++) {
      let arr: number[] = [];
      for (let j = 0; j < 8; j++) {
        arr.push(Piece.SPACE);
      }
      this.boardCells.push(arr);
    }
  }
}
