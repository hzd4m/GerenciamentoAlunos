package util;

import bean.Aluno;
import java.io.BufferedWriter;
import java.io.FileWriter; 
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Hassan Zeidam && Filipe Gênesis 
 */
public class CSVUtil {

    public static void salvarAlunos(List<Aluno> alunos) throws IOException {
        // Caminho para a área de trabalho do usuário
        String desktopPath = System.getProperty("user.home") + "\\Desktop\\ListagemAlunos.txt";

        BufferedWriter writer = new BufferedWriter(new FileWriter(desktopPath));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        
        writer.write("Matricula,Nome,Idade,DataNascimento,Telefone,CPF");
        writer.newLine();

        for (Aluno aluno : alunos) {
            String linha = String.format("%s,%s,%d,%s,%s,%s",
                aluno.getMatricula(),
                aluno.getNome(),
                aluno.getIdade(),
                aluno.getDataNascimento().format(dtf),
                aluno.getTelefone(),
                aluno.getCpf()
            );
            writer.write(linha);
            writer.newLine();
        }

        writer.close();
    }
}
