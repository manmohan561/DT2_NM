package com.example.dt1_normalmode;

class Position {
    static int WIDTH ;
    static int HEIGHT ;
    static int MIN_SCORE ;
    static int MAX_SCORE ;

    static void initialisePosition(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        MIN_SCORE = -(WIDTH*HEIGHT)/2 + 3;
        MAX_SCORE = (WIDTH*HEIGHT+1)/2 - 3;
        bottom_mask = bottom(WIDTH, HEIGHT);
        board_mask = bottom_mask * ((1L << HEIGHT)-1);
    }
    void play(long move) {
        current_position ^= mask;
        mask |= move;
        moves++;
    }

    boolean canWinNext() {
        return ((winning_position() & possible())!=0) ;
    }

    int nbMoves() {
        return moves;
    }

    long key()  {
        return current_position + mask;
    }

    long possibleNonLosingMoves() {
        long possible_mask = possible();
        long opponent_win = opponent_winning_position();
        long forced_moves = possible_mask & opponent_win;
        if(forced_moves!=0) {
            if((forced_moves & (forced_moves - 1))!=0)
                return 0;
            else possible_mask = forced_moves;
        }
        return possible_mask & ~(opponent_win >> 1);
    }

    int moveScore(long move) {
        return popcount(compute_winning_position(current_position | move, mask));
    }

    void playCol(int col) {
        play((mask + (1L << col*(HEIGHT+1))) & column_mask(col));
    }

    Position() {
        current_position= 0;
        mask= 0;
        moves=0;
    }

    Position(Position P) {
        this.current_position=P.current_position;
        this.mask= P.mask;
        this.moves= P.moves;
    }

    private long current_position;
    private long mask;
    private int moves;

    boolean canPlay(int col) {
        return (mask &  (1L << ((HEIGHT - 1) + col*(HEIGHT+1)))) == 0;
    }

    private boolean isWinningMove(int col) {
        return ((winning_position() & possible() & column_mask(col))!=0) ;
    }

    private long winning_position() {
        return compute_winning_position(current_position, mask);
    }

    private long opponent_winning_position() {
        return compute_winning_position(current_position ^ mask, mask);
    }

    private long possible() {
        return (mask + bottom_mask) & board_mask;
    }

    private static int popcount(long m) {
        int c ;
        for (c = 0; m!=0; c++)
            m &= m - 1;
        return c;
    }

    private static long compute_winning_position(long position, long mask) {
        // vertical;
        long r = (position << 1) & (position << 2) & (position << 3);

        //horizontal
        long p = (position << (HEIGHT+1)) & (position << 2*(HEIGHT+1));
        r |= p & (position << 3*(HEIGHT+1));
        r |= p & (position >> (HEIGHT+1));
        p = (position >> (HEIGHT+1)) & (position >> 2*(HEIGHT+1));
        r |= p & (position << (HEIGHT+1));
        r |= p & (position >> 3*(HEIGHT+1));

        //diagonal 1
        p = (position << HEIGHT) & (position << 2*HEIGHT);
        r |= p & (position << 3*HEIGHT);
        r |= p & (position >> HEIGHT);
        p = (position >> HEIGHT) & (position >> 2*HEIGHT);
        r |= p & (position << HEIGHT);
        r |= p & (position >> 3*HEIGHT);

        //diagonal 2
        p = (position << (HEIGHT+2)) & (position << 2*(HEIGHT+2));
        r |= p & (position << 3*(HEIGHT+2));
        r |= p & (position >> (HEIGHT+2));
        p = (position >> (HEIGHT+2)) & (position >> 2*(HEIGHT+2));
        r |= p & (position << (HEIGHT+2));
        r |= p & (position >> 3*(HEIGHT+2));

        return r & (board_mask ^ mask);
    }

    private static long bottom_mask ;
    private static long board_mask ;

    private static long column_mask(int col) {
        return (((1L << HEIGHT)-1) << col*(HEIGHT+1));
    }

    private static long bottom(int width, int height) {
        return width == 0 ? 0 : bottom(width-1, height) | 1L << (width-1)*(height+1);
    }
}
