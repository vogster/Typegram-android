package com.unlogicon.typegram.utils;

import com.unlogicon.typegram.Constants;
import com.unlogicon.typegram.models.Article;

/**
 * Nikita Korovkin 11.10.2018.
 */
public class StringUtils {

    /**
     * Генерируем url для открытия в браузере
     * @param article
     * @return
     */
    public static String generateUrlFromArticle(Article article){
        StringBuilder builder = new StringBuilder();
        builder.append(Constants.BASE_URL);
        builder.append("@" + article.getAuthor() + "/");
        builder.append(article.getID());
        return builder.toString();
    }
}
