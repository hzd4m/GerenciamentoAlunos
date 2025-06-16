package view;

import bean.Aluno;
import util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AlunoForm extends JFrame {
    private JTextField txtMatricula, txtNome, txtDataNascimento, txtTelefone, txtCpf, txtIdade;
    private JButton btnSalvar, btnListar;

    public AlunoForm() {
        setTitle("Cadastro de Aluno");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        JLabel lblTitulo = new JLabel("Cadastro de Aluno");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblMatricula = new JLabel("Matrícula:");
        JLabel lblNome = new JLabel("Nome:");
        JLabel lblDataNascimento = new JLabel("Data de Nascimento (dd/MM/yyyy):");
        JLabel lblTelefone = new JLabel("Telefone:");
        JLabel lblCpf = new JLabel("CPF:");
        JLabel lblIdade = new JLabel("Idade:");

        txtMatricula = new JTextField();
        txtNome = new JTextField();
        txtDataNascimento = new JTextField();
        txtTelefone = new JTextField();
        txtCpf = new JTextField();
        txtIdade = new JTextField();
        txtIdade.setEditable(false);

        btnSalvar = new JButton("Salvar");
        btnListar = new JButton("Listar Alunos");

        txtDataNascimento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                calcularIdade();
            }
        });

        btnSalvar.addActionListener(e -> salvarAluno());
        btnListar.addActionListener(e -> abrirListaAlunos());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(lblMatricula, gbc);
        gbc.gridx = 1;
        panel.add(txtMatricula, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(lblNome, gbc);
        gbc.gridx = 1;
        panel.add(txtNome, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(lblDataNascimento, gbc);
        gbc.gridx = 1;
        panel.add(txtDataNascimento, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(lblIdade, gbc);
        gbc.gridx = 1;
        panel.add(txtIdade, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(lblTelefone, gbc);
        gbc.gridx = 1;
        panel.add(txtTelefone, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(lblCpf, gbc);
        gbc.gridx = 1;
        panel.add(txtCpf, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(btnSalvar, gbc);
        gbc.gridx = 1;
        panel.add(btnListar, gbc);

        add(panel);
    }

    private void calcularIdade() {
        String data = txtDataNascimento.getText().trim();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate nascimento = LocalDate.parse(data, formatter);
            int idade = LocalDate.now().getYear() - nascimento.getYear();
            if (LocalDate.now().getDayOfYear() < nascimento.getDayOfYear()) idade--;
            txtIdade.setText(String.valueOf(idade));
        } catch (DateTimeParseException e) {
            txtIdade.setText("Data inválida");
        }
    }

    private void salvarAluno() {
        String matricula = txtMatricula.getText().trim();
        String nome = txtNome.getText().trim();
        String data = txtDataNascimento.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String cpf = txtCpf.getText().trim();

        try {
            Aluno aluno = new Aluno();
            aluno.setMatricula(matricula);
            aluno.setNome(nome);
            aluno.setDataNascimento(data);
            aluno.setTelefone(telefone);
            aluno.setCpf(cpf);

            Session session = HibernateUtil.getSessionFactory().openSession();
            Integer ultimaPosicao = (Integer) session.createQuery("SELECT MAX(posicao) FROM Aluno").uniqueResult();
            if (ultimaPosicao == null) ultimaPosicao = 0;
            aluno.setPosicao(ultimaPosicao + 1);

            Transaction tx = session.beginTransaction();
            session.save(aluno);
            tx.commit();
            session.close();

            JOptionPane.showMessageDialog(this, "Aluno salvo com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar aluno: " + ex.getMessage());
        }
    }

    private void abrirListaAlunos() {
        new ListaAluno().setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AlunoForm().setVisible(true));
    }
}
