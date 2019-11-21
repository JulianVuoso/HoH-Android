package ar.edu.itba.hci.hoh.notifications;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DatabaseHandler {

    public static Tuple getById(LocalDatabase db, final String id) {

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, workQueue);

        Tuple userData = null;

        Future<Tuple> future = threadPoolExecutor.submit(() -> db.tupleDao().getById(id));

        try {
            userData = future.get(500, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        future.cancel(true);
        return userData;
    }

    public static void insert(LocalDatabase db, final Tuple data){

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, workQueue);

        Future<Void> future = threadPoolExecutor.submit(() -> {
            db.tupleDao().insert(data);
            return null;
        });

        try {
            future.get(500,TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        future.cancel(true);
    }

    public static void delete(LocalDatabase db, final Tuple tuple) {

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, workQueue);

        Future<Void> future = threadPoolExecutor.submit(() -> {
            db.tupleDao().delete(tuple);
            return null;
        });

        try {
            future.get(500, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        future.cancel(true);
    }

    public static void deleteAll(LocalDatabase db) {

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, workQueue);

        Future<Void> future = threadPoolExecutor.submit(() -> {
            db.tupleDao().deleteAll();
            return null;
        });

        try {
            future.get(5000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        future.cancel(true);
    }

    public static List<Tuple> getMissing(LocalDatabase db, String[] ids) {

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, workQueue);

        List<Tuple> userData = null;

        Future<List<Tuple>> future = threadPoolExecutor.submit(() -> db.tupleDao().getMissing(ids));

        try {
            userData = future.get(500, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        future.cancel(true);
        return userData;
    }

    public static Integer getTotalCount(LocalDatabase db) {

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, workQueue);

        Integer userData = null;

        Future<Integer> future = threadPoolExecutor.submit(() -> db.tupleDao().getTotalCount());

        try {
            userData = future.get(500, TimeUnit.MILLISECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        future.cancel(true);
        return userData;
    }

}