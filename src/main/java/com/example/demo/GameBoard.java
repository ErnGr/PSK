package com.example.demo;

import lombok.Getter;

import java.security.SecureRandom;

public class GameBoard {
    private final int boardSize;
    private final int mineCount;
    private final GameTile[][] board;
    private static final String STATE_PLAYING = "playing";
    @Getter
    private boolean generated = false;
    private final SecureRandom random = new SecureRandom();
    @Getter
    private String state = STATE_PLAYING;

    GameBoard() {
        this(12, 20);
    }

    GameBoard(int boardSize, int mineCount) {
        this.boardSize = boardSize;
        this.mineCount = mineCount;
        this.board = new GameTile[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                board[i][j] = new GameTile(0,'h');
            }
        }
    }

    public void clearBoard()
    {
        generated = false;
        state = STATE_PLAYING;
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                board[i][j] = new GameTile(0,'h');
            }
        }
    }

    public void generateBoard(int clickRow, int clickCol) {
        placeMines(clickRow, clickCol);
        calculateAdjacentNumbers();
        generated = true;
    }

    private void placeMines(int clickRow, int clickCol) {
        for (int i = 0; i < mineCount; i++) {
            int row;
            int col;
            do {
                row = random.nextInt(boardSize);
                col = random.nextInt(boardSize);
            } while (board[row][col].getReal() != 0 ||
                    (row >= clickRow - 1 && row <= clickRow + 1
                            && col >= clickCol - 1 && col <= clickCol + 1));
            board[row][col].setReal(-1);
        }
    }

    private void calculateAdjacentNumbers() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j].getReal() != -1) {
                    board[i][j].setReal(countAdjacentMines(i, j));
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int mines = 0;
        for (int a = row - 1; a <= row + 1; a++) {
            for (int b = col - 1; b <= col + 1; b++) {
                if (a >= 0 && a < boardSize && b >= 0 && b < boardSize && board[a][b].getReal() == -1) {
                        mines++;
                    }

            }
        }
        return mines;
    }

    private void checkWin() {
        boolean checkWin = true;
        for(int i=0; i<boardSize; i++) {
            for(int j=0; j<boardSize; j++) {
                if (((board[i][j].getReal() == -1) && (board[i][j].getView() == 'm')) || ((board[i][j].getReal() != -1) && (board[i][j].getView() == 'f' || board[i][j].getView() == 'h'))) {
                    checkWin = false;
                    break;
                }
            }
            if(!checkWin) {
                break;
            }
        }
        if(checkWin) {
            state = "win";
        }
    }

    private void clickHidden(int row, int col) {
        switch(board[row][col].getReal()) {
            case -1:
                board[row][col].setView('m');
                state = "fail";
                break;
            case 0:
                board[row][col].setView('0');
                clickNumber(row, col);
            break;
            default:
                board[row][col].setView((char)(board[row][col].getReal()+('0')));
                break;
        }
    }

    private void clickNumber(int row, int col) {
        if(countFlagsAround(row, col) ==
                Character.getNumericValue(board[row][col].getView())) {
            revealNeighbors(row, col);
        }
    }

    private int countFlagsAround(int row, int col) {
        int flags = 0;
        for(int i = row-1; i <= row+1; i++) {
            for(int j = col-1; j <= col+1; j++) {
                if(isInBounds(i, j) && board[i][j].getView() == 'f') {
                    flags++;
                }
            }
        }
        return flags;
    }

    private void revealNeighbors(int row, int col) {
        for(int i = row-1; i <= row+1; i++) {
            for(int j = col-1; j <= col+1; j++) {
                if(isInBounds(i, j) && board[i][j].getView() == 'h') {
                    click(i, j);
                }
            }
        }
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < boardSize
                && col >= 0 && col < boardSize;
    }

    public void click(int row, int col) {
        if(board[row][col].getView() == 'h' && state.equals(STATE_PLAYING)){
            clickHidden(row, col);
        }
        else if(state.equals(STATE_PLAYING) && (board[row][col].getView()!='0'&&
                board[row][col].getView()!='m'&&
                board[row][col].getView()!='f'&&
                board[row][col].getView()!='h')){
            clickNumber(row, col);
        }
        checkWin();
    }

    public void flag(int row, int col) {
        board[row][col].flag();
    }

    public char[][] getView(){
        char[][] view = new char[boardSize][boardSize];
        for(int i=0; i<boardSize; i++) {
            for(int j=0; j<boardSize; j++) {
                view[i][j] = board[i][j].getView();
            }
        }
        return view;
    }

}
