package br.com.fiap.womanshield.factory;

import br.com.fiap.womanshield.dao.AlertaContatoDao;
import br.com.fiap.womanshield.dao.AlertaDao;
import br.com.fiap.womanshield.dao.BairroDao;
import br.com.fiap.womanshield.dao.ContatoConfiancaDao;
import br.com.fiap.womanshield.dao.DisponibilidadeDao;
import br.com.fiap.womanshield.dao.PeriodoDiaDao;
import br.com.fiap.womanshield.dao.UsuariaDao;
import br.com.fiap.womanshield.dao.impl.AlertaContatoDaoImpl;
import br.com.fiap.womanshield.dao.impl.AlertaDaoImpl;
import br.com.fiap.womanshield.dao.impl.BairroDaoImpl;
import br.com.fiap.womanshield.dao.impl.ContatoConfiancaDaoImpl;
import br.com.fiap.womanshield.dao.impl.DisponibilidadeDaoImpl;
import br.com.fiap.womanshield.dao.impl.PeriodoDiaDaoImpl;
import br.com.fiap.womanshield.dao.impl.UsuariaDaoImpl;

/**
 * Factory que esconde a implementacao concreta de cada DAO.
 * Troque aqui se um dia a implementacao mudar (ex.: outro banco).
 */
public final class DaoFactory {

    private DaoFactory() {
    }

    public static BairroDao getBairroDao() {
        return new BairroDaoImpl();
    }

    public static PeriodoDiaDao getPeriodoDiaDao() {
        return new PeriodoDiaDaoImpl();
    }

    public static UsuariaDao getUsuariaDao() {
        return new UsuariaDaoImpl();
    }

    public static ContatoConfiancaDao getContatoConfiancaDao() {
        return new ContatoConfiancaDaoImpl();
    }

    public static DisponibilidadeDao getDisponibilidadeDao() {
        return new DisponibilidadeDaoImpl();
    }

    public static AlertaDao getAlertaDao() {
        return new AlertaDaoImpl();
    }

    public static AlertaContatoDao getAlertaContatoDao() {
        return new AlertaContatoDaoImpl();
    }
}
