package com.example.demo.Services;

import com.example.demo.Model.News.Article;
import com.example.demo.Model.News.Articles;
import com.example.demo.Model.News.Interest;
import com.example.demo.User;
import com.example.demo.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class NewsService {

    @Autowired
    UserRepository users;

    public Iterable<Article> articlesByCategory(String category) {
        RestTemplate fromApi = new RestTemplate();
        Articles articles = fromApi.getForObject("https://newsapi.org/v2/top-headlines?country=us&category=" + category + "&apiKey=3b28a13d11f14ee085aa28e77986059a", Articles.class);
        return articles.getArticles();
    }

    //    public Iterable<Article> articlesByInterests(String interests){
//        RestTemplate fromApi = new RestTemplate();
//        Articles articles = fromApi.getForObject("https://newsapi.org/v2/top-headlines?q="+interests+"&apiKey=3b28a13d11f14ee085aa28e77986059a", Articles.class);
//        return articles.getArticles();
//    }
    public Iterable<Article> articlesBySearch(String searchterms) {
        RestTemplate fromApi = new RestTemplate();
        Articles articles = fromApi.getForObject("https://newsapi.org/v2/everything?q=" + searchterms + "&from=" + LocalDate.now().minusDays(1) + "&sortBy=relevancy&apiKey=3b28a13d11f14ee085aa28e77986059a", Articles.class);

        return articles.getArticles();
    }

    public Iterable<Article> defaultArticles() {
        RestTemplate fromApi = new RestTemplate();
        Articles articles = fromApi.getForObject("https://newsapi.org/v2/top-headlines?country=us&apiKey=3b28a13d11f14ee085aa28e77986059a", Articles.class);

        return articles.getArticles();
    }

    public Iterable<Article> personalized(Authentication authentication) {
        User thisUser = users.findByUsername(authentication.getName());
        Set<Article> articles = new HashSet<>();

        for (Interest interest : thisUser.getInterests()) {
            for (Article article : articlesByCategory(interest.getName()))
                articles.add(article);
        }

        return articles;
    }
}
