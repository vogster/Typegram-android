package com.unlogicon.typegram.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.enums.EditorEnum;
import com.unlogicon.typegram.utils.EditorPreferencesUtils;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Nikita Korovkin 18.10.2018.
 */
public class RxEditorTextWatcher implements TextWatcher {
    protected String previousText;

    protected String text;

    protected PublishSubject<String> subject;

    protected WeakReference<EditText> editTextReference;

    protected EditorEnum editorEnum;

    @Inject
    EditorPreferencesUtils editorPreferencesUtils;

    public Observable<String> watch(EditText editText, EditorEnum editorEnum){



        editTextReference = new WeakReference<>(editText);

        editText.addTextChangedListener(this);

        text = editText.getText().toString();

        subject = PublishSubject.create();

        this.editorEnum = editorEnum;

        return subject;
    }

    public RxEditorTextWatcher() {
        TgramApplication.getInstance().getComponents().getEditorComponent().inject(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        previousText = text;
        text = s.toString();

        switch (editorEnum){
            case TITLE:
                editorPreferencesUtils.setTitle(text);
                break;
            case OGIMAGE:
                editorPreferencesUtils.setOgimage(text);
                break;
            case BODY:
                editorPreferencesUtils.setBody(text);
                break;
            case TAG:
                editorPreferencesUtils.setTag(text);
                break;
        }

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
