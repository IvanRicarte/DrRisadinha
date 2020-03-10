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
package risadinha;

import bloggerdata.Post;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.apache.log4j.Logger;

/**
 * Extrai informação específica de publicações do Dr Risadinha do 
 * conteúdo da publicação do blog.
 *
 * @author ricarte at ft.unicamp.br
 */
public class RisadinhaPost {

    static Logger log = Logger.getLogger(RisadinhaPost.class);
    private final Post post;
    private String mensagemCurta;
    private String mensagem;
    private String imageUrl;
    private Collection<String> referencia;
    private Collection<String> autor;
    private Collection<String> revisor;

    public RisadinhaPost(Post post) {
        this.post = post;
        referencia = new ArrayList<>();
        autor = new ArrayList<>();
        revisor = new ArrayList<>();
        log.info(">>> Processando " + post.getTitle());
        setPostContentData();
    }

    private void setPostContentData() {
        ResumoHtmlContentParser parser = new ResumoHtmlContentParser(post.getContent());
        mensagemCurta = parser.getMensagemCurta().trim();
        mensagem = parser.getMensagem().trim();
        imageUrl = parser.getUrlImagem();
        referencia = parser.getReferencia();
        autor = parser.getAutor();
        revisor = parser.getRevisor();
    }

    public String getTitle() {
        return post.getTitle();
    }

    public long getId() {
        return post.getId();
    }

    public LocalDateTime getUpdatedDate() {
        return post.getUpdatedDate();
    }

    public LocalDateTime getPublishedDate() {
        return post.getPublishedDate();
    }

    public String[] getLabels() {
        return post.getLabels();
    }

    public String getUrl() {
        return post.getUrl();
    }

    public String getMensagemCurta() {
        return mensagemCurta;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Collection<String> getReferencia() {
        return Collections.unmodifiableCollection(referencia);
    }

    public Collection<String> getAutor() {
        return Collections.unmodifiableCollection(autor);
    }

    public Collection<String> getRevisores() {
        return Collections.unmodifiableCollection(revisor);
    }

}
