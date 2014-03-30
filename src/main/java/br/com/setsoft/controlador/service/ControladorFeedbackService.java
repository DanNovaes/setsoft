package br.com.setsoft.controlador.service;

import javax.persistence.EntityManager;

import br.com.setsoft.crud.CrudGenerico;
import br.com.setsoft.modelo.Feedback;
import br.com.setsoft.utilidade.Fabrica;

public class ControladorFeedbackService extends CrudGenerico<Feedback, Integer> {
	
	@Override
	protected EntityManager getEntityManager() {
		
		return Fabrica.getEntityManager();
	}	
}