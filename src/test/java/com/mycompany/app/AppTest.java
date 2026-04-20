package com.mycompany.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void testInitialBoardIsEmpty() {
        for (char cell : game.board) {
            assertEquals(' ', cell);
        }
    }

    @Test
    void testCheckStateXWinHorizontal() {
        char[] board = {
                'X', 'X', 'X',
                'O', ' ', 'O',
                ' ', ' ', ' '
        };
        game.symbol = 'X'; // Устанавливаем текущий символ для проверки
        assertEquals(State.XWIN, game.checkState(board));
    }

    @Test
    void testCheckStateOWinDiagonal() {
        char[] board = {
                'O', 'X', ' ',
                ' ', 'O', 'X',
                ' ', ' ', 'O'
        };
        game.symbol = 'O';
        assertEquals(State.OWIN, game.checkState(board));
    }

    @Test
    void testCheckStateDraw() {
        char[] board = {
                'X', 'O', 'X',
                'X', 'O', 'O',
                'O', 'X', 'X'
        };
        game.symbol = 'X';
        assertEquals(State.DRAW, game.checkState(board));
    }

    @Test
    void testGenerateMoves() {
        char[] board = {
                'X', 'O', 'X',
                ' ', 'O', ' ',
                ' ', 'X', ' '
        };
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(board, moves);

        assertEquals(4, moves.size());
        assertTrue(moves.contains(3));
        assertTrue(moves.contains(5));
        assertTrue(moves.contains(6));
        assertTrue(moves.contains(8));
    }

    @Test
    void testEvaluatePositionWin() {
        char[] board = {
                'X', 'X', 'X',
                ' ', ' ', ' ',
                ' ', ' ', ' '
        };
        game.symbol = 'X';
        assertEquals(Game.INF, game.evaluatePosition(board, game.player1));
    }

    @Test
    void testEvaluatePositionLoss() {
        char[] board = {
                'O', 'O', 'O',
                ' ', ' ', ' ',
                ' ', ' ', ' '
        };
        game.symbol = 'O';
        assertEquals(-Game.INF, game.evaluatePosition(board, game.player1));
    }

    @Test
    void testMinimaxShouldBlockOpponent() {
        game.board = new char[]{
                'X', 'X', ' ',
                ' ', 'O', ' ',
                ' ', ' ', ' '
        };

        int bestMove = game.MiniMax(game.board, game.player2);

        assertEquals(3, bestMove);
    }

    @Test
    void testMinimaxShouldTakeWinningMove() {

        game.board = new char[]{
                'X', ' ', 'O',
                'X', ' ', 'O',
                ' ', ' ', ' '
        };

        int bestMove = game.MiniMax(game.board, game.player2);

        assertEquals(7, bestMove);
    }

    @Test
    void testMinimaxPrefersQuickWin() {
        game.board = new char[]{
                'O', 'O', ' ',
                ' ', ' ', ' ',
                'X', 'X', ' '
        };
        int bestMove = game.MiniMax(game.board, game.player2);
        assertEquals(3, bestMove, "ИИ должен выбрать победный ход 3");
    }

    @Test
    void testBoardIntegrityAfterMinimax() {
        char[] originalBoard = {
                'X', ' ', ' ',
                ' ', 'O', ' ',
                ' ', ' ', ' '
        };
        game.board = originalBoard.clone();

        game.MiniMax(game.board, game.player2);

        assertArrayEquals(originalBoard, game.board, "Minimax не должен оставлять временные ходы на доске");
    }

    @Test
    void testCheckStateFullBoardNoWinner() {
        char[] board = {
                'X', 'O', 'X',
                'X', 'X', 'O',
                'O', 'X', 'O'
        };
        game.symbol = 'O';
        assertEquals(State.DRAW, game.checkState(board));
    }

    @Test
    void testEvaluatePositionDraw() {
        char[] board = {
                'X', 'O', 'X',
                'X', 'X', 'O',
                'O', 'X', 'O'
        };
        game.symbol = 'O';
        assertEquals(0, game.evaluatePosition(board, game.player1), "При ничьей оценка должна быть 0");
    }

    @Test
    void testPlayerInitialization() {
        assertNotNull(game.player1);
        assertNotNull(game.player2);
        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
    }

    @Test
    void testUtilityPrintMethods() {
        // Проверка, что методы не выбрасывают исключений
        assertDoesNotThrow(() -> {
            Utility.print(game.board);
            Utility.print(new int[9]);
            Utility.print(new ArrayList<Integer>());
        });
    }

    @Test
    void testCellProperties() {
        TicTacToeCell cell = new TicTacToeCell(5, 1, 1);
        assertEquals(5, cell.getNum());
        assertEquals(1, cell.getRow());
        assertEquals(1, cell.getCol());

        cell.setMarker("X");
        assertEquals('X', cell.getMarker());
        assertFalse(cell.isEnabled());
    }

    @Test
    void testGameResetAndRestart() {
        game.board[0] = 'X';
        game.state = State.XWIN;

        game = new Game();
        assertEquals(State.PLAYING, game.state, "После сброса игра должна вернуться в состояние PLAYING");
        assertEquals(' ', game.board[0], "Доска должна быть очищена");
    }

    @Test
    void testInvalidMoveScenario() {
        game.board[0] = 'X';

        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(game.board, moves);
        assertFalse(moves.contains(0), "Список ходов не должен содержать уже занятую ячейку");
    }

    @Test
    void testCheckStateAllWinningLines() {
        char[] boardVertical = {
                ' ', 'X', ' ',
                ' ', 'X', ' ',
                ' ', 'X', ' '
        };
        game.symbol = 'X';
        assertEquals(State.XWIN, game.checkState(boardVertical));

        char[] boardDiag2 = {
                ' ', ' ', 'O',
                ' ', 'O', ' ',
                'O', ' ', ' '
        };
        game.symbol = 'O';
        assertEquals(State.OWIN, game.checkState(boardDiag2));
    }

    @Test
    void testGameCycleStates() {
        assertEquals(State.PLAYING, game.state);

        game.board = new char[]{'X','X','X',' ',' ',' ',' ',' ',' '};
        game.symbol = 'X';
        game.state = game.checkState(game.board);
        assertEquals(State.XWIN, game.state);

        game = new Game();
        assertEquals(State.PLAYING, game.state);
        assertEquals(' ', game.board[0]);
    }

    @Test
    void testMinimaxOnFullBoard() {
        char[] fullBoard = {'X','O','X','X','X','O','O','X','O'};
        game.board = fullBoard;

        assertDoesNotThrow(() -> game.MiniMax(fullBoard, game.player2));
    }

    @Test
    void testPanelInitialization() {
        TicTacToePanel panel = new TicTacToePanel(new java.awt.GridLayout(3,3));
        assertNotNull(panel);
        assertEquals(9, panel.getComponentCount());
    }

    @Test
    void testProgramObjectCreation() {
        Program program = new Program();
        assertNotNull(program);
    }

    @Test
    void testMinimaxStepsCounterReset() {
        game.board = new char[]{
                'X', 'X', ' ',
                ' ', ' ', ' ',
                ' ', ' ', ' '
        };
        game.MiniMax(game.board, game.player2);
        assertEquals(0, game.q, "Счетчик шагов должен быть сброшен после завершения хода");
    }


}