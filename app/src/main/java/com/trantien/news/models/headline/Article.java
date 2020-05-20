package com.trantien.news.models.headline;

import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Article {
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    @SerializedName("source")
    @Expose
    private Source source;
    @SerializedName("author")
    @Expose
    private Object author;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("urlToImage")
    @Expose
    private String urlToImage;
    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;
    @SerializedName("content")
    @Expose
    private String content;

    public Article(Source source, Object author, String title, String description, String url, String urlToImage, String publishedAt, String content) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public Article(Source source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;

        Article article = (Article) o;

        if (source != null ? !source.equals(article.source) : article.source != null) return false;
        if (author != null ? !author.equals(article.author) : article.author != null) return false;
        if (title != null ? !title.equals(article.title) : article.title != null) return false;
        if (description != null ? !description.equals(article.description) : article.description != null)
            return false;
        if (url != null ? !url.equals(article.url) : article.url != null) return false;
        if (urlToImage != null ? !urlToImage.equals(article.urlToImage) : article.urlToImage != null)
            return false;
        if (publishedAt != null ? !publishedAt.equals(article.publishedAt) : article.publishedAt != null)
            return false;
        return content != null ? content.equals(article.content) : article.content == null;
    }

    @Override
    public int hashCode() {
        int result = source != null ? source.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (urlToImage != null ? urlToImage.hashCode() : 0);
        result = 31 * result + (publishedAt != null ? publishedAt.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @BindingAdapter("imageGlide")
    public static void setImage(AppCompatImageView imageView, String urlToImage){
        Glide
                .with(imageView.getContext())
                .load(urlToImage)
                .into(imageView);
    }

    @BindingAdapter("timePost")
    public static void setTime(MaterialTextView textView, String time){
        SimpleDateFormat input = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        PrettyTime prettyTime = new PrettyTime();

            Log.d("######", "setTime: " + time);
        try {
            String timeFormat = prettyTime.format(input.parse(time));
            Log.d("######", "setTime: " + timeFormat);
            textView.setText(timeFormat);
        } catch (ParseException e) {
            Log.d("######", "setTime: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Object getAuthor() {
        return author;
    }

    public void setAuthor(Object author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "source=" + source +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}