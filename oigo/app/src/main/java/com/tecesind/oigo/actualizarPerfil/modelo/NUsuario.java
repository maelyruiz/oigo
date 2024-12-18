package com.tecesind.oigo.actualizarPerfil.modelo;

import java.util.List;

import dato.DaoMaster;
import dato.DaoSession;
import dato.Usuario;
import dato.UsuarioDao;

/**
 * Created by Rosember on 11/26/2015.
 */
public class NUsuario {



    public Usuario getUsuario(int id) {

        DaoSession daoSession = DaoMaster.getSession();
        UsuarioDao usuarioDao = daoSession.getUsuarioDao();

        Usuario usuarioOigo=null;

        //List<Usuario>listUsuario=usuarioDao.queryBuilder().where(UsuarioDao.Properties.Id.eq(id)).list();
        List<Usuario>listUsuario=usuarioDao.loadAll();

        if (listUsuario!=null) {
            for (Usuario usuario : listUsuario) {
                usuarioOigo= new Usuario();
                usuarioOigo.setFoto(usuario.getFoto());
                usuarioOigo.setId(usuario.getId());
                usuarioOigo.setNombre(usuario.getNombre());
                usuarioOigo.setTelefono(usuario.getTelefono());
            }
        }

        return usuarioOigo;
    }
}
