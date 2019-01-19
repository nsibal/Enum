/**
 * Implementation of Data Structures & Algorithms
 * Fall 2018
 * SP 11
 *
 * Comparison of Performances of selection of k largest elements
 * between QuickSort's Partition method and Priority Queue
 *
 * @author : Nirbhay Sibal nxs180002
 * @author : Sudeep Maity sdm170530
 */

package nxs180002;

import java.util.PriorityQueue;
import java.util.Random;

public class EnumSP11 {

    public static Random random = new Random();
    public static int numTrials = 100;

    /**Threshold*/
    public static int T = 11;

    /**
     * Select k largest elements in the array
     * Result in arr[arr.length-k ... arr.length-1]
     * @param arr : array
     * @param k : number of largest elements
     */
    public static void select(int[] arr, int k) {
        select(arr, 0, arr.length, k);
    }

    /**
     * Find the kth largest element of arr[p ... p+n-1]
     * @param arr : array
     * @param p : start index
     * @param n : end index
     * @param k : kth largest element
     * @return : index of the kth largest element
     */
    public static int select(int[] arr, int p, int n, int k) {
        if (n<T) {
            insertionSort(arr, p, p+n-1);
            return n-k;
        } else {
            int q = randomizedPartition(arr, p, p+n-1);
            int left = q-p;
            int right = n-left-1;

            if (right>=k) {
                return select(arr, q+1, right, k);
            } else if (right+1 == k) {
                return q;
            } else {
                return select(arr, p, left, k-right-1);
            }
        }
    }

    /**
     * Sorting the input array using Insertion Sort
     * @param arr : array to be sorted
     * @param p : from index
     * @param r : to index
     */
    private static void insertionSort(int[] arr, int p, int r) {
        for (int i=p+1;i<=r;i++) {
            int tmp = arr[i];
            int j = i-1;
            while (j>=p && tmp<arr[j]) {
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = tmp;
        }
    }

    /**
     * Randomized Partition method
     * @param arr : array
     * @param p : left index
     * @param r : right index
     *
     * @return pivot element index
     */
    private static int randomizedPartition(int[] arr, int p, int r) {
        int q = p-1;
        int tmp = p;

        int randIdx;

        if (r==p) {
            randIdx = p;
        } else {
            randIdx = p + new Random().nextInt(r - p);
        }

        swap(arr, randIdx, r);

        while (tmp<r) {
            if (arr[tmp]<=arr[r]) {
                q++;
                swap(arr, q, tmp);
            }
            tmp++;
        }
        swap(arr, q+1, r);
        return q+1;
    }

    /**
     * Swaps the elements on indexes x and y in the array arr.
     * @param arr : array
     * @param x
     * @param y
     */
    private static void swap(int[] arr, int x, int y) {
        int tmp = arr[x];
        arr[x] = arr[y];
        arr[y] = tmp;
    }

    /**
     * Priority Queue based implementation of finding the stream of k largest elements in the array.
     * @param arr : array
     * @param k : number of largest elements in the array
     */
    public static void PQkLargestStream(int[] arr, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i : arr) {
            if (pq.size()<k) {
                pq.add(i);
            } else {
                if (i>pq.peek()) {
                    pq.add(i);
                    pq.remove();
                }
            }
        }
    }

    /**
     * Main Method
     * @param args : args[0] -  Size of the array
     *             : args[1] :
     *               Choice: 1 - Select Method
     *               Choice: 2 - Priority Queue Method
     */
    public static void main(String[] args) {
        int n = 100;
        int choice = 1 + random.nextInt(2);

        if(args.length > 0) {
            n = Integer.parseInt(args[0]);
        }

        if(args.length > 1) {
            choice = Integer.parseInt(args[1]);
        }

        int k = n/2;

        int[] arr = new int[n];
        for(int i=0; i<n; i++) {
            arr[i] = i;
        }
        Timer timer = new Timer();
        switch(choice) {
            case 1: // Select
                for(int i=0; i<numTrials; i++) {
                    Shuffle.shuffle(arr);
                    select(arr, k);
                }
                break;
            case 2: // PQ
                for(int i=0; i<numTrials; i++) {
                    Shuffle.shuffle(arr);
                    PQkLargestStream(arr, k);
                }
                break;
        }
        timer.end();
        timer.scale(numTrials);

        System.out.println("Choice: " + choice + "\n" + timer);
    }

    /** Timer class for roughly calculating running time of programs
     *  @author rbk
     *  Usage:  Timer timer = new Timer();
     *          timer.start();
     *          timer.end();
     *          System.out.println(timer);  // output statistics
     */
    public static class Timer {
        long startTime, endTime, elapsedTime, memAvailable, memUsed;
        boolean ready;

        public Timer() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public void start() {
            startTime = System.currentTimeMillis();
            ready = false;
        }

        public Timer end() {
            endTime = System.currentTimeMillis();
            elapsedTime = endTime-startTime;
            memAvailable = Runtime.getRuntime().totalMemory();
            memUsed = memAvailable - Runtime.getRuntime().freeMemory();
            ready = true;
            return this;
        }

        public long duration() {
            if(!ready) {
                end();
            }
            return elapsedTime;
        }

        public long memory() {
            if(!ready) {
                end();
            }
            return memUsed;
        }

        public void scale(int num) {
            elapsedTime /= num;
        }

        public String toString() {
            if(!ready) {
                end();
            }
            return "Time: " + elapsedTime + " msec.\n" + "Memory: " + (memUsed/1048576) + " MB / " + (memAvailable/1048576) + " MB.";
        }
    }

    /**
     * @author rbk : based on algorithm described in a book
     */


    /* Shuffle the elements of an array arr[from..to] randomly */
    public static class Shuffle {

        public static void shuffle(int[] arr) {
            shuffle(arr, 0, arr.length-1);
        }

        public static<T> void shuffle(T[] arr) {
            shuffle(arr, 0, arr.length-1);
        }

        public static void shuffle(int[] arr, int from, int to) {
            int n = to - from  + 1;
            for(int i=1; i<n; i++) {
                int j = random.nextInt(i);
                swap(arr, i+from, j+from);
            }
        }

        public static<T> void shuffle(T[] arr, int from, int to) {
            int n = to - from  + 1;
            Random random = new Random();
            for(int i=1; i<n; i++) {
                int j = random.nextInt(i);
                swap(arr, i+from, j+from);
            }
        }

        static void swap(int[] arr, int x, int y) {
            int tmp = arr[x];
            arr[x] = arr[y];
            arr[y] = tmp;
        }

        static<T> void swap(T[] arr, int x, int y) {
            T tmp = arr[x];
            arr[x] = arr[y];
            arr[y] = tmp;
        }

        public static<T> void printArray(T[] arr, String message) {
            printArray(arr, 0, arr.length-1, message);
        }

        public static<T> void printArray(T[] arr, int from, int to, String message) {
            System.out.print(message);
            for(int i=from; i<=to; i++) {
                System.out.print(" " + arr[i]);
            }
            System.out.println();
        }
    }
}
