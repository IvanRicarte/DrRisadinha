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

import bloggerdata.json.BlogHeader;
import bloggerdata.json.AllPosts;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Provê acesso às informações do blog via API de blogger. O acesso às informações públicas do blog depende de uma
 * chave, cujo valor deve estar definido na propriedade bloggerkey em um arquivo de propriedades 'risadinha.properties'
 * no diretório raiz do projeto.
 *
 * @author ricarte at ft.unicamp.br
 */
public class Blog {

    private final String base = "https://www.googleapis.com/blogger/v3/blogs/";
    private final String blogUrl;
    private final String key;
    private final Gson gson;
    private final BlogHeader header;

    /**
     * Inicializa a estrutura para o acesso ao blog indicado na url, com chave de acesso definida no arquivo de
     * propriedades do projeto.
     *
     * @param url Endereço URL do blog.
     * @throws MalformedURLException Endereço URL inválido.
     * @throws IOException Problema na transferência de dados do blog.
     */
    public Blog(String url) throws MalformedURLException, IOException {
        blogUrl = url;
        key = getKey();
        gson = new Gson();
        header = gson.fromJson(new InputStreamReader(
                new URL(base + "byurl?url=" + blogUrl + "&key=" + key).openStream()), BlogHeader.class);
    }

    /**
     * Obtém os dados do cabeçalho do blog.
     *
     * @return Referência para um BlogHeader, com os dados do cabeçalho.
     * @see bloggerdata.BlogHeader
     */
    private BlogHeader getBlogHeader() {
        return header;
    }

    public List<Post> getPosts() throws MalformedURLException, IOException {
        AllPosts list = gson.fromJson(new InputStreamReader(
                new URL(base + header.getId()
                        + "/posts?key=" + key
                        + "&maxResults=" + header.getNumberOfPosts()).openStream()),
                AllPosts.class);
        header.setAllPosts(list);
        return list.getPosts();
    }

    public Post getPost(long id) throws MalformedURLException, IOException {
        Post post = gson.fromJson(new InputStreamReader(
                new URL(base + getBlogHeader().getId()
                        + "/posts/" + id + "?key=" + key).openStream()), Post.class);
        return post;
    }

    private static String getKey() throws IOException {
        Properties blogProp = new Properties();
        blogProp.load(new FileInputStream("risadinha.properties"));
        return blogProp.getProperty("bloggerkey");
    }

    public String getName() {
        return header.getName();
    }

    public int getNumberOfPosts() {
        return header.getNumberOfPosts();
    }

    public LocalDateTime getLastUpdated() {
        return header.getLastUpdated();
    }

    public String getUrl() {
        return header.getUrl();
    }

    public long getId() {
        return header.getId();
    }

    public LocalDateTime getPublished() {
        return header.getPublished();
    }
    
    public String getSelfLink() {
        return header.getSelfLink();
    }
}
