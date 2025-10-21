package app.service;

import org.springframework.stereotype.Service;

@Service
public class EmailNotificacaoService implements NotificacaoService{

	@Override
	public String mensagem(String msg) {
		System.out.println("Email de Notificação:  "+ msg);
		return "Email de Notificação: " + msg;
	}

}
