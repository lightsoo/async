## ExecutorService
ThreadPool을 사용해서 Thread 생성 리소스를 최소화
```java
    ExecutorService executorService = Executors.newCachedThreadPool();
```

### ExecutorService#submit()
- ThreadPool에 작업을 전달하고 결과를 가져오는것

### ExecutorService#execute()
- Future를 전달 받아 비동기 작업을 실행하는 메서드

## Future
- 비동기 작업에 대한 결과를 가지고 있어 이를 표현 하는것.
    - 비동기 작업의 리턴값은 아니고 future를 통해서 리턴값을 가져오는 방법을 제공하는 Handler
    - 비동기 작업의 결과를 가져오는 수단 
- 현재 작업을 수행하는 스레드가 아닌 새로운 스레드를 열어서 별개의 작업을 수행한다.
- 같은 스레드 안에서 메서드 리턴값으로 결과를 받을수 있지만, 
다른 스레드의 결과(stack trace)를 가져오기 위해선 Future를 사용해서 가져올수 있다.

###Future#get()
- 비동기 작업에 대한 결과를 가져오는 메서드 
- 비동기 작업의 결과를 가져 올때 까지 Blocking 된 상태
    - 비동기 결과를 가져오는 수단인데 Blocking이 되는게 어색하다는 의견

## FutureTask 
- Future + callback 이 들어있는 클래스
```java
FutureTask<String> f = new FutureTask(() -> {
    Thread.sleep(1000);
    log.info("Async");
    return "Hello"
});
```

## CallbackFutureTask extends FutureTask
- Callable: 결과값이 있는 비동기 작업 
- SuccessCallback: 비동기 작업이 정상적으로 수행 됐을때 처리 할 인터페이스

## AsynchronousByteChannel
- 비동기 작업의 결과를 가져와서 callback으로 넘기는 기법 
### #<A> void read(... CompletionHandler) 
- 비동기적으로 결과를 응답하는건 아니고 Handler의 completed(), failed()를 통해 Callback 으로 결과값을 가져와서 처리한다.
- 1.8인 경우 각각의 CompletedCompletionHandler, FailedCompletionHandler 인터페이스를 만들면 람다식으로 처리 가능.

