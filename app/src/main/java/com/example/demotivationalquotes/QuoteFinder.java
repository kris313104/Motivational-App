package com.example.demotivationalquotes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuoteFinder {
    protected List<String> searchQuote() throws IOException {


        List<String> tags = new ArrayList<String>();
        // generate random number for page and quote
        Random rand = new Random();

        int n = rand.nextInt(99);

        // setting up the url to a random page
        String url = "https://www.goodreads.com/quotes/tag/motivational?page=" + (n + 1);


        // connecting and getting quotes
        Document doc = Jsoup.connect(url).get();
        Elements e = doc.select("div.quoteText");


        int quotes = e.size();

        // get random quote from quotes
        n = rand.nextInt(quotes);

        String quote = e.get(n).text();

        // filter strings to get quote and author separately
        String quoteText = quote.replaceAll("\\―(.*)", "");

        String quoteAuthor = quote.substring(quote.indexOf("―"), quote.length());

        System.out.println(quoteText);

        System.out.println(quoteAuthor);

        tags.add(quoteText);
        tags.add(quoteAuthor);

        return tags;
    }
}
