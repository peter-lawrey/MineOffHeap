package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public class IntCache {
    private static final int CACHE_LIMIT = 1024;
    private static int largeSize = 256;
    private static List<int[]> smallFree = new ArrayList();
    private static List<int[]> smallUsed = new ArrayList();
    private static List<int[]> largeFree = new ArrayList();
    private static List<int[]> largeUsed = new ArrayList();

    public static synchronized int[] a(int capacity) {
        if (capacity <= 256) {
            if (smallFree.isEmpty()) {
                return addAndReturn(new int[256]);
            }
            return addAndReturn(smallFree.remove(smallFree.size() - 1));
        }
        if (capacity > largeSize) {
            largeSize = capacity;

            largeFree.clear();
            largeUsed.clear();
        }
        if (largeFree.isEmpty()) {
            return addLargeAndReturn(new int[largeSize]);
        }
        return addLargeAndReturn(largeFree.remove(largeFree.size() - 1));
    }

    private static int[] addLargeAndReturn(int[] arrayOfInt) {
        if (largeUsed.size() < CACHE_LIMIT)
            largeUsed.add(arrayOfInt);
        return arrayOfInt;
    }

    private static int[] addAndReturn(int[] arrayOfInt) {
        if (smallUsed.size() < CACHE_LIMIT)
            smallUsed.add(arrayOfInt);
        return arrayOfInt;
    }

    public static synchronized void a() {
        if (!largeFree.isEmpty()) {
            largeFree.remove(largeFree.size() - 1);
        }
        if (!smallFree.isEmpty()) {
            smallFree.remove(smallFree.size() - 1);
        }
        largeFree.addAll(largeUsed);
        smallFree.addAll(smallUsed);

        largeUsed.clear();
        smallUsed.clear();
    }

    public static synchronized String b() {
        return "cache: " + largeFree.size() + ", tcache: " + smallFree.size() + ", allocated: " + largeUsed.size() + ", tallocated: " + smallUsed.size();
    }
}
