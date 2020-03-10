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
package bloggerdata.json;

import com.google.gson.JsonElement;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Mapeia o objeto JSON com o cabeçalho
 * do blog.
 * 
 * @author ricarte at ft.unicamp.br
 */
public class BlogHeader {
    private String kind;
    private long id;
    private String name;
    private String description;
    private String published;
    private String updated;
    private String url;
    private String selfLink;
    private Resources posts;
    private Resources pages;
    private JsonElement locale;
    private AllPosts allPosts;
    
    public String getKind() {
        return kind;
    }
    
    public long getId() {
        return id;
    }
    
    public int getNumberOfPosts() {
        return posts.getTotalItems();
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getSelfLink() {
        return selfLink;
    }
    
    public LocalDateTime getPublished() {
        return OffsetDateTime.parse(published).toLocalDateTime();
    }
    
    public LocalDateTime getLastUpdated() {
        return OffsetDateTime.parse(updated).toLocalDateTime();
    }
    
    public AllPosts getAllPosts() {
        return allPosts;
    }
    
    public void setAllPosts(AllPosts allPosts) {
        this.allPosts = allPosts;
    }
    
    @Override
    public String toString() {
        return name + "[" +
                "id: " + id + " ; " +
                "updated: " + updated + " ; " +
                "posts: " + getNumberOfPosts() + "]";
    }
}
