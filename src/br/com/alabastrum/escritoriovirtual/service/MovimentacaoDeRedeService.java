package br.com.alabastrum.escritoriovirtual.service;

import br.com.alabastrum.escritoriovirtual.hibernate.HibernateUtil;
import br.com.alabastrum.escritoriovirtual.modelo.MovimentacaoRede;
import br.com.alabastrum.escritoriovirtual.util.Util;

import java.util.GregorianCalendar;

public class MovimentacaoDeRedeService {

    private HibernateUtil hibernateUtil;

    public MovimentacaoDeRedeService(HibernateUtil hibernateUtil) {

        this.hibernateUtil = hibernateUtil;
    }

    public MovimentacaoRede buscarMovimentacaoRede(GregorianCalendar data, Integer nivel) {

        GregorianCalendar primeiroDiaDoMes = Util.getPrimeiroDiaDoMes(data);

        MovimentacaoRede movimentacaoRedeFiltro = new MovimentacaoRede();
        movimentacaoRedeFiltro.setData(primeiroDiaDoMes);
        movimentacaoRedeFiltro.setNivel(nivel);
        return hibernateUtil.selecionar(movimentacaoRedeFiltro);
    }
}