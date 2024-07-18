package model;

import chess.ChessGame;

record GameData(int myGameID, String myWhiteUsername, String myBlackUsername,
                       String myGameName, ChessGame myGame) {}
