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

/**
 * Classe que representa os dados essenciais de uma referência.
 *
 * @author ricarte
 */
public class Referencia {
    private String source;
    private String description;
    private String url;
    private String accessed;
    private String updated;

    public Referencia(String ref) {
        parse(ref);
    }

    private void parse(String ref) {
        String disp = "Disponível em";
        String access = "Acesso em";
        String update1 = "Atualizado em";
        String update2 = "Informação atualizada em";
        String update;

        int endSource = ref.indexOf('.');
        source = ref.substring(0, endSource);
        
        int initDisp = ref.indexOf(disp);
        int initAccess = ref.indexOf(access);
        int initUpdate1 = ref.indexOf(update1);
        int initUpdate2 = ref.indexOf(update2);
        int initUpdate;

        if (initUpdate1 == -1 && initUpdate2 == -1) {
            initUpdate = -1;
            update = "";
        } else if (initUpdate2 == -1) {
            initUpdate = initUpdate1;
            update = update1;
        } else {
            initUpdate = initUpdate2;
            update = update2;
        }

        int endTitle;
        if (initDisp > 0 && initAccess > 0 && initUpdate > 0) {
            endTitle = Math.min(initDisp, Math.min(initAccess, initUpdate)) - 1;
        } else if (initDisp > 0 && initAccess > 0) {
            endTitle = Math.min(initDisp, initAccess) - 1;
        } else if (initDisp > 0 && initUpdate > 0) {
            endTitle = Math.min(initDisp, initUpdate) - 1;
        } else if (initAccess > 0 && initUpdate > 0) {
            endTitle = Math.min(initAccess, initUpdate) - 1;
        } else if (initDisp > 0) {
            endTitle = initDisp - 1;
        } else if (initAccess > 0) {
            endTitle = initAccess - 1;
        } else if (initUpdate > 0) {
            endTitle = initUpdate - 1;
        } else {
            endTitle = ref.length()-1;
        }

        if (endTitle > endSource) {
            description = ref.substring(endSource+1, endTitle).trim();
        } else {
            description = "";
        }

        if (initDisp > 0) {
            int endDisp;
            if (initAccess > initDisp && initUpdate > initDisp)
                endDisp = Math.min(initAccess, initUpdate);
            else if (initAccess > initDisp) 
                endDisp = initAccess;
            else if (initUpdate > initDisp)
                endDisp = initUpdate;
            else
                endDisp = ref.length()-1;
            
            url = ref.substring(initDisp + disp.length() + 1, endDisp).trim();
        } else {
            url = "";
        }

        if (initAccess > 0) {
            accessed = ref.substring(initAccess + access.length() + 1, ref.indexOf("20", initAccess) + 4).trim();
        } else {
            accessed = "";
        }

        if (initUpdate > 0) {
            updated = ref.substring(initUpdate + update.length() + 1, ref.indexOf("20", initUpdate) + 4).trim();
        } else {
            updated = "";
        }
    }

    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getAccessed() {
        return accessed;
    }

    public String getUpdated() {
        return updated;
    }

}
