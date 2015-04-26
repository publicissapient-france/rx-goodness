package fr.xebia.rx.goodness.observer;

import rx.Observer;

public class ThreadAwareLongPrinterObserver implements Observer<Long> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Long value) {
        System.out.println("onNext : Got a value on thread \"" + Thread.currentThread().getName() + "\" " + value);
    }
}
