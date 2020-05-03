package gr.uoa.di.softeng.data.model;

/**
 *
 */
public class Limits {

    public static final int DEFAULT_COUNT = 10;

    private final long start;
    private final int count;

    public Limits() {

        this(0L);
    }

    public Limits(Long start) {

        this(start, DEFAULT_COUNT);
    }

    public Limits(Long start, Integer count) {

        this.start = start;
        this.count = count;
    }

    public long getStart() {

        return start;
    }

    public int getCount() {

        return count;
    }

}
