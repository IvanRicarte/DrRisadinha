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
 * Cabeçalho do blog, objeto preenchido por API Blogger.
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
    
    /**
     * Get blog kind.
     * @return Blog kind.
     */
    public String getKind() {
        return kind;
    }
    
    /**
     * Get blog id.
     * @return Blog id.
     */
    public long getId() {
        return id;
    }
    
    /**
     * Get number of posts.
     * @return Number of posts.
     */
    public int getNumberOfPosts() {
        return posts.getTotalItems();
    }
    
    /**
     * Get blog name.
     * @return Name of the blog.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get blog description.
     * @return String with blog description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get blog URL.
     * @return String with URL.
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Get blog self link
     * @return Self link.
     */
    public String getSelfLink() {
        return selfLink;
    }
    
    /**
     * Get date of publication.
     * @return Date/Time of blog publication.
     */
    public LocalDateTime getPublished() {
        return OffsetDateTime.parse(published).toLocalDateTime();
    }
    
    /**
     * Get date of last update.
     * @return Date/Time of last update. 
     */
    public LocalDateTime getLastUpdated() {
        return OffsetDateTime.parse(updated).toLocalDateTime();
    }
    
    /**
     * Return the reference to all posts.
     * @return Object that contains the list of all posts. 
     */
    public AllPosts getAllPosts() {
        return allPosts;
    }
    
    /**
     * Set the reference to all posts.
     * @param allPosts The reference to all posts.
     */
    public void setAllPosts(AllPosts allPosts) {
        this.allPosts = allPosts;
    }

}
