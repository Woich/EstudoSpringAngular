package com.example.moneyapi.event.listners;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.moneyapi.event.RecursoCriadoEvent;

@Component
public class RecursoCriadoListner implements ApplicationListener<RecursoCriadoEvent>{

	@Override
	public void onApplicationEvent(RecursoCriadoEvent eventoDeCriacao) {
		
		HttpServletResponse response = eventoDeCriacao.getResponse();
		Long codigo = eventoDeCriacao.getCodigo();
		
		
		//Retorna o caminho em que foi salva
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(codigo).toUri();
				
		//Adiciona a localização nos headers
		response.setHeader("Location", uri.toString());
	}
	
	

}
