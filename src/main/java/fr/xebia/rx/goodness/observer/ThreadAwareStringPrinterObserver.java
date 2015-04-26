package fr.xebia.rx.goodness.observer;

public class ThreadAwareStringPrinterObserver extends StringPrinterObserver {
    @Override
    public void onNext(String string) {
        System.out.println("onNext : Got a value on thread \"" + Thread.currentThread().getName() + "\" " + string);
    }
}
