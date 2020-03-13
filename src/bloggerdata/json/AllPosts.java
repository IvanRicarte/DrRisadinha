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

import bloggerdata.Post;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe que representa o mapeamento para o objeto JSON entregue por Blogger API
 * com todas as publicações do blog.
 * 
 * @author ricarte at ft.unicamp.br
 */
public class AllPosts {
    private String kind;
    private String nextPageToken;
    private final List<Post> items = new ArrayList<>();

    /**
     * Obtém a lista de todas as publicações.
     * 
     * @return Referência para lista (java.util.List) não modificável de publicações.
     */
    public List<Post> getPosts() {
        return Collections.unmodifiableList(items);
    }   
}
