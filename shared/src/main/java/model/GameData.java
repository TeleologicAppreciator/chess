package model;

import chess.ChessGame;

public record GameData(int myGameID, String myWhiteUsername, String myBlackUsername,
                       String myGameName, ChessGame myGame) {}
