package com.chess.online.controllers;

import com.chess.online.dto.MakeMoveInFen;
import com.chess.online.dto.MovesResponseDto;
import com.chess.online.dto.UserIn;
import com.chess.online.services.GameService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

//@RestController
//@RequestMapping("/api/game")
@Controller
public class GameController {

  private final GameService gameService;

  private SimpMessagingTemplate simpMessagingTemplate;

  public GameController(GameService gameService, SimpMessagingTemplate simpMessagingTemplate) {
    this.gameService = gameService;
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

  @PostMapping("/getMoves")
  public MovesResponseDto getMoves(@RequestBody String fen) {
      return this.gameService.getMoves(fen);
  }

//  @PostMapping("/makeMove")
  @MessageMapping("/game/{gameID}")
  @SendTo("/topic/game/{gameID}")
  public MovesResponseDto makeMove(@DestinationVariable String gameID, MakeMoveInFen fen) {
    return this.gameService.makeMove(fen.getFen());
  }

  @MessageMapping("/user/{userID}")
//  @SendTo("/topic/game/{userID}")
  public void users(@DestinationVariable String userID) {
//    this.gameService.makeMove(user.getUserID());
  }

  @MessageMapping("/game-mode/blitz")
//  @SendTo("/topic/game/")
  public void gameModeBlitz(UserIn user) {
    Map<String, String> map = this.gameService.addUserInBlitz(user.getUserID());

    if (map != null) {
      for (String userid: map.keySet()) {
        if ("gameID".equals(userid)) continue;
        simpMessagingTemplate.convertAndSend(String.format("/topic/user/%s", userid), map);
      }
    }
//    return this.gameService.makeMove(user.getUserID());
  }

  @MessageMapping("/game-mode/bullet")
//  @SendTo("/topic/")
  public void gameModeBullet(UserIn user) {
    Map<String, String> map = this.gameService.addUserInBullet(user.getUserID());

    if (map != null) {
      for (String userid: map.keySet()) {
        if ("gameID".equals(userid)) continue;
        simpMessagingTemplate.convertAndSend(String.format("/topic/user/%s", userid), map);
      }
    }
  }

  @MessageMapping("/game-mode/rapid")
//  @SendTo("/topic/")
  public void gameModeRapid(UserIn user) {
    Map<String, String> map = this.gameService.addUserInRapid(user.getUserID());

    if (map != null) {
      for (String userid: map.keySet()) {
        if ("gameID".equals(userid)) continue;
        simpMessagingTemplate.convertAndSend(String.format("/topic/user/%s", userid), map);
      }
    }
  }

  @MessageMapping("/game-mode/classic")
//  @SendTo("/topic/")
  public void gameModeClassic(UserIn user) {
    Map<String, String> map = this.gameService.addUserInClassic(user.getUserID());

    if (map != null) {
      for (String userid: map.keySet()) {
        if ("gameID".equals(userid)) continue;
        simpMessagingTemplate.convertAndSend(String.format("/topic/user/%s", userid), map);
      }
    }
  }

  @MessageExceptionHandler
  @SendTo("/topic/errors")
  public Map<String, String> handleExcpetion(Throwable exception) {
    Map<String, String> map = new HashMap<>();
    map.put("error", exception.getMessage());
    return map;
  }

}
