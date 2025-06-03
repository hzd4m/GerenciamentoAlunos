package br.uespi.controller;

import br.uespi.model.Aluno;
import br.uespi.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

/**
 * Classe responsável por realizar as operações de CRUD com Hibernate.
 * Agora não usamos mais ArrayList em memória, e sim persistência real no MySQL.
 * 
 * @author Hassan Zeidam && Filipe Genesis
 */
public class AlunoController {

    /**
     * Adiciona um novo aluno ao banco de dados.
     * Verifica se já existe um aluno com a mesma matrícula antes de inserir.
     * @param aluno objeto Aluno a ser inserido
     * @return true se inseriu com sucesso
     * @throws Exception se a matrícula já existir
     */
    public static boolean adicionarAluno(Aluno aluno) throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        // Verifica se já existe um aluno com essa matrícula
        Aluno existente = session.get(Aluno.class, aluno.getMatricula());
        if (existente != null) {
            tx.rollback(); // desfaz a transação
            session.close();
            throw new Exception("Aluno já existente com essa matrícula.");
        }

        session.persist(aluno); // persiste o aluno no banco
        tx.commit();
        session.close();
        return true;
    }

    /**
     * Retorna a lista completa de alunos do banco.
     * @return lista de alunos
     */
    public static List<Aluno> getListaAlunos() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Aluno> lista = session.createQuery("from Aluno", Aluno.class).list();
        session.close();
        return lista;
    }

    /**
     * Remove um aluno por matrícula.
     * @param matricula matrícula do aluno a ser removido
     * @return true se removeu, false se não encontrou
     */
    public static boolean removerPorMatricula(String matricula) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Aluno aluno = session.get(Aluno.class, matricula);
        if (aluno != null) {
            session.remove(aluno);
            tx.commit();
            session.close();
            return true;
        }

        tx.rollback();
        session.close();
        return false;
    }
}
