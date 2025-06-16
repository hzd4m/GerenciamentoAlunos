package com.gerenciamentoalunos;

import javax.swing.SwingUtilities;
import view.AlunoForm;

public class GerenciamentoAlunos {

    public static void main(String[] args) {
        // Garante que a GUI seja criada na thread correta (EDT)
        SwingUtilities.invokeLater(() -> {
            new AlunoForm().setVisible(true);
        });
    }
}
