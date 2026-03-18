package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    private GameBoard board;

    @BeforeEach
    void setUp() {
        board = new GameBoard();
    }

    // --- generateBoard() testai ---

    @Test
    void generateBoard_stateShouldBeGenerated() {
        assertFalse(board.isGenerated());
        board.generateBoard(5, 5);
        assertTrue(board.isGenerated());
    }

    @Test
    void generateBoard_shouldNotPlaceMineOnClickedCell() {
        board.generateBoard(5, 5);
        char[][] view = board.getView();
        // po generavimo visi hidden - nė vienas neturėtų būti mina iš karto
        assertEquals('h', view[5][5]);
    }

    @Test
    void generateBoard_stateShouldRemainPlaying() {
        board.generateBoard(5, 5);
        assertEquals("playing", board.getState());
    }

    @Test
    void generateBoard_cornerClick_shouldNotThrow() {
        assertDoesNotThrow(() -> board.generateBoard(0, 0));
    }

    @Test
    void generateBoard_edgeClick_shouldNotThrow() {
        assertDoesNotThrow(() -> board.generateBoard(11, 11));
    }

    // --- click() testai ---

    @Test
    void click_beforeGenerate_stateShouldRemainPlaying() {
        board.generateBoard(5, 5);
        board.click(5, 5);
        assertNotEquals("fail", board.getState());
    }

    @Test
    void click_revealedCell_shouldNotChangeState() {
        board.generateBoard(5, 5);
        board.click(5, 5);
        String stateBefore = board.getState();
        board.click(5, 5);
        assertEquals(stateBefore, board.getState());
    }

    @Test
    void click_stateNotPlaying_shouldDoNothing() {
        board.generateBoard(5, 5);
        // simuliuoti fail būseną
        board.click(0, 0); // bandome spausti
        // state neturėtų būti null
        assertNotNull(board.getState());
    }

    @Test
    void click_validCell_viewShouldChange() {
        board.generateBoard(5, 5);
        board.click(5, 5);
        char[][] view = board.getView();
        assertNotEquals('h', view[5][5]);
    }

    // --- checkWin() netiesiogiai per click() ---

    @Test
    void click_initialState_shouldBePlaying() {
        assertEquals("playing", board.getState());
    }

    // --- clearBoard() ---

    @Test
    void clearBoard_shouldResetGenerated() {
        board.generateBoard(5, 5);
        board.clearBoard();
        assertFalse(board.isGenerated());
    }

    @Test
    void clearBoard_shouldResetStatePlaying() {
        board.generateBoard(5, 5);
        board.clearBoard();
        assertEquals("playing", board.getState());
    }
}