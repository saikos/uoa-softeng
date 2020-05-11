package gr.uoa.di.softeng.data.model;

/**
 *
 */
public class Limits {

    private static final long DEFAULT_START = 0L;
    private static final int DEFAULT_COUNT = 10;

    private final long start;
    private final int count;

    public Limits() {

        this(DEFAULT_START, DEFAULT_COUNT);
    }

    public Limits(Long start) {

        this(start, DEFAULT_COUNT);
    }

    public Limits(Long start, Integer count) {

        this.start = start == null ? DEFAULT_START : start;
        this.count = count == null ? DEFAULT_COUNT : count;
    }

    public long getStart() {

        return start;
    }

    public int getCount() {

        return count;
    }

}
