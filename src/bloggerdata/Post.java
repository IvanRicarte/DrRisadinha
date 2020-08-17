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
 * Dados de uma publicação do blog. Os campos dos objetos desta classe são
 * preenchidos pela API Blogger.
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

    private String cleanUtf(String source) {
        String result = source.replaceAll("[\u2000-\u200f]", "");
        result = result.replaceAll("[\u2010-\u2015]", "-");
        result = result.replaceAll("[\u2016-\u201f]", "'");
        return result;
    }

    /**
     * Obter título da publicação.
     *
     * @return O título da publicação.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Obter identificador da publicação.
     *
     * @return O identificador da publicação.
     */
    public long getId() {
        return id;
    }

    /**
     * Obter data da última atualização da publicação.
     *
     * @return A data da atualização.
     */
    public LocalDateTime getUpdatedDate() {
        return OffsetDateTime.parse(updated).toLocalDateTime();
    }

    /**
     * Obter data original da publicação.
     *
     * @return A data da publicação.
     */
    public LocalDateTime getPublishedDate() {
        return OffsetDateTime.parse(published).toLocalDateTime();
    }

    /**
     * Obter o conteúdo da publicação, retirando (se necessário) os caracteres
     * UTF-8 de espaço (\u2000 a \u200f) e hífen (\u2010 a \u2015).
     *
     * @return String com o conteúdo completo da publicação.
     */
    public String getContent() {
        return cleanUtf(content);
    }

    /**
     * Obter a representação do responsável pela publicação.
     *
     * @return Objeto que representa usuário da plataforma blogger.
     */
    public PostPublisher getPublisher() {
        return publisher;
    }

    /**
     * Obter marcadores da publicação.
     *
     * @return Arranjo com os marcadores.
     */
    public String[] getLabels() {
        if (labels != null) {
            for (String label : labels) {
                label = cleanUtf(label);
            }
        }
        return labels;
    }

    /**
     * Obter URL da publicação.
     *
     * @return String com URL da publicação.
     */
    public String getUrl() {
        return url;
    }
}
