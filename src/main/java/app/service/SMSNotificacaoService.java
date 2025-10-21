package app.service;

import org.springframework.stereotype.Service;

@Service
public class SMSNotificacaoService implements NotificacaoService{

	@Override
	public String mensagem(String msg) {
		
		System.out.println("SMS de Notificação:  "+ msg);

		return "SMS de Notificação: " + msg;
		
	}

}
