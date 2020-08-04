package com.example.async;

import static java.lang.System.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FutureExample {
    interface SuccessCallback {
        void onSuccess(String result);
    }

    interface ExceptionCallback {
        void onError(Throwable t);
    }

    public static class CallbackFutureTask extends FutureTask<String> {
        private SuccessCallback successCallback;
        private ExceptionCallback exceptionCallback;

        public CallbackFutureTask(Callable<String> callable, @NonNull SuccessCallback successCallback,
                                  @NonNull ExceptionCallback exceptionCallback) {
            super(callable);
            this.successCallback = successCallback;
            this.exceptionCallback = exceptionCallback;
        }

        @Override
        protected void done() {
            try {
                successCallback.onSuccess(get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                exceptionCallback.onError(e.getCause());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        CallbackFutureTask callbackFutureTask = new CallbackFutureTask(() -> {
            Thread.sleep(1000);
            if (1 == 1) { throw new RuntimeException("Async Error!"); }
            log.info("+ World");
            return " World";
        }, result -> {
            String concat = result + " World";
            out.println(concat);
        }, error -> {
            out.println("Error: " + error.getMessage());
        });

        executorService.execute(callbackFutureTask);

        Future<String> future = executorService.submit(() -> {
            Thread.sleep(1000);
            log.info("Async-1");
            return "Hello";
        });

        executorService.execute(makeFuture());

        FutureTask<String> futureTask = new FutureTask<String>(() -> {
            Thread.sleep(1000);
            log.info("Async-2");
            return "Done";
        }) {
            @Override
            public boolean isDone() {
                out.println("Done");
                return super.isDone();
            }
        };

        executorService.execute(futureTask);

        out.println("callbackFutureTask: " + callbackFutureTask.isDone());
        out.println("future: " + future.isDone());
        Thread.sleep(2000);
        log.info("Exit");

        out.println("callbackFutureTask: " + callbackFutureTask.isDone());
        out.println("future: " + future.isDone());
        log.info(future.get());
        log.info(callbackFutureTask.get());
        executorService.shutdown();
    }

    private static FutureTask makeFuture() {
//        Future와 callback이 모두 가진 Future의 인터페이스
        return new FutureTask<>(() -> {
            Thread.sleep(2000);
            log.info("FutureTask Async");
            return "Hello";
        });
    }
}
