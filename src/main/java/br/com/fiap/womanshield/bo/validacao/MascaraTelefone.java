package br.com.fiap.womanshield.bo.validacao;

/**
 * Mascara o telefone para a resposta da API: celular (DD) 9****-XXXX; fixo (DD) ****-XXXX.
 * O numero completo (ver TelefoneValidador) nunca sai da camada de persistencia/BO.
 */
public final class MascaraTelefone {

    private MascaraTelefone() {
    }

    public static String mascarar(String digitsOnly) {
        if (digitsOnly == null || digitsOnly.length() < 10) {
            return digitsOnly;
        }
        String ddd = digitsOnly.substring(0, 2);
        String ultimosQuatro = digitsOnly.substring(digitsOnly.length() - 4);
        if (digitsOnly.length() == 11) {
            char primeiroDigito = digitsOnly.charAt(2);
            return "(" + ddd + ") " + primeiroDigito + "****-" + ultimosQuatro;
        }
        return "(" + ddd + ") ****-" + ultimosQuatro;
    }
}
