package com.chess.jnd;

import com.chess.jnd.entity.*;
import com.chess.jnd.entity.figures.ShortFigureName;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

public class ParameterFabric {

    private static final String TOKEN_ID_1_WHITE_PLAYER = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzM1MTAxNzQ4LCJDb2xvciI6IndoaXRlIn0.JIxKNQGoPQ7itth-ra0KY73cr-jx6br_fv7OpuFIgeL6SJ8KWteGLq0IeGX26g03ddB_SJ4Zb-h9ioVpIkmeqQ";
    private static final String TOKEN_ID_1_BLACK_PLAYER = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzM1MTE5NTU5LCJDb2xvciI6IndoaXRlIn0.gExbW_tY8LBAB_2mToT_BL4l7kxcE72BQmn0NHAuKMnKLt3-d8cW6MjogeJilPwF07dXHJnvnYUa3o_orJ3JbQ";

    public static String getBlackToken() {
        return TOKEN_ID_1_BLACK_PLAYER;
    }

    public static String getWhiteToken() {
        return TOKEN_ID_1_WHITE_PLAYER;
    }

    public static GameRedis createRedisGame() {
        return GameRedis.builder()
                .id(1)
                .tokenForBlackPlayer(TOKEN_ID_1_BLACK_PLAYER)
                .tokenForWhitePlayer(TOKEN_ID_1_WHITE_PLAYER)
                .active(Color.WHITE)
                .gameInfo(new GameInfo(GameStatus.IN_PROCESS))
                .timeForMove(2000)
                .build();

    }

    public static GameRedis createRedisGameWithBoard() {
        ShortFigureName shortNames [] [] = {
                {ShortFigureName.BLACK_ROOK, ShortFigureName.BLACK_KNIGHT, ShortFigureName.BLACK_BISHOP, ShortFigureName.BLACK_QUEEN, ShortFigureName.BLACK_KING, ShortFigureName.BLACK_BISHOP, ShortFigureName.BLACK_KNIGHT, ShortFigureName.BLACK_ROOK },
                {ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN },
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                { ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN },
                { ShortFigureName.WHITE_ROOK, ShortFigureName.WHITE_KNIGHT, ShortFigureName.WHITE_BISHOP, ShortFigureName.WHITE_QUEEN, ShortFigureName.WHITE_KING, ShortFigureName.WHITE_BISHOP, ShortFigureName.WHITE_KNIGHT, ShortFigureName.WHITE_ROOK },
        };

        return GameRedis.builder()
                .id(1)
                .tokenForBlackPlayer(TOKEN_ID_1_BLACK_PLAYER)
                .tokenForWhitePlayer(TOKEN_ID_1_WHITE_PLAYER)
                .active(Color.WHITE)
                .gameInfo(new GameInfo(GameStatus.IN_PROCESS))
                .timeForMove(2000)
                .board(shortNames)
                .build();
    }

    public static GameRedis createRedisGameWithBoardWithStatusFinished() {
        ShortFigureName shortNames [] [] = {
                {ShortFigureName.BLACK_ROOK, ShortFigureName.BLACK_KNIGHT, ShortFigureName.BLACK_BISHOP, ShortFigureName.BLACK_QUEEN, ShortFigureName.BLACK_KING, ShortFigureName.BLACK_BISHOP, ShortFigureName.BLACK_KNIGHT, ShortFigureName.BLACK_ROOK },
                {ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN, ShortFigureName.BLACK_PAWN },
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                { ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN, ShortFigureName.WHITE_PAWN },
                { ShortFigureName.WHITE_ROOK, ShortFigureName.WHITE_KNIGHT, ShortFigureName.WHITE_BISHOP, ShortFigureName.WHITE_QUEEN, ShortFigureName.WHITE_KING, ShortFigureName.WHITE_BISHOP, ShortFigureName.WHITE_KNIGHT, ShortFigureName.WHITE_ROOK },
        };

        return GameRedis.builder()
                .id(1)
                .tokenForBlackPlayer(TOKEN_ID_1_BLACK_PLAYER)
                .tokenForWhitePlayer(TOKEN_ID_1_WHITE_PLAYER)
                .active(Color.WHITE)
                .gameInfo(new GameInfo(GameStatus.FINISHED))
                .timeForMove(2000)
                .board(shortNames)
                .build();
    }

