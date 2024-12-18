package dato;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FRASE".
*/
public class NotificacionDao extends AbstractDao<Notificacion, Long> {

    public static final String TABLENAME = "NOTIFICACION";

    /**
     * Properties of entity Notificacion.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Nombre = new Property(1, String.class, "mensaje", false, "MENSAJE");
        public final static Property IdGrupo = new Property(2, Integer.class, "idGrupo", false, "ID_GRUPO");
        public final static Property Complemento = new Property(3, String.class, "complemento", false, "COMPLEMENTO");
    };


    public NotificacionDao(DaoConfig config) {
        super(config);
    }
    
    public NotificacionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NOTIFICACION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"MENSAJE\" TEXT," + // 1: nombre
                "\"ID_GRUPO\" INTEGER)," +// 2: idGrupo
                "\"COMPLEMENTO\" TEXT);"); // 3: complemento
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NOTIFICACION\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Notificacion entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String nombre = entity.getMensaje();
        if (nombre != null) {
            stmt.bindString(2, nombre);
        }
 
        Long idGrupo = entity.getIdGrupo();
        if (idGrupo != null) {
            stmt.bindLong(3, idGrupo);
        }

        String complemento = entity.getComplemento();
        if (complemento != null){
            stmt.bindString(4, complemento);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Notificacion readEntity(Cursor cursor, int offset) {
        Notificacion entity = new Notificacion( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // nombre
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // idPaquete
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3)
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Notificacion entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setMensaje(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIdGrupo(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setComplemento(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Notificacion entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Notificacion entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
