package com.example.mazerunnergame;

import java.util.*;

public class MazeModel {
    private int rows, cols;
    private int[][] grid;
    private int playerRow, playerCol;
    private int botRow, botCol;
    private int goalRow, goalCol;
    private List<int[]> botPath;
    private int botPathIndex;

    public static final int WALL = 1;
    public static final int PATH = 0;
    public static final int GOAL = 2;

    private boolean gameWon = false;

    public MazeModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new int[rows][cols];
        generateMaze();
        ensureValidGoalPosition();
        resetPositions();
    }

    public void updateGrid(int row, int col) {
        this.rows = row;
        this.cols = col;
        this.grid = new int[row][col];
        generateMaze();
        ensureValidGoalPosition();
        resetPositions();
    }

    public void generateMaze() {
        for (int i = 0; i < rows; i++) {
            Arrays.fill(grid[i], WALL);
        }

        boolean[][] visited = new boolean[rows][cols];
        if (rows % 2 == 0) {
            rows--;
        }
        if (cols % 2 == 0) {
            cols--;
        }
        MazeGeneration(1, 1, visited);

        grid[1][1] = PATH;
    }

    public void ensureValidGoalPosition() {
        List<int[]> openCells = new ArrayList<>();
        for (int r = 1; r < rows - 1; r++) {
            for (int c = 1; c < cols - 1; c++) {
                if (grid[r][c] == PATH && (r != 1 || c != 1)) {
                    openCells.add(new int[]{r, c});
                }
            }
        }

        Random rand = new Random();
        while (!openCells.isEmpty()) {
            int[] potentialGoal = openCells.remove(rand.nextInt(openCells.size()));
            List<int[]> path = findShortestPath(1, 1, potentialGoal[0], potentialGoal[1]);
            if (!path.isEmpty()) {
                grid[potentialGoal[0]][potentialGoal[1]] = GOAL;
                goalRow = potentialGoal[0];
                goalCol = potentialGoal[1];
                return;
            }
        }
    }

    private void MazeGeneration(int row, int col, boolean[][] visited) {
        visited[row][col] = true;
        grid[row][col] = PATH;

        int[][] directions = { {0, 2}, {0, -2}, {2, 0}, {-2, 0} };
        Collections.shuffle(Arrays.asList(directions));

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (newRow > 0 && newRow < rows - 1 && newCol > 0 && newCol < cols - 1 && !visited[newRow][newCol]) {
                grid[row + dir[0] / 2][col + dir[1] / 2] = PATH;
                MazeGeneration(newRow, newCol, visited);
            }
        }
    }

    public void resetPositions() {
        playerRow = 1;
        playerCol = 1;
        botRow = 1;
        botCol = 1;
        botPath = findShortestPath(botRow, botCol, goalRow, goalCol);
        botPathIndex = 0;
    }

    private List<int[]> findShortestPath(int startRow, int startCol, int goalRow, int goalCol) {
        int[] directions = {-1, 0, 1, 0, 0, -1, 0, 1};
        boolean[][] visited = new boolean[rows][cols];
        int[][] parent = new int[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;
        parent[startRow][startCol] = -1;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            if (current[0] == goalRow && current[1] == goalCol) {
                return reconstructPath(parent, goalRow, goalCol);
            }

            for (int i = 0; i < directions.length; i += 2) {
                int newRow = current[0] + directions[i];
                int newCol = current[1] + directions[i + 1];

                if (newRow > 0 && newRow < rows - 1 && newCol > 0 && newCol < cols - 1
                        && grid[newRow][newCol] != WALL && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    parent[newRow][newCol] = current[0] * cols + current[1];
                    queue.add(new int[]{newRow, newCol});
                }
            }
        }
        return new ArrayList<>();
    }

    private List<int[]> reconstructPath(int[][] parent, int goalRow, int goalCol) {
        List<int[]> path = new ArrayList<>();
        int current = goalRow * cols + goalCol;
        while (current != -1) {
            int row = current / cols;
            int col = current % cols;
            path.add(new int[]{row, col});
            current = parent[row][col];
        }
        Collections.reverse(path);
        return path;
    }

    public boolean movePlayer(int dRow, int dCol) {
        if (gameWon) {
            return false;
        }
        int newRow = playerRow + dRow;
        int newCol = playerCol + dCol;
        if (isValidMove(newRow, newCol)) {
            playerRow = newRow;
            playerCol = newCol;
            if (playerRow == goalRow && playerCol == goalCol) {
                gameWon = true;
            }
            return true;
        }
        return false;
    }

    public boolean moveBot() {
        if (gameWon) return false;

        if (botPathIndex < botPath.size()) {
            int[] nextMove = botPath.get(botPathIndex++);
            botRow = nextMove[0];
            botCol = nextMove[1];
            return true;
        }
        return false;
    }

    private boolean isValidMove(int row, int col) {
        return row > 0 && row < rows - 1 && col > 0 && col < cols - 1 && grid[row][col] != WALL;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int[] getPlayerPosition() {
        return new int[]{playerRow, playerCol};
    }

    public int[] getBotPosition() {
        return new int[]{botRow, botCol};
    }

    public int[] getGoalPosition() {
        return new int[]{goalRow, goalCol};
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean isGameWon() {
        return gameWon;
    }
}
