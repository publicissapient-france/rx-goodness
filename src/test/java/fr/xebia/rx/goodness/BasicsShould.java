package fr.xebia.rx.goodness;

import fr.xebia.rx.goodness.SaveStateSubscriber.State;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicsShould {

    private Basics basics = new Basics();

    @Test
    public void create_observable_and_output_hello_word() throws Exception {
        // Given
        SaveStateSubscriber<String> subscriber = new SaveStateSubscriber<>();

        // When
        basics.outputHelloWord(subscriber);

        // Then
        State<String> state = subscriber.getState();
        assertThat(state.isCompleted()).isTrue();
        assertThat(state.getErrors()).isEmpty();
        assertThat(state.getValues()).containsOnly("Hello, world!");
    }

    @Test
    public void use_just_observable_and_output_hello_word() throws Exception {
        // Given
        SaveStateSubscriber<String> subscriber = new SaveStateSubscriber<>();

        // When
        basics.justOutputHelloWord(subscriber);

        // Then
        State<String> state = subscriber.getState();
        assertThat(state.isCompleted()).isTrue();
        assertThat(state.getErrors()).isEmpty();
        assertThat(state.getValues()).containsOnly("Hello, world!");
    }

    @Test
    public void append_text_to_hello_word() throws Exception {
        // Given
        String textToAppend = " Hey !";
        SaveStateSubscriber<String> subscriber = new SaveStateSubscriber<>();

        // When
        basics.appendTextToHelloWord(textToAppend, subscriber);

        // Then
        State<String> state = subscriber.getState();
        assertThat(state.isCompleted()).isTrue();
        assertThat(state.getErrors()).isEmpty();
        assertThat(state.getValues()).containsOnly("Hello, world!" + textToAppend);
    }

    @Test
    public void multiply_even_numbers_by_2() throws Exception {
        // Given
        int by = 2;
        SaveStateSubscriber<Integer> subscriber = new SaveStateSubscriber<>();

        // When
        basics.multiplyEvenBy(by, subscriber);

        // Then
        State<Integer> state = subscriber.getState();
        assertThat(state.isCompleted()).isTrue();
        assertThat(state.getErrors()).isEmpty();
        assertThat(state.getValues()).containsExactly(0, 4, 8, 12);
    }

    @Test
    public void repeat_3_times_values() throws Exception {
        // Given
        int times = 3;
        SaveStateSubscriber<Integer> subscriber = new SaveStateSubscriber<>();

        // When
        basics.repeat(times, subscriber);

        // Then
        State<Integer> state = subscriber.getState();
        assertThat(state.isCompleted()).isTrue();
        assertThat(state.getErrors()).isEmpty();
        assertThat(state.getValues()).containsExactly(0, 1, 2, 3, 4, 5, 6, 0, 1, 2, 3, 4, 5, 6, 0, 1, 2, 3, 4, 5, 6);
    }

    @Test
    public void repeat_3_times_each_number() throws Exception {
        // Given
        int times = 3;
        SaveStateSubscriber<Integer> subscriber = new SaveStateSubscriber<>();

        // When
        basics.repeatEachNumber(times, subscriber);

        // Then
        State<Integer> state = subscriber.getState();
        assertThat(state.isCompleted()).isTrue();
        assertThat(state.getErrors()).isEmpty();
        assertThat(state.getValues()).containsExactly(0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6);
    }

    @Test
    public void repeat_3_times_only_odd_numbers() throws Exception {
        // Given
        int times = 3;
        SaveStateSubscriber<Integer> subscriber = new SaveStateSubscriber<>();

        // When
        basics.repeatOnlyOddNumbers(times, subscriber);

        // Then
        State<Integer> state = subscriber.getState();
        assertThat(state.isCompleted()).isTrue();
        assertThat(state.getErrors()).isEmpty();
        assertThat(state.getValues()).containsExactly(0, 1, 1, 1, 2, 3, 3, 3, 4, 5, 5, 5, 6);
    }

    @Test
    public void repeat_3_times_only_odd_numbers_and_keep_first_5_elements() throws Exception {
        // Given
        int times = 3;
        int keep = 5;
        SaveStateSubscriber<Integer> subscriber = new SaveStateSubscriber<>();

        // When
        basics.repeatOnlyOddNumbersAndKeep(times, keep, subscriber);

        // Then
        State<Integer> state = subscriber.getState();
        assertThat(state.isCompleted()).isTrue();
        assertThat(state.getErrors()).isEmpty();
        assertThat(state.getValues()).containsExactly(0, 1, 1, 1, 2);
    }

    @Test
    public void output_error_when_value_is_2() throws Exception {
        // Given
        int value = 2;
        SaveStateSubscriber<Integer> subscriber = new SaveStateSubscriber<>();

        // When
        basics.outputErrorWhenValueIs(value, subscriber);

        // Then
        State<Integer> state = subscriber.getState();
        assertThat(state.isCompleted()).isFalse();
        assertThat(state.getErrors()).extracting("message").containsOnly("Oups");
        assertThat(state.getValues()).containsExactly(0, 1);
    }

    @Test
    public void resume_after_error_when_value_is_2_then_multiply_by_2() throws Exception {
        // Given
        int by = 2;
        int value = 2;
        SaveStateSubscriber<Integer> subscriber = new SaveStateSubscriber<>();

        // When
        basics.multiplyByAfterErrorWhenValueIs(by, value, subscriber);

        // Then
        State<Integer> state = subscriber.getState();
        assertThat(state.isCompleted()).isTrue();
        assertThat(state.getErrors()).isEmpty();
        assertThat(state.getValues()).containsExactly(0, 1, 0, 2, 4, 6, 8, 10, 12);
    }

    @Test
    public void return_42_after_error_when_value_is_2() throws Exception {
        // Given
        int value = 2;
        SaveStateSubscriber<Integer> subscriber = new SaveStateSubscriber<>();

        // When
        basics.return42AfterErrorWhenValueIs(value, subscriber);

        // Then
        State<Integer> state = subscriber.getState();
        assertThat(state.isCompleted()).isTrue();
        assertThat(state.getErrors()).isEmpty();
        assertThat(state.getValues()).containsExactly(0, 1, 42);
    }

    @Test
    public void retry_three_times_after_error_when_value_is_2() throws Exception {
        // Given
        int value = 2;
        int times = 3;
        SaveStateSubscriber<Integer> subscriber = new SaveStateSubscriber<>();

        // When
        basics.retryWhenValueIs(times, value, subscriber);

        // Then
        State<Integer> state = subscriber.getState();
        assertThat(state.isCompleted()).isFalse();
        assertThat(state.getErrors()).hasSize(1);
        assertThat(state.getErrors()).extracting("message").containsExactly("Oups");
        assertThat(state.getValues()).containsExactly(0, 1, 0, 1, 0, 1, 0, 1);
    }

    @Test
    public void match_sentence_with_number() throws Exception {
        // Given
        SaveStateSubscriber<String> subscriber = new SaveStateSubscriber<>();

        // When
        basics.matchSentenceWithNumber(subscriber);

        // Then
        State<String> state = subscriber.getState();
        assertThat(state.isCompleted()).isTrue();
        assertThat(state.getErrors()).isEmpty();
        assertThat(state.getValues()).containsExactly("Hey 0", "Ho 1", "On rentre 2");
    }

    @Test
    public void concat_strings_with_accumulate_operator() throws Exception {
        // Given
        SaveStateSubscriber<String> subscriber = new SaveStateSubscriber<>();

        // When
        basics.matchSentenceWithNumber2(subscriber);

        // Then
        State<String> state = subscriber.getState();
        assertThat(state.isCompleted()).isTrue();
        assertThat(state.getErrors()).isEmpty();
        assertThat(state.getValues()).containsExactly("Hey Ho On rentre");
    }


}
