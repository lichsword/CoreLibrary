package org.lichsword.java.os;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AsyncTask<Params, Progress, Result> {

    abstract protected void onPreExecute();

    protected abstract Result doInBackground(Params... params);

    abstract protected void onPostExecute();

    private volatile Status mStatus = Status.PENDING;
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean();
    private final WorkerRunnable<Params, Result> mWorker;
    private final FutureTask<Result> mFuture;

    /**
     * Indicates the current status of the task. Each status will be set only
     * once during the lifetime of a task.
     */
    public enum Status {
        /**
         * Indicates that the task has not been executed yet.
         */
        PENDING,
        /**
         * Indicates that the task is running.
         */
        RUNNING,
        /**
         * Indicates that {@link AsyncTask#onPostExecute} has finished.
         */
        FINISHED,
    }

    private static class SerialExecutor implements Executor {

        @Override
        public void execute(Runnable command) {
            // TODO Auto-generated method stub

        }

    }

    public AsyncTask() {
        mWorker = new WorkerRunnable<Params, Result>() {
            @Override
            public Result call() throws Exception {
                // mTaskInvoked.set(true);

                // Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                return postResult(doInBackground(mParams));
            }
        };

        mFuture = new FutureTask<Result>(mWorker) {
            @Override
            protected void done() {
                try {
                    final Result result = get();

                    postResultIfNotInvoked(result);
                } catch (InterruptedException e) {
                    // android.util.Log.w(LOG_TAG, e);
                } catch (ExecutionException e) {
                    throw new RuntimeException("An error occured while executing doInBackground()", e.getCause());
                } catch (CancellationException e) {
                    postResultIfNotInvoked(null);
                } catch (Throwable t) {
                    throw new RuntimeException("An error occured while executing " + "doInBackground()", t);
                }
            }
        };
    }

    /**
     * An {@link Executor} that executes tasks one at a time in serial order.
     * This serialization is global to a particular process.
     */
    public static final Executor SERIAL_EXECUTOR = new SerialExecutor();

    private final Executor sDefaultExecutor = SERIAL_EXECUTOR;

    public final AsyncTask<Params, Progress, Result> execute(Params... params) {
        return executeOnExecutor(sDefaultExecutor, params);
    }

    public AsyncTask<Params, Progress, Result> executeOnExecutor(Executor exec, Params... params) {
        if (mStatus != Status.PENDING) {
            switch (mStatus) {
            case RUNNING:
                throw new IllegalStateException("Cannot execute task:" + " the task is already running.");
            case FINISHED:
                throw new IllegalStateException("Cannot execute task:" + " the task has already been executed "
                        + "(a task can be executed only once)");
            }
        }

        mStatus = Status.PENDING;

        onPreExecute();

        mWorker.mParams = params;

        sDefaultExecutor.execute(mFuture);

        return this;
    }

    private void postResultIfNotInvoked(Result result) {
        final boolean wasTaskInvoked = mTaskInvoked.get();
        if (!wasTaskInvoked) {
            postResult(result);
        }
    }

    private Result postResult(Result result) {
        // Message message = sHandler.obtainMessage(MESSAGE_POST_RESULT, new
        // AsyncTaskResult<Result>(this, result));
        // message.sendToTarget();
        return result;
    }

    private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params[] mParams;
    }

    private static class AsyncTaskResult<Data> {
        final AsyncTask mTask;
        final Data[] mData;

        AsyncTaskResult(AsyncTask task, Data... data) {
            mTask = task;
            mData = data;
        }
    }
}