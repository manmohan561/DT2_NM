package com.example.dt1_normalmode;

class Solver {

    private int[] columnOrder;

    private TranspositionTable transTable;

    private int negamax(final Position P, int alpha, int beta) {

        long possible = P.possibleNonLosingMoves();
        if(possible == 0)
            return -(Position.WIDTH*Position.HEIGHT - P.nbMoves())/2;

        if(P.nbMoves() >= Position.WIDTH*Position.HEIGHT - 2)
            return 0;

        int min = -(Position.WIDTH*Position.HEIGHT-2 - P.nbMoves())/2;
        if(alpha < min) {
            alpha = min;
            if(alpha >= beta) return alpha;
        }

        int max = (Position.WIDTH*Position.HEIGHT-1 - P.nbMoves())/2;
        if(beta > max) {
            beta = max;
            if(alpha >= beta) return beta;
        }

        final long key = P.key();
        int val = transTable.get(key);
        if(val!=0) {
            if(val > Position.MAX_SCORE - Position.MIN_SCORE + 1) {
                min = val + 2*Position.MIN_SCORE - Position.MAX_SCORE - 2;
                if(alpha < min) {
                    alpha = min;
                    if(alpha >= beta) return alpha;
                }
            }
            else {
                max = val + Position.MIN_SCORE - 1;
                if(beta > max) {
                    beta = max;
                    if(alpha >= beta) return beta;
                }
            }
        }

        MoveSorter moves= new MoveSorter();

        for(int i = Position.WIDTH-1;i>=0 ;i-- ){
            long move = possible & (((1L << Position.HEIGHT)-1) << columnOrder[i]*(Position.HEIGHT+1));
            if(move!=0)
                moves.add(move, P.moveScore(move));
        }
        long next= moves.getNext();
        while(next!=0) {
            next= moves.getNext();
            Position P2= new Position(P);
            P2.play(next);
            int score = -negamax(P2, -beta, -alpha);

            if(score >= beta) {
                transTable.put(key, score + Position.MAX_SCORE - 2*Position.MIN_SCORE + 2);
                return score;
            }
            if(score > alpha) alpha = score;

        }

        transTable.put(key, alpha - Position.MIN_SCORE + 1);
        return alpha;
    }

    int solve(final Position P) {
        if(P.canWinNext())
            return (Position.WIDTH*Position.HEIGHT+1 - P.nbMoves())/2;
        int min = -(Position.WIDTH*Position.HEIGHT - P.nbMoves())/2;
        int max = (Position.WIDTH*Position.HEIGHT+1 - P.nbMoves())/2;

        while(min < max) {
            int med = min + (max - min)/2;
            if(med <= 0 && min/2 < med) med = min/2;
            else if(med >= 0 && max/2 > med) med = max/2;
            int r = negamax(P, med, med + 1);
            if(r <= med) max = r;
            else min = r;
        }
        return min;
    }

    void reset() {
        transTable.reset();
    }

    Solver(){
        columnOrder= new int[Position.WIDTH];
        transTable= new TranspositionTable() ;
        for(int i = 0; i < Position.WIDTH; i++)
            columnOrder[i] = Position.WIDTH/2 + (1-2*(i%2))*(i+1)/2;
    }

}
