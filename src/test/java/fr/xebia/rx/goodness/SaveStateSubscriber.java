package fr.xebia.rx.goodness;

import rx.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SaveStateSubscriber<T> extends Subscriber<T> {

    private State<T>  state = new State<>();
    private Semaphore semaphore;

    public SaveStateSubscriber() {
    }

    public SaveStateSubscriber(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void onCompleted() {
        state.completed();
        if(semaphore != null){
            semaphore.release();
        }
    }

    @Override
    public void onError(Throwable e) {
        state.addError(e);
        if(semaphore != null){
            semaphore.release();
        }
    }

    @Override
    public void onNext(T next) {
        state.addValue(next);
        if(semaphore != null){
            semaphore.release();
        }
    }

    public State<T> getState() {
        return state;
    }

    public static class State<T> {
        private boolean completed;
        private List<Throwable> errors = new ArrayList<>();
        private List<T> values = new ArrayList<>();

        public void completed(){
            this.completed = true;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void addError(Throwable e) {
            errors.add(e);
        }

        public List<Throwable> getErrors() {
            return errors;
        }

        public void addValue(T value) {
            values.add(value);
        }

        public List<T> getValues() {
            return values;
        }
    }
}
