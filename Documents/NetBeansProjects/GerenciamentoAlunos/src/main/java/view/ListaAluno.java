package view;

import bean.Aluno;

import util.HibernateUtil;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.time.format.DateTimeFormatter;

public class ListaAluno extends javax.swing.JFrame {

    private JTable tabela;
    private JButton btnAtualizar, btnVoltar, btnMaisVelho, btnMaisNovo, btnRemover;
    private JButton btnInserir, btnSalvarCSV, btnBuscar;

    public ListaAluno() {
        configurarComponentes();
        carregarTabela();
    }

    private void configurarComponentes() {
        setTitle("Lista de Alunos");
        setSize(720, 420);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tabela = new JTable();
        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBounds(20, 20, 660, 200);
        add(scrollPane);

        btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.setBounds(20, 230, 150, 30);
        btnAtualizar.addActionListener(e -> carregarTabela());
        add(btnAtualizar);

        btnMaisVelho = new JButton("Aluno Mais Velho");
        btnMaisVelho.setBounds(180, 230, 150, 30);
        btnMaisVelho.addActionListener(e -> mostrarMaisVelho());
        add(btnMaisVelho);

        btnMaisNovo = new JButton("Aluno Mais Novo");
        btnMaisNovo.setBounds(340, 230, 150, 30);
        btnMaisNovo.addActionListener(e -> mostrarMaisNovo());
        add(btnMaisNovo);

        btnRemover = new JButton("Remover Aluno");
        btnRemover.setBounds(500, 230, 150, 30);
        btnRemover.addActionListener(e -> removerAluno());
        add(btnRemover);

        btnVoltar = new JButton("Voltar");
        btnVoltar.setBounds(20, 280, 100, 30);
        btnVoltar.addActionListener(e -> voltarParaAlunoForm());
        add(btnVoltar);

        btnInserir = new JButton("Inserir na Posição");
        btnInserir.setBounds(140, 280, 180, 30);
        btnInserir.addActionListener(e -> inserirNaPosicao());
        add(btnInserir);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(340, 280, 150, 30);
        btnBuscar.addActionListener(e -> btnBuscarActionPerformed(null));
        add(btnBuscar);

        btnSalvarCSV = new JButton("Salvar CSV");
        btnSalvarCSV.setBounds(500, 280, 150, 30);
        btnSalvarCSV.addActionListener(e -> salvarCSV());
        add(btnSalvarCSV);
    }

    private void carregarTabela() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Posição", "Matrícula", "Nome", "Data Nascimento", "Idade", "Telefone", "CPF"}, 0);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        List<Aluno> alunos = session.createQuery("FROM Aluno ORDER BY posicao ASC", Aluno.class).list();

        int novaPosicao = 1;
        for (Aluno a : alunos) {
            if (a.getPosicao() != novaPosicao) {
                a.setPosicao(novaPosicao);
                session.update(a);
            }
            novaPosicao++;
        }

        tx.commit();
        session.close();

        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Aluno> listaAtualizada = s.createQuery("FROM Aluno ORDER BY posicao ASC", Aluno.class).list();
        for (Aluno a : listaAtualizada) {
            model.addRow(new Object[]{
                    a.getPosicao(),
                    a.getMatricula(),
                    a.getNome(),
                    a.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    a.getIdade(),
                    a.getTelefone(),
                    a.getCpf()
            });
        }
        s.close();

