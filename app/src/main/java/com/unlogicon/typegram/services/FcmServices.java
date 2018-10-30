package com.unlogicon.typegram.services;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.unlogicon.typegram.Constants;
import com.unlogicon.typegram.TgramApplication;
import com.unlogicon.typegram.interfaces.dao.ArticlesDao;
import com.unlogicon.typegram.models.Article;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.schedulers.Schedulers;

public class FcmServices extends FirebaseMessagingService {

    @Inject
    ArticlesDao articlesDao;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        TgramApplication.getInstance().getComponents().getAppComponent().inject(this);
        if (remoteMessage.getFrom().equals("/topics/" + Constants.TOPIC) ||
                remoteMessage.getFrom().equals("/topics/" + Constants.TOPIC_DEL)) {
            insertArticles(remoteMessage)
                    .observeOn(Schedulers.io())
                    .subscribe();
        }
    }

    private Observable insertArticles(final RemoteMessage remoteMessage) {
        return new Observable() {
            @Override
            protected void subscribeActual(Observer observer) {

                Article articles = new Article();
                articles.setAuthor(remoteMessage.getData().get("Author"));
                articles.setCreatedAt(remoteMessage.getData().get("CreatedAt"));
                articles.setBody(remoteMessage.getData().get("body"));
                articles.setID(Integer.parseInt(remoteMessage.getData().get("ID")));
                articles.setTitle(remoteMessage.getData().get("title"));
                if (remoteMessage.getFrom().equals("/topics/" + Constants.TOPIC)) {
                    articlesDao.insertAll(articles);
                } else if (remoteMessage.getFrom().equals("/topics/" + Constants.TOPIC_DEL)){
                    articlesDao.delete(articles);
                }
            }
        };
    }
}
