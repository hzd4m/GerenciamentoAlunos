/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.uespi.controller;


import br.uespi.model.Aluno; 
import java.util.ArrayList; 
import java.util.List; 


/**
 *
 * @author Hassan Zeidam && Filipe Genesis 
 */
public class AlunoController {
    private static List<Aluno> lista = new ArrayList<>(); 
    
    
    public static boolean adicionarAluno(Aluno aluno) throws Exception{
        for(int i = 0 ; i < lista.size() ; i++){
            if(lista.get(i).getMatricula().equals(aluno.getMatricula())){
                throw new Exception("Aluno já existente com essa matrícula.");
            }
        }
        
        return lista.add(aluno);
    }
    
    public static List<Aluno> getListaAlunos(){
        return lista;
    }
    
    public static boolean removerPorMatricula(String matricula){
        for(int i = 0 ; i < lista.size() ; i++){
            if(lista.get(i).getMatricula().equals(matricula)){
                lista.remove(i);
                return true;
            }
        }
        
        return false;
    }
    
    
}
