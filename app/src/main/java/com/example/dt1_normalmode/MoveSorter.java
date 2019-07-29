package com.example.dt1_normalmode;

class MoveSorter {
    private int size;

    private class Entries{
        long[] move; int[] score;
        Entries() {
            move= new long[Position.WIDTH];
            score= new int[Position.WIDTH];
        }
    }
    private Entries entries;

    void add(long move, int score)
    {
        int pos = size++;
        for( ;(pos!=0) && entries.score[pos-1] > score ;--pos) {
            entries.move[pos] = entries.move[pos-1];
            entries.score[pos] = entries.score[pos-1];
        }
        this.entries.move[pos] = move;
        this.entries.score[pos] = score;
    }

    long getNext()
    {
        if(size!=0)
            return entries.move[--size];
        else
            return 0;
    }

    MoveSorter()
    {
        size=0;
        entries= new Entries();
    }
}

