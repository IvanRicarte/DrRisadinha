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
import org.apache.log4j.Logger;

/**
 * Extração dos elementos específicos do blog Dr Risadinha a partir do 'content' não-estruturado de uma postagem.
 *
 * @author ricarte at ft.unicamp.br
 */
public class ResumoContentParser {

    static Logger log = Logger.getLogger(ResumoContentParser.class);
    private final String linhaImagem;
    private final String linhaMensagemCurta;
    private final String linhaMensagem;
    private final String linhaReferencia;
    private final String linhaAutor;
    private final String linhaRevisores;

    public ResumoContentParser(String html) {
        linhaImagem = extrairUrlImagem(html);
        linhaMensagemCurta = extrairMensagemCurta(html);
        linhaMensagem = extrairMensagem(html);
        linhaReferencia = extrairReferencia(html);
        linhaAutor = extrairAutor(html);
        linhaRevisores = extrairRevisor(html);
    }

    public String cleanTags(String source) {
        StringBuilder result = new StringBuilder();
        int pos = 0;
        while (pos < source.length()) {
            if (source.charAt(pos) == '<') {
                while (source.charAt(pos) != '>') {
                    ++pos;
                }
            }
            result.append(source.charAt(pos++));
        }
        while (result.charAt(result.length() - 1) == '.') {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString().replace(">", "").replace("&nbsp;", " ").
                replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">").
                replace("\n\n", "\n");
    }

    public String getUrlImagem() {
        return linhaImagem;
    }

    public String getMensagemCurta() {
        return linhaMensagemCurta;
    }

    public String getMensagem() {
        return linhaMensagem;
    }

    public Collection<String> getReferencia() {
        return getMultipleNames(linhaReferencia, "\n");
    }

    public Collection<String> getAutor() {
        return getMultipleNames(linhaAutor, ",\n");
    }

    public Collection<String> getRevisor() {
        return getMultipleNames(linhaRevisores, ",\n");
    }

    private Collection<String> getMultipleNames(String source, String sep) {
        StringTokenizer linha = new StringTokenizer(source, sep);
        Collection<String> result = new ArrayList<>();
        while (linha.hasMoreTokens()) {
            result.add(linha.nextToken().trim());
        }
        return Collections.unmodifiableCollection(result);
    }

    private String extrairUrlImagem(String source) {
        int pos1 = source.indexOf("href") + 6;
        if (pos1 == -1) {
            return "";
        }
        int pos2 = source.indexOf('"', pos1);
        log.info("URL entre " + pos1 + " e " + pos2);
        return source.substring(pos1, pos2);
    }

    private String extrairMensagemCurta(String source) {
        String cleanSource = cleanTags(source);
        int posFinal = cleanSource.indexOf("***");
        log.info("Mensagem curta entre 0 e " + posFinal);
        if (posFinal > 0) {
            return cleanSource.substring(0, posFinal).replace("\n", " ").trim();
        } else {
            return "\n";
        }
    }

    private String extrairMensagem(String source) {
        String cleanSource = cleanTags(source);
        int posInicial = cleanSource.indexOf("***");
        if (posInicial == -1) {
            log.info("Mensagem: não identificada!");
            return "\n";
        }
        posInicial += 3;
        int posFinal = cleanSource.indexOf("Refer", posInicial);
        if (posFinal == -1) {
            posFinal = cleanSource.indexOf("Para ter mai", posInicial);
        }
        if (posFinal > 0) {
            log.info("Mensagem entre " + posInicial + " e " + posFinal);
            return cleanSource.substring(posInicial, posFinal).trim();
        } else {
            log.info("Mensagem: não identificada!");
            return "\n";
        }
    }

    private String extrairReferencia(String source) {
        int posInicial = source.indexOf("Referênc");
        if (posInicial == -1) {
            log.info("Referência: não identificada!");
            return "\n";
        }
        posInicial = source.indexOf("</b>", posInicial) + 4;
        int posFinal = source.indexOf("<b>", posInicial);
        if (posFinal == -1) {
            posFinal = source.indexOf("<a ", posInicial);
        }
        log.info("Referência entre " + posInicial + " e " + posFinal);
        return cleanTags(source.substring(posInicial, posFinal)).trim();
    }

    private String extrairAutor(String source) {
        int posInicial = source.indexOf("Autor");
        if (posInicial == -1) {
            log.info("Autor não identificado!");
            return "\n";
        }
        posInicial = source.indexOf("</b>", posInicial) + 4;
        int posFinal = source.indexOf("<b>", posInicial);
        if (posFinal == -1) {
            posFinal = source.indexOf("<a", posInicial);
        }
        log.info("Autor entre " + posInicial + " e " + posFinal);
        String autores = cleanTags(source.substring(posInicial, posFinal)).trim();
        return autores;
    }

    private String extrairRevisor(String source) {
        int posInicial = source.indexOf("Revisor");
        if (posInicial == -1) {
            log.info("Revisor não identificado!");
            return "\n";
        }
        posInicial = source.indexOf("</b>", posInicial) + 4;
        int posFinal = source.indexOf("<a ", posInicial);
        log.info("Revisor entre " + posInicial + " e " + posFinal);
        return cleanTags(source.substring(posInicial, posFinal)).trim();
    }
}
