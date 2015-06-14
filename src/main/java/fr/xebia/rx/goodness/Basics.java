package fr.xebia.rx.goodness;

import fr.xebia.rx.goodness.observer.ThreadAwareLongPrinterObserver;
import fr.xebia.rx.goodness.observer.ThreadAwareStringPrinterObserver;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Basics {

    private static final List<Integer> INTEGERS = Arrays.asList(0, 1, 2, 3, 4, 5, 6);

    private static final List<String> SENTENCES = Arrays.asList("Hey", "Ho", "On rentre");


    private static int count = 0;

    public void outputHelloWord(Subscriber<String> subscriber) {
        Observable<String> observable = Observable.create(
            new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> sub) {
                    sub.onNext("Hello, world!");
                    sub.onCompleted();
                }
            }
        );
        observable.subscribe(subscriber);
    }

    public void justOutputHelloWord(Subscriber<String> subscriber) {
        Observable.just("Hello, world!").subscribe(subscriber);
    }

    public void appendTextToHelloWord(String textToAppend, Subscriber<String> subscriber) {
        Observable.just("Hello, world!").map(s -> s + textToAppend).subscribe(subscriber);
    }

    public void multiplyEvenBy(int by, Subscriber<Integer> subscriber) {
        Observable.from(INTEGERS).
            filter(x -> x % 2 == 0).
            map(x -> x * by).
            subscribe(subscriber);
    }

    public void repeat(int times, Subscriber<Integer> subscriber) {
        Observable.from(INTEGERS).
            repeat(times).
            subscribe(subscriber);
    }

    public void repeatEachNumber(int times, Subscriber<Integer> subscriber) {
        Observable.from(INTEGERS).
            flatMap(c -> Observable.just(c).repeat(times)).
            subscribe(subscriber);
    }

    public void repeatOnlyOddNumbers(int times, Subscriber<Integer> subscriber) {
        Observable.from(INTEGERS).
            flatMap(c -> {
                if (c % 2 == 1) {
                    return Observable.just(c).repeat(times);
                } else {
                    return Observable.just(c);
                }
            }).
            subscribe(subscriber);
    }

    public void repeatOnlyOddNumbersAndKeep(int times, int keep, Subscriber<Integer> subscriber) {
        Observable.from(INTEGERS).
            flatMap(c -> {
                if (c % 2 == 1) {
                    return Observable.just(c).repeat(times);
                } else {
                    return Observable.just(c);
                }
            }).
            take(keep).
            subscribe(subscriber);
    }


    public void outputErrorWhenValueIs(int value, Subscriber<Integer> subscriber) {
        Observable.from(INTEGERS).
            map(x -> {
                if (x == value) {
                    throw new RuntimeException("Oups");
                }
                return x;
            }).
            subscribe(subscriber);
    }

    public void multiplyByAfterErrorWhenValueIs(int by, int value, Subscriber<Integer> subscriber) {
        Observable.from(INTEGERS)
            .map(x -> {
                if (x == value) {
                    throw new RuntimeException("Oups");
                }
                return x;
            })
            .onErrorResumeNext(Observable.from(INTEGERS).map(x -> by * x))
            .subscribe(subscriber);
    }

    public void return42AfterErrorWhenValueIs(int value, Subscriber<Integer> subscriber) {
        Observable.from(INTEGERS).
            map(x -> {
                if (x == value) {
                    throw new RuntimeException("Oups");
                }
                return x;
            }).
            doOnError(System.out::println).
            onErrorReturn(x -> 42).
            subscribe(subscriber);
    }


    public void retryWhenValueIs(int times,int value, Subscriber<Integer> subscriber) {
        Observable.from(INTEGERS).
            map(x -> {
                if (x == value) {
                    throw new RuntimeException("Oups");
                }
                return x;
            }).
            retry(times).
            subscribe(subscriber);
    }

    public void matchSentenceWithNumber(Subscriber<String> subscriber) {
        Observable.zip(Observable.from(SENTENCES), Observable.from(INTEGERS), (x, y) -> x + " " + y).
            subscribe(subscriber);
    }

    public void matchSentenceWithNumber2(Subscriber<String> subscriber) {
        Observable.from(SENTENCES)
            .scan((x, y) -> x + " " + y)
            .last()
            .subscribe(subscriber);
    }

    public static void main(String[] args) {

        /* Simple thread aware observer */
        Observable.just("Hello").subscribe(new ThreadAwareStringPrinterObserver());

        /* SubscribeOn / ObserveOn ne marche pas car le programme se termine avant */
        Observable.interval(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .subscribe(new ThreadAwareLongPrinterObserver());
        /*while (true) ;*/
    }
}