        tabela.setModel(model);
    }

    private void mostrarMaisVelho() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Aluno maisVelho = session.createQuery("FROM Aluno ORDER BY dataNascimento ASC", Aluno.class)
                .setMaxResults(1).uniqueResult();
        session.close();

        if (maisVelho != null) {
            JOptionPane.showMessageDialog(this, "Mais velho: " + maisVelho.getNome() + " (" + maisVelho.getIdade() + " anos)");
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum aluno encontrado.");
        }
    }

    private void mostrarMaisNovo() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Aluno maisNovo = session.createQuery("FROM Aluno ORDER BY dataNascimento DESC", Aluno.class)
                .setMaxResults(1).uniqueResult();
        session.close();

        if (maisNovo != null) {
            JOptionPane.showMessageDialog(this, "Mais novo: " + maisNovo.getNome() + " (" + maisNovo.getIdade() + " anos)");
        } else {
            JOptionPane.showMessageDialog(this, "Nenhum aluno encontrado.");
        }
    }

    private void inserirNaPosicao() {
        String posStr = JOptionPane.showInputDialog(this, "Digite a posição desejada:");
        if (posStr == null || posStr.isEmpty()) return;

        int posicao;
        try {
            posicao = Integer.parseInt(posStr);
            if (posicao < 1) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Posição inválida.");
            return;
        }

        String matricula = JOptionPane.showInputDialog(this, "Matrícula:");
        String nome = JOptionPane.showInputDialog(this, "Nome:");
        String data = JOptionPane.showInputDialog(this, "Data Nasc (dd/MM/yyyy):");
        String telefone = JOptionPane.showInputDialog(this, "Telefone:");
        String cpf = JOptionPane.showInputDialog(this, "CPF:");

        if (matricula == null || nome == null || data == null || telefone == null || cpf == null) return;

        try {
            Aluno novoAluno = new Aluno();
            novoAluno.setMatricula(matricula.trim());
            novoAluno.setNome(nome.trim());
            novoAluno.setDataNascimento(data.trim());
            novoAluno.setTelefone(telefone.trim());
            novoAluno.setCpf(cpf.trim());

            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            List<Aluno> lista = session.createQuery("FROM Aluno ORDER BY posicao ASC", Aluno.class).list();
            for (Aluno a : lista) {
                if (a.getPosicao() >= posicao) {
                    a.setPosicao(a.getPosicao() + 1);
                    session.update(a);
                }
            }

            novoAluno.setPosicao(posicao);
            session.save(novoAluno);
            tx.commit();
            session.close();

            carregarTabela();
            JOptionPane.showMessageDialog(this, "Aluno inserido com sucesso na posição " + posicao);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao inserir aluno: " + ex.getMessage());
        }
    }

    private void salvarCSV() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Aluno> listaAtualizada = session.createQuery("FROM Aluno ORDER BY posicao ASC", Aluno.class).list();
            String desktopPath = System.getProperty("user.home") + "\\Desktop\\ListagemAlunos.csv";
            util.CSVUtil.salvarAlunos(listaAtualizada);
            JOptionPane.showMessageDialog(this, "CSV salvo na Área de Trabalho!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar CSV: " + e.getMessage());
        }
    }

    private void removerAluno() {
        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula do aluno a remover:");
        if (matricula == null || matricula.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Matrícula inválida.");
            return;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Aluno aluno = session.get(Aluno.class, matricula.trim());
        if (aluno != null) {
            session.remove(aluno);
            List<Aluno> listaAtualizada = session.createQuery("FROM Aluno ORDER BY posicao ASC", Aluno.class).list();
            int novaPos = 1;
            for (Aluno a : listaAtualizada) {
                a.setPosicao(novaPos++);
                session.update(a);
            }
            tx.commit();
            JOptionPane.showMessageDialog(this, "Aluno removido e posições atualizadas com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
            tx.rollback();
        }

        session.close();
        carregarTabela();
    }

    private void voltarParaAlunoForm() {
        new AlunoForm().setVisible(true);
        this.dispose();
    }

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        String matricula = JOptionPane.showInputDialog(this, "Digite a matrícula do aluno para buscar:");

        if (matricula == null || matricula.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Matrícula não informada.");
            return;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Aluno> lista = session.createQuery("FROM Aluno ORDER BY posicao ASC", Aluno.class).list();
        session.close();

        boolean encontrado = false;
        for (int i = 0; i < lista.size(); i++) {
            Aluno a = lista.get(i);
            if (a.getMatricula().equalsIgnoreCase(matricula.trim())) {
                tabela.setRowSelectionInterval(i, i);
                tabela.scrollRectToVisible(tabela.getCellRect(i, 0, true));
                JOptionPane.showMessageDialog(this,
                        "Aluno encontrado:\n" +
                        "Nome: " + a.getNome() + "\n" +
                        "Nascimento: " + a.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n" +
                        "Telefone: " + a.getTelefone() + "\n" +
                        "CPF: " + a.getCpf() + "\n" +
                        "Idade: " + a.getIdade() + " anos");
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ListaAluno().setVisible(true));
    }
}
