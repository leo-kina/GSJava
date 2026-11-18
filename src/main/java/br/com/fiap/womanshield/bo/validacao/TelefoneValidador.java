package br.com.fiap.womanshield.bo.validacao;

import br.com.fiap.womanshield.exception.CampoInvalidoException;

import java.util.Set;

/**
 * Validacao de telefone brasileiro, igual ao que ja roda na central web (ver LEIA-ME secao 4).
 * Grava so os digitos; o numero completo nunca volta pela API (ver MascaraTelefone).
 */
public final class TelefoneValidador {

    private static final Set<Integer> DDDS = Set.of(
            11, 12, 13, 14, 15, 16, 17, 18, 19, 21, 22, 24, 27, 28, 31, 32, 33, 34, 35, 37, 38,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 51, 53, 54, 55, 61, 62, 63, 64, 65, 66, 67, 68, 69,
            71, 73, 74, 75, 77, 79, 81, 82, 83, 84, 85, 86, 87, 88, 89, 91, 92, 93, 94, 95, 96, 97,
            98, 99);

    private TelefoneValidador() {
    }

    /**
     * @return somente os digitos do telefone (10 ou 11 digitos), validado.
     */
    public static String validar(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new CampoInvalidoException("Confira os campos destacados.", "telefone", "Telefone e obrigatorio.");
        }

        String d = texto.replaceAll("\\D", "");
        if ((d.length() == 12 || d.length() == 13) && d.startsWith("55")) {
            d = d.substring(2);
        }

        if (d.length() != 10 && d.length() != 11) {
            throw erro("Telefone deve ter DDD + numero, como (11) 91234-5678.");
        }
        if (!DDDS.contains(Integer.parseInt(d.substring(0, 2)))) {
            throw erro("DDD " + d.substring(0, 2) + " nao existe.");
        }
        if (d.length() == 11 && d.charAt(2) != '9') {
            throw erro("Celular com 11 digitos precisa comecar com 9 depois do DDD.");
        }
        if (d.length() == 10 && "2345".indexOf(d.charAt(2)) < 0) {
            throw erro("Telefone fixo deve comecar com 2, 3, 4 ou 5 depois do DDD.");
        }
        return d;
    }

    private static CampoInvalidoException erro(String motivo) {
        return new CampoInvalidoException("Confira os campos destacados.", "telefone", motivo);
    }
}
