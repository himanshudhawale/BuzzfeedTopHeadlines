package com.example.himanshudhawale.buzzfeedheadlines;

public class News {


     String title, urlToImage, publishedAt, description;

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
