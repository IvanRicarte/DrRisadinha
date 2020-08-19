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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.StringTokenizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

/**
 * Extração dos elementos específicos do blog Dr Risadinha a partir do 'content' não-estruturado de uma postagem.
 *
 * @author ricarte at ft.unicamp.br
 */
public class ResumoHtmlContentParser {

    static Logger log = LogManager.getRootLogger();
    private final Document soup;
    private final String source;

    /**
     * Inicializar conteúdo para a extração de elementos.
     *
     * @param html Fragmento de HTML a ser analisado.
     */
    public ResumoHtmlContentParser(String html) {
        source = html;
        soup = Jsoup.parseBodyFragment(html);
    }

    /**
     * Obter URL da imagem representativa da publicação.
     *
     * @return String com URL da imagem.
     */
    public String getUrlImagem() {
        Element e = soup.getElementsByTag("img").first();
        return e.attr("src");
    }

    /**
     * Obter a mensagem curta.
     *
     * @return Mensagem curta.
     */
    public String getMensagemCurta() {
        String texts = Jsoup.clean(source, Whitelist.none());
        int end = texts.indexOf("***");
        if (end > 0) {
            return texts.substring(0, end).replace("&nbsp;", "").
                    replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");
        } else {
            return "";
        }
    }

    /**
     * Obter a mensagem completa.
     *
     * @return Mensagem completa.
     */
    public String getMensagem() {
        String texts = Jsoup.clean(source, Whitelist.none());
        int start = texts.indexOf("***");
        if (start > 0) {
            start += 3;
            int end = texts.indexOf("Referência");
            if (end > 0) {
                return texts.substring(start, end).replace("&nbsp;", "").
                        replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");
            } else {
                end = texts.indexOf("Para ter mai");
                if (end > 0) {
                    return texts.substring(start, end).replace("&nbsp;", "").
                            replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");
                } else {
                    return texts.substring(start).replace("&nbsp;", "").
                            replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");
                }
            }
        }
        return "";
    }

    /**
     * Obter as referências.
     *
     * @return Coleção de referências.
     */
    public Collection<String> getReferencia() {
        return getMultipleNames(extrairReferencia(), "\n");
    }

    /**
     * Obter os autores.
     *
     * @return Coleção de autores.
     */
    public Collection<String> getAutor() {
        return getMultipleNames(extrairAutor(), ",\n");
    }

    /**
     * Obter os revisores.
     *
     * @return Coleção de revisores.
     */
    public Collection<String> getRevisor() {
        return getMultipleNames(extrairRevisor(), ",\n");
    }

    private Collection<String> getMultipleNames(String source, String sep) {
        StringTokenizer linha = new StringTokenizer(source, sep);
        Collection<String> result = new ArrayList<>();
        String token;
        while (linha.hasMoreTokens()) {
            token = linha.nextToken().trim();
            if (!token.isEmpty()) {
                result.add(token);
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    private String extrairReferencia() {
        String texts = Jsoup.clean(source, Whitelist.none().addTags("br"));
        int posRef = texts.indexOf("Referênc");
        if (posRef > 0) {
            int start = texts.indexOf(":", posRef) + 1;
            if (start > 0) {
                int end = texts.indexOf("Autor");
                if (end > 0) {
                    return texts.substring(start, end).replace("&nbsp;", "").replace("<br>", "\n").
                            replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");
                }
            }
        }
        return "";
    }

    private String extrairAutor() {
        String texts = Jsoup.clean(source, Whitelist.none().addTags("br"));
        int posAut = texts.indexOf("Autor");
        if (posAut > 0) {
            int start = texts.indexOf(":", posAut) + 1;
            if (start > 0) {
                int end = texts.indexOf("Revisor");
                if (end > 0) {
                    return texts.substring(start, end).replace("&nbsp;", "").replace("<br>", "\n").
                            replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");
                } else {
                    end = texts.indexOf("Você achou");
                    if (end > 0) {
                        return texts.substring(start, end).replace("&nbsp;", "").replace("<br>", "\n").
                                replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");
                    } else {
                        return texts.substring(start).replace("&nbsp;", "").replace("<br>", "\n").
                                replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");
                    }
                }
            }
        }
        return "";
    }

    private String extrairRevisor() {
        String texts = Jsoup.clean(source, Whitelist.none().addTags("br"));
        int posRev = texts.indexOf("Revisor");
        if (posRev > 0) {
            int start = texts.indexOf(":", posRev) + 1;
            if (start > 0) {
                int end = texts.indexOf("Observação", start);
                if (end == -1) {
                    end = texts.indexOf("Você", start);
                    if (end == -1) {
                        end = texts.indexOf("Clique", start);
                    }
                }
                if (end > 0) {
                    return texts.substring(start, end).replace("&nbsp;", "").replace("<br>", "\n").
                            replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");
                } else {
                    return texts.substring(start).replace("&nbsp;", "").replace("<br>", "\n").
                            replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">");
                }
            }
        }
        return "";
    }
}
