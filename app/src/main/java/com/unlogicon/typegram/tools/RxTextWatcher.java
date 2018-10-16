package com.unlogicon.typegram.tools;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Nikita Korovkin 16.10.2018.
 */
public class RxTextWatcher implements TextWatcher {

    protected String previousText;

    protected String text;

    protected PublishSubject<String> subject;

    protected WeakReference<EditText> editTextReference;

    public Observable<String> watch(EditText editText){
        editTextReference = new WeakReference<>(editText);

        editText.addTextChangedListener(this);

        text = editText.getText().toString();

        subject = PublishSubject.create();

        return subject;
    }

    public RxTextWatcher() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        previousText = text;
        text = s.toString();

        if(subject != null){
            subject.onNext(s.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public String getText() {
        return text;
    }
}
