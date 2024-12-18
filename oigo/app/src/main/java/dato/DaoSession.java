package dato;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig usuarioDaoConfig;
    private final DaoConfig grupoDaoConfig;
    private final DaoConfig notificacionDaoConfig;
    private final DaoConfig contactoDaoConfig;
    private final DaoConfig mensajeDaoConfig;


    private final UsuarioDao usuarioDao;
    private final GrupoDao grupoDao;
    private final NotificacionDao notificacionDao;
    private final ContactoDao contactoDao;
    private final MensajeDao mensajeDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        usuarioDaoConfig = daoConfigMap.get(UsuarioDao.class).clone();
        usuarioDaoConfig.initIdentityScope(type);

        grupoDaoConfig = daoConfigMap.get(GrupoDao.class).clone();
        grupoDaoConfig.initIdentityScope(type);

        notificacionDaoConfig = daoConfigMap.get(NotificacionDao.class).clone();
        notificacionDaoConfig.initIdentityScope(type);

        contactoDaoConfig = daoConfigMap.get(ContactoDao.class).clone();
        contactoDaoConfig.initIdentityScope(type);

        mensajeDaoConfig = daoConfigMap.get(MensajeDao.class).clone();
        mensajeDaoConfig.initIdentityScope(type);


        usuarioDao = new UsuarioDao(usuarioDaoConfig, this);
        grupoDao = new GrupoDao(grupoDaoConfig, this);
        notificacionDao = new NotificacionDao(notificacionDaoConfig, this);
        contactoDao = new ContactoDao(contactoDaoConfig, this);
        mensajeDao = new MensajeDao(mensajeDaoConfig, this);

        registerDao(Usuario.class, usuarioDao);
        registerDao(Grupo.class, grupoDao);
        registerDao(Notificacion.class, notificacionDao);
        registerDao(Contacto.class, contactoDao);
        registerDao(Mensaje.class, mensajeDao);
    }
    
    public void clear() {
        usuarioDaoConfig.getIdentityScope().clear();
        grupoDaoConfig.getIdentityScope().clear();
        notificacionDaoConfig.getIdentityScope().clear();
        contactoDaoConfig.getIdentityScope().clear();
        mensajeDaoConfig.getIdentityScope().clear();
    }

    public UsuarioDao getUsuarioDao() {
        return usuarioDao;
    }

    public GrupoDao getGrupoDao() {
        return grupoDao;
    }

    public NotificacionDao getNotificacionDao() {
        return notificacionDao;
    }

    public ContactoDao getContactoDao() {
        return contactoDao;
    }

    public MensajeDao getMensajeDao() {
        return mensajeDao;
    }

}