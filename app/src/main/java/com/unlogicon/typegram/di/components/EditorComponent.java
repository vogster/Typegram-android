package com.unlogicon.typegram.di.components;

import com.unlogicon.typegram.di.modules.EditorPreferencesModule;
import com.unlogicon.typegram.di.scopes.EditorScope;
import com.unlogicon.typegram.presenters.activities.ArticleEditorPresenter;
import com.unlogicon.typegram.watchers.RxEditorTextWatcher;

import dagger.Component;

/**
 * Nikita Korovkin 18.10.2018.
 */

@Component(modules = {EditorPreferencesModule.class}, dependencies = AppComponent.class)
@EditorScope
public interface EditorComponent {

    void inject(RxEditorTextWatcher rxEditorTextWatcher);

    void inject(ArticleEditorPresenter presenter);
}
