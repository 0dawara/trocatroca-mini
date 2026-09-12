package br.unifor.trocatroca.infra;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class Console {

    private static final Console INSTANCIA = new Console();

    private final Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    private Console() {
    }

    public static Console get() {
        return INSTANCIA;
    }

    public String lerTexto(String rotulo) {
        while (true) {
            System.out.print(rotulo + ": ");
            String linha = scanner.nextLine().trim();
            if (!linha.isEmpty()) {
                return linha;
            }
        }
    }

    public String lerTextoOpcional(String rotulo, String atual) {
        System.out.print(rotulo + " [" + atual + "]: ");
        String linha = scanner.nextLine().trim();
        return linha.isEmpty() ? atual : linha;
    }

    public int lerInt(String rotulo) {
        while (true) {
            System.out.print(rotulo + ": ");
            String linha = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, digite um número.");
            }
        }
    }

    public long lerLong(String rotulo) {
        while (true) {
            System.out.print(rotulo + ": ");
            String linha = scanner.nextLine().trim();
            try {
                return Long.parseLong(linha);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, digite um número.");
            }
        }
    }

    public <E extends Enum<E>> E lerEnum(String rotulo, Class<E> tipo) {
        E[] constantes = tipo.getEnumConstants();
        while (true) {
            System.out.println(rotulo + ":");
            for (int i = 0; i < constantes.length; i++) {
                System.out.println((i + 1) + " - " + constantes[i].name());
            }
            int opcao = lerInt("Opção");
            if (opcao >= 1 && opcao <= constantes.length) {
                return constantes[opcao - 1];
            }
            System.out.println("Valor inválido, digite um número.");
        }
    }

    public <E extends Enum<E>> E lerEnumOpcional(String rotulo, Class<E> tipo, E atual) {
        E[] constantes = tipo.getEnumConstants();
        while (true) {
            System.out.println(rotulo + " [" + atual.name() + "]:");
            for (int i = 0; i < constantes.length; i++) {
                System.out.println((i + 1) + " - " + constantes[i].name());
            }
            System.out.print("Opção [Enter mantém atual]: ");
            String linha = scanner.nextLine().trim();
            if (linha.isEmpty()) {
                return atual;
            }
            try {
                int opcao = Integer.parseInt(linha);
                if (opcao == 0) {
                    return atual;
                }
                if (opcao >= 1 && opcao <= constantes.length) {
                    return constantes[opcao - 1];
                }
            } catch (NumberFormatException ignored) {
                // cai no aviso abaixo
            }
            System.out.println("Valor inválido, digite um número.");
        }
    }

    public void titulo(String t) {
        System.out.println("\n=== " + t + " ===");
    }

    public void info(String m) {
        System.out.println(m);
    }

    public void erro(String m) {
        System.out.println("[ERRO] " + m);
    }
}