    public static GameRedis createRedisGameForCheckMateOrDraw() {
        ShortFigureName shortNames [] [] = {
                {ShortFigureName.BLACK_KING, null, null, null, null, ShortFigureName.BLACK_ROOK, null, null },
                {null, null, null, null, null, null, ShortFigureName.BLACK_ROOK, null},
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                {null, null, null, null, null, null, null, null },
                { null, null, null, null, null, null, null, ShortFigureName.WHITE_KING },
        };

        return GameRedis.builder()
                .id(1)
                .tokenForBlackPlayer(TOKEN_ID_1_BLACK_PLAYER)
                .tokenForWhitePlayer(TOKEN_ID_1_WHITE_PLAYER)
                .active(Color.BLACK)
                .gameInfo(new GameInfo(GameStatus.IN_PROCESS))
                .timeForMove(2000)
                .board(shortNames)
                .build();
    }

    public static Game createGame() {

        return Game.builder()
                .id(1)
                .tokenForBlackPlayer(TOKEN_ID_1_BLACK_PLAYER)
                .tokenForWhitePlayer(TOKEN_ID_1_WHITE_PLAYER)
                .active(Color.WHITE)
                .gameInfo(new GameInfo(GameStatus.IN_PROCESS))
                .timeForMove(2000)
                .build();
    }

    public static Game createGameWithBoard() {

        String board =
                """
                [["r","n","b","q","k","b","n","r"],["p","p","p","p","p","p","p","p"],[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null],["P","P","P","P","P","P","P","P"],["R","N","B","Q","K","B","N","R"]]
                 """;

        return Game.builder()
                .id(1)
                .tokenForBlackPlayer(TOKEN_ID_1_BLACK_PLAYER)
                .tokenForWhitePlayer(TOKEN_ID_1_WHITE_PLAYER)
                .active(Color.WHITE)
                .gameInfo(new GameInfo(GameStatus.IN_PROCESS))
                .timeForMove(18000)
                .board(board)
                .build();
    }

    public static Game createGameForCheckMate() {

        String board =
                """
                [["r","n","b","q","k","b","n","r"],["p","p","p","p","p","p","p","p"],[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null],[null,null,null,null,null,null,null,null],["P","P","P","P","P","P","P","P"],["R","N","B","Q","K","B","N","R"]]
                 """;

        return Game.builder()
                .id(1)
                .tokenForBlackPlayer(TOKEN_ID_1_BLACK_PLAYER)
                .tokenForWhitePlayer(TOKEN_ID_1_WHITE_PLAYER)
                .active(Color.BLACK)
                .gameInfo(new GameInfo(GameStatus.IN_PROCESS))
                .timeForMove(18000)
                .board(board)
                .build();

    }

    public static MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "Bearer " + TOKEN_ID_1_WHITE_PLAYER);

        return mockRequest;
    }

    public static GameInfoResponse createGameInfoResponse() {
        return new GameInfoResponse(GameStatus.IN_PROCESS);
    }

    public static GameResponse createGameResponse() {
        return GameResponse.builder()
                .info(new GameInfoResponse(GameStatus.IN_PROCESS))
                .currentColor(Color.WHITE)
                .active(Color.BLACK)
                .tokenForWhitePlayer(TOKEN_ID_1_WHITE_PLAYER)
                .tokenForBlackPlayer(TOKEN_ID_1_BLACK_PLAYER)
                .build();
    }

    public static CreateGameRequest createGameRequest() {
        return new CreateGameRequest(Color.WHITE);
    }

    public static MoveRequest createMoveRequest(int xFrom, int xTo, int yFrom, int yTo) {
        return new MoveRequest(xFrom,xTo, yFrom,yTo);
    }

    public static CacheData<GameRedis> createCacheDataWithRedis() {
        CacheData<GameRedis> cacheData = new CacheData<>();
        GameRedis gameRedis = new GameRedis();
        cacheData.setValue(gameRedis);

        return cacheData;
    }

    public static HttpHeaders createHttpHeader() {
        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + TOKEN_ID_1_WHITE_PLAYER);

        return header;

    }
}