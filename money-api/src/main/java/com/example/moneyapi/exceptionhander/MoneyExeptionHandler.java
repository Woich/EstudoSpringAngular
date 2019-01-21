package com.example.moneyapi.exceptionhander;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MoneyExeptionHandler extends ResponseEntityExceptionHandler{
	
	@Autowired
	private MessageSource messageSource;
	
	/*--------------------------- NÃO CONSEGUE LER O QUE FOI ENVIADO --------------------------*/
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String mesagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String mensagemDesevolvedor = ex.getCause().toString();
		
		Erro erro = new Erro(mesagemUsuario, mensagemDesevolvedor);
		
		List<Erro> erros = Arrays.asList(erro);
		
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	
	
	/*---------------------------------- ENVIADO VALOR NULL OU TAMANHO INVÁLIDO -----------------------------------*/
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
	
		List<Erro> erros = criaListaDeErros(ex.getBindingResult());
		
		
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	/*------------------------------------- ELEMENTO A SER DELETADO NÃO EXISTE ------------------------------------*/
	
	@ExceptionHandler({EmptyResultDataAccessException.class})
	public ResponseEntity<Object> handleEmptyResultDataAccessException(RuntimeException ex, WebRequest request){
		String mesagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
		String mensagemDesevolvedor = ex.getCause().toString();
		
		Erro erro = new Erro(mesagemUsuario, mensagemDesevolvedor);
		
		List<Erro> erros = Arrays.asList(erro);
		
		return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	
	
	
	/*------------------------------------- CLASSE DE ERRO ------------------------------------*/
	public static class Erro {
		
		private String menUsuario;
		private String menDesenvolvedor;
		
		/*-----------CONSTRUTOR-----------*/
		public Erro(String menUsuario, String menDesenvolvedor) {
			this.menUsuario = menUsuario;
			this.menDesenvolvedor = menDesenvolvedor;
		}
		
		/*-----------GETS-----------*/
		public String getMenUsuario() {
			return menUsuario;
		}
		
		public String getMenDesenvolvedor() {
			return menDesenvolvedor;
		}
		
	}
	
	/*------------------------------------- LISTA DE ERROS ------------------------------------*/
	
	private List<Erro> criaListaDeErros(BindingResult bindingResult) {
		
		List<Erro> erros = new ArrayList<>();
		
		//Para cada campo com erro que foi retornado
		for(FieldError fildError : bindingResult.getFieldErrors()) {
		
			String menUsuario = messageSource.getMessage(fildError, LocaleContextHolder.getLocale());
			String menDesenvolvedor = fildError.toString();
			
			erros.add(new Erro(menUsuario, menDesenvolvedor));
			
		}
		
		return erros;
	}

}
