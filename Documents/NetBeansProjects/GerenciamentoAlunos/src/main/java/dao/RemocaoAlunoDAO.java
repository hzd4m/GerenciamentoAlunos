/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import bean.Aluno;
import java.util.List;

/**
 *
 * @author Clinica do Notebook
 */
public class RemocaoAlunoDAO implements AlunoDAO { 
    
    @Override
    public List<Aluno> removerAluno(List<Aluno> alunos, Aluno a){
        alunos.removeIf(aluno -> aluno.getMatricula().equals(a.getMatricula()));
        return alunos;
    
}
}
