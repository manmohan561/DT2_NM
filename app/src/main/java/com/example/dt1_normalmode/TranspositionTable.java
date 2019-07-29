package com.example.dt1_normalmode;

class TranspositionTable {

    private final static int log_size=23;
    private static long size ;
    private long[] K;
    private int[] V;

    private long index(long key)  {
        return key%size;
    }

    TranspositionTable() {
        size = next_prime(1 << log_size);
        K = new long[(int)size];
        V = new int[(int)size];

    }

    void reset() {
        for(int i=0;i<size;i++)
            K[i]=0;
        for(int i=0;i<size;i++)
            V[i]=0;
    }

    void put(long key, int value) {
        long pos = index(key);
        K[(int)pos] = key;
        V[(int)pos] = value;
    }

    int get(long key) {
        long pos = index(key);
        if(K[(int)pos] == key)
            return V[(int)pos];
        else return 0;
    }

    private long next_prime(long n) {
        return has_factor(n, 2, n) ? next_prime(n+1) : n;
    }

    private boolean has_factor(long n, long min, long max) {
        return min * min <= n && (min + 1 >= max ? n % min == 0 :
                has_factor(n, min, med(min, max)) || has_factor(n, med(min, max), max));
    }

    private long med(long min, long max) {
        return (min+max)/2;
    }
}


