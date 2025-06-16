/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import bean.Aluno;
import java.util.List; 


/**
 *
 * @author Hassan Zeidam && Filipe Gênesis 
 */
public interface AlunoDAO {
    List<Aluno> removerAluno(List<Aluno> alunos, Aluno a);
}
