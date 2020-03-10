/*
 * The MIT License
 *
 * Copyright 2020 Prof. Ivan L. M. Ricarte, FT-UNICAMP.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package bloggerdata;

import bloggerdata.json.PostPublisher;
import com.google.gson.JsonElement;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Mapeia o objeto JSON referente a uma postagem do blog.
 *
 * @author ricarte at ft.unicamp.br
 */
public class Post {
    private String kind;
    private long id;
    private JsonElement blog;
    private String published;
    private String updated;
    private String url;
    private String selfLink;
    private String title;
    private String content;
    private PostPublisher publisher;
    private JsonElement replies;
    private String[] labels;

    @Override
    public String toString() {
        StringBuilder post = new StringBuilder();
        post.append("Post.").append(id).append(": ").
                append(title).append(" / ").append(url);
        return post.toString();
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getUpdatedDate() {
        return OffsetDateTime.parse(updated).toLocalDateTime();
    }

    public LocalDateTime getPublishedDate() {
        return OffsetDateTime.parse(published).toLocalDateTime();
    }

    public String getContent() {
        return content;
    }

    public PostPublisher getPublisher() {
        return publisher;
    }

    public String[] getLabels() {
        return labels;
    }

    public String getUrl() {
        return url;
    }
}
