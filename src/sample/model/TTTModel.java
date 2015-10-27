package sample.model;

/**
 * Created by Alexander on 30/09/15.
 */
public class TTTModel {
    private FieldState field[][];
    private FieldState currentState;
    private int wonFields[][];
    private boolean isTie;

    public TTTModel() {
        field = new FieldState[3][3];
        newGame();
    }

    private void clearField() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = FieldState.empty;
            }
        }
    }

    private boolean checkMainDiagonal(FieldState state) {
        for (int i = 0; i < 3; i++) {
            if (field[i][i] != state) {
                return false;
            }
        }
        return true;
    }

    private boolean checkAdditionalDiagonal(FieldState state) {
        for (int i = 0; i < 3; i++) {
            if (field[i][2-i] != state) {
                return false;
            }
        }
        return true;
    }

    private boolean checkNoEmptyFields() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] == FieldState.empty) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean tryFinish(int i, int j, FieldState state) {
        if ((field[i][0] == state) && (field[i][1] == state) && (field[i][2] == state)) {
            finish(i, 0, i, 1, i, 2);
            return true;
        }

        if ((field[0][j] == state) && (field[1][j] == state) && (field[2][j] == state)) {
            finish(0, j, 1, j, 2, j);
            return true;
        }

        if (checkMainDiagonal(state)) {
            finish(0, 0, 1, 1, 2, 2);
            return true;
        }

        if (checkAdditionalDiagonal(state)) {
            finish(0, 2, 1, 1, 2, 0);
            return true;
        }

        if (checkNoEmptyFields()) {
            finish();
            return true;
        }

        return false;
    }

    private void finish() {
        currentState = FieldState.empty;
        isTie = true;
    }

    private void finish(int x1, int y1, int x2, int y2, int x3, int y3) {
        currentState = FieldState.empty;
        wonFields = new int[3][2];
        wonFields[0][0] = x1;
        wonFields[0][1] = y1;
        wonFields[1][0] = x2;
        wonFields[1][1] = y2;
        wonFields[2][0] = x3;
        wonFields[2][1] = y3;
    }


    public boolean isFinished() {
        return currentState == FieldState.empty;
    }

    public int[][] getWonFields() {
        return wonFields;
    }

    public void newGame() {
        clearField();
        currentState = FieldState.x;
        wonFields = null;
        isTie = false;
    }

    public FieldState trySetField(int i, int j) {
        if (field[i][j] != FieldState.empty || currentState == FieldState.empty)
            return null;

        field[i][j] = currentState;

        if (!tryFinish(i, j, currentState)) {
            currentState = currentState == FieldState.x ? FieldState.o : FieldState.x;
        }

        return field[i][j];
    }

    public boolean isTie() {
        return isTie;
    }
}
