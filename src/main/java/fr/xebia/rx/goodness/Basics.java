package fr.xebia.rx.goodness;

import fr.xebia.rx.goodness.observer.IntegerPrinterObserver;
import fr.xebia.rx.goodness.observer.StringPrinterObserver;
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

    public static void main(String[] args) {

        /* Create observable + first subscriber */
        Observable<String> myObservable = Observable.create(
            new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> sub) {
                    sub.onNext("Hello, world!");
                    sub.onCompleted();
                }
            }
        );
        myObservable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });


        /* Just + ignore (completed/error) */
        Observable.just("Hello, world!").subscribe(System.out::println);

        /* Append text first approach */
        Observable.just("Hello, world!").subscribe(s -> System.out.println(s + " You"));

        /* Append text second approach */
        Observable.just("Hello, world!").map(s -> s + " You").subscribe(System.out::println);

        /* Filter and map (multiply by 2 only even numbers)*/
        Observable.from(INTEGERS).
            filter(x -> x % 2 == 0).
            map(x -> x * 2).
            subscribe(System.out::println);

        /* Repeat (repeat 3 times the list) */
        Observable.from(INTEGERS).
            repeat(3).
            subscribe(System.out::println);

        /* Flatmap and repeat(repeat 3 times every number) */
        Observable.from(INTEGERS).
            flatMap(c -> Observable.just(c).repeat(3)).
            repeat(3).
            subscribe(System.out::println);

         /* Filter flatMap and repeat (repeat 3 times only odd numbers) */
        Observable.from(INTEGERS).
            flatMap(c -> {
                if (c % 2 == 0) {
                    return Observable.just(c).repeat(3);
                } else {
                    return Observable.just(c);
                }
            }).
            subscribe(new IntegerPrinterObserver());

        /* Filter flatMap repeat take (repeat 3 times only odd numbers and keep 5 elements) */
        Observable.from(INTEGERS).
            flatMap(c -> {
                if (c % 2 == 0) {
                    return Observable.just(c).repeat(3);
                } else {
                    return Observable.just(c);
                }
            }).
            take(5).
            subscribe(new IntegerPrinterObserver());


        /* Basic Errors */
        Observable.from(INTEGERS).
            map(x -> {
                if (x == 2) {
                    throw new RuntimeException("Oups");
                }
                return x;
            }).
            subscribe(new IntegerPrinterObserver());


        /* Errors resume */
        Observable.from(INTEGERS)
            .map(x -> {
                if (x % 2 != 0) {
                    throw new RuntimeException("Oups");
                }
                return x;
            })
            .onErrorResumeNext(Observable.from(INTEGERS).map(x -> 2 * x))
            .subscribe(new IntegerPrinterObserver());


        /* Errors return */
        Observable.from(INTEGERS).
            map(x -> {
                if (x == 2) {
                    throw new RuntimeException("Oups");
                }
                return x;
            }).
            doOnError(System.out::println).
            onErrorReturn(x -> 42).
            subscribe(new IntegerPrinterObserver());

        /* Errors retry */
        Observable.from(INTEGERS).
            map(x -> {
                if (x > count) {
                    count++;
                    throw new RuntimeException("Oups");
                }
                return x;
            }).
            retry().
            subscribe(new IntegerPrinterObserver());

        /* Simple thread aware observer */
        Observable.just("Hello").subscribe(new ThreadAwareStringPrinterObserver());

        /* SubscribeOn / ObserveOn ne marche pas car le programme se termine avant */
        Observable.interval(5, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .subscribe(new ThreadAwareLongPrinterObserver());
        /*while (true) ;*/


        /* Zip */
        Observable.zip(Observable.from(SENTENCES), Observable.from(INTEGERS), (x, y) -> x + " " + y).subscribe(new StringPrinterObserver());

        /* Accumulator sample */
        Observable.from(SENTENCES)
            .scan((x, y) -> x + " " + y)
            .last()
            .subscribe(new StringPrinterObserver());
    }
}
