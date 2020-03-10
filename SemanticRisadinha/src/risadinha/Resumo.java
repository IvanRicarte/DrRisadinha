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

import java.io.Serializable;
import java.text.Collator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Organiza informações selecionadas referentes a uma
 * postagem no blog Dr Risadinha.
 * 
 * @author ricarte at ft.unicamp.br
 */
public class Resumo implements Serializable, Comparable {

    private final String titulo;
    private final LocalDateTime data;
    private String imagemUrl;
    private String mensagemCurta;
    private String mensagem;
    private final Collection<String> referencia;
    private final Collection<String> autor;
    private final Collection<String> revisor;
    private String[] labels;
    private long id;
    private LocalDateTime updated;
    private String url;
    
    public Resumo(String titulo, LocalDateTime data) {
        this.titulo = titulo;
        this.data = data;
        referencia = new ArrayList<>();
        autor = new ArrayList<>();
        revisor = new ArrayList<>();
    }
    
    public long getId() {
        return id;
    }
    
    
    public String getTitulo() {
        return titulo;
    }
    
    public String getUrl() {
        return url;
    }

    public LocalDateTime getData() {
        return data;
    }
    
    public LocalDateTime getUpdated() {
        return updated;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public String getMensagemCurta() {
        return mensagemCurta;
    }

    public String getMensagem() {
        return mensagem;
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
    
    public void setId(long id) {
        this.id = id;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public void setMensagemCurta(String mensagemCurta) {
        this.mensagemCurta = mensagemCurta;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void addReferencia(String referencia) {
        this.referencia.add(referencia);
    }

    public void addAutor(String autor) {
        this.autor.add(autor);
    }

    public void addRevisor(String revisor) {
        this.revisor.add(revisor);
    }
    
    public void setLabels(String[] labels) {
        this.labels = labels;
    }
    
    public String[] getLabels() {
        return labels;
    }

    @Override
    public String toString() {
        return getTitulo();
    }

    @Override
    public int compareTo(Object o) {
        Collator local = Collator.getInstance();
        Resumo outro = (Resumo) o;
        return local.compare(titulo, outro.titulo);
    }
}
